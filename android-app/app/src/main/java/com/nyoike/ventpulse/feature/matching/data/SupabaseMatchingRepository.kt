package com.nyoike.ventpulse.feature.matching.data

import com.nyoike.ventpulse.core.data.supabase.SupabaseClientProvider
import com.nyoike.ventpulse.feature.coreflow.data.AnonymousProfileDto
import com.nyoike.ventpulse.feature.matching.domain.AnonymousMatch
import com.nyoike.ventpulse.feature.matching.domain.ChatMessage
import com.nyoike.ventpulse.feature.matching.domain.MatchStatus
import com.nyoike.ventpulse.feature.matching.domain.MatchingRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PrimaryKey
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresListDataFlow
import io.github.jan.supabase.realtime.postgresSingleDataFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.util.UUID

class SupabaseMatchingRepository(
    private val supabaseClientProvider: SupabaseClientProvider
) : MatchingRepository {

    override suspend fun startOrJoinMatch(profileId: String, communityId: String): AnonymousMatch {
        val client = supabaseClientProvider.client ?: return createDemoMatch(profileId, communityId)
        ensureRemoteProfile(profileId = profileId, communityId = communityId)

        return runCatching {
            findExistingActiveMatch(profileId, communityId)?.toAnonymousMatch(profileId)
                ?: joinOldestWaitingMatch(profileId, communityId)?.toAnonymousMatch(profileId)
                ?: createWaitingMatch(profileId, communityId).toAnonymousMatch(profileId)
        }.getOrElse {
            createDemoMatch(profileId, communityId)
        }
    }

    override suspend fun createDemoMatch(profileId: String, communityId: String): AnonymousMatch {
        return AnonymousMatch(
            id = "demo-${profileId.take(8)}",
            communityId = communityId,
            initiatorProfileId = profileId,
            partnerProfileId = "demo-gentle-orbit",
            status = MatchStatus.ACTIVE,
            partnerAlias = "Gentle Orbit",
            partnerAvatarSeed = "gentle-orbit",
            currentProfileId = profileId,
            isDemo = true
        )
    }

    override fun observeMatch(matchId: String, fallback: AnonymousMatch): Flow<AnonymousMatch> {
        val client = supabaseClientProvider.client
        if (client == null || fallback.isDemo) return flowOf(fallback)

        return flow {
            val channel = client.channel("match:$matchId")
            val dataFlow = channel.postgresSingleDataFlow(
                schema = "public",
                table = "matches",
                primaryKey = PrimaryKey<MatchDto>("id") { it.id }
            ) {
                eq("id", matchId)
            }.map { it.toAnonymousMatch(fallback.currentProfileId) }

            channel.subscribe()
            try {
                emitAll(dataFlow)
            } finally {
                channel.unsubscribe()
            }
        }.catch {
            emit(fallback)
        }
    }

    override fun observeMessages(
        matchId: String,
        profileId: String,
        fallback: AnonymousMatch
    ): Flow<List<ChatMessage>> {
        val client = supabaseClientProvider.client
        if (client == null || fallback.isDemo) return flowOf(demoMessages(fallback, profileId))

        return flow {
            val channel = client.channel("chat:$matchId")
            val filter = FilterOperation("match_id", FilterOperator.EQ, matchId)
            val dataFlow = channel.postgresListDataFlow(
                schema = "public",
                table = "chat_messages",
                filter = filter,
                primaryKey = PrimaryKey<ChatMessageDto>("id") { it.id.orEmpty() }
            ).map { messages ->
                messages
                    .sortedBy { it.createdAt.orEmpty() }
                    .map { it.toChatMessage(profileId) }
                    .ifEmpty { demoMessages(fallback, profileId) }
            }

            channel.subscribe()
            try {
                emitAll(dataFlow)
            } finally {
                channel.unsubscribe()
            }
        }.catch {
            emit(demoMessages(fallback, profileId))
        }
    }

    override suspend fun sendMessage(match: AnonymousMatch, profileId: String, body: String): ChatMessage {
        val trimmed = body.trim()
        if (trimmed.isBlank()) return localMessage(match.id, profileId, trimmed, isMine = true)
        if (match.isDemo) return localMessage(match.id, profileId, trimmed, isMine = true)

        val client = supabaseClientProvider.client ?: return localMessage(match.id, profileId, trimmed, isMine = true)
        return runCatching {
            ensureRemoteProfile(profileId = profileId, communityId = match.communityId)
            client.from("chat_messages").insert(
                ChatMessageDto(
                    matchId = match.id,
                    profileId = profileId,
                    body = trimmed
                )
            ) {
                select(
                    Columns.raw(
                        "id,match_id,profile_id,body,moderation_status,created_at,anonymous_profiles(alias,avatar_seed)"
                    )
                )
            }.decodeSingle<ChatMessageDto>().toChatMessage(profileId)
        }.getOrElse {
            localMessage(match.id, profileId, trimmed, isMine = true)
        }
    }

    override suspend fun endMatch(match: AnonymousMatch, profileId: String) {
        if (match.isDemo) return
        val client = supabaseClientProvider.client ?: return
        runCatching {
            client.from("matches").update(EndMatchDto(endedAt = Instant.now().toString())) {
                filter { eq("id", match.id) }
            }
        }
    }

    override suspend fun reportMessage(profileId: String, message: ChatMessage) {
        if (message.id.startsWith("demo-") || message.id.startsWith("local-")) return
        val client = supabaseClientProvider.client ?: return
        runCatching {
            client.from("reports").insert(
                ChatReportDto(
                    chatMessageId = message.id,
                    reporterProfileId = profileId,
                    reportedProfileId = message.profileId.takeUnless { it == profileId }
                )
            )
        }
    }

    private suspend fun findExistingActiveMatch(profileId: String, communityId: String): MatchDto? {
        val client = supabaseClientProvider.client ?: return null
        return client.from("matches")
            .select {
                filter {
                    eq("community_id", communityId)
                    eq("status", "active")
                }
                order("updated_at", Order.DESCENDING)
                limit(12)
            }
            .decodeList<MatchDto>()
            .firstOrNull { it.initiatorProfileId == profileId || it.partnerProfileId == profileId }
    }

    private suspend fun joinOldestWaitingMatch(profileId: String, communityId: String): MatchDto? {
        val client = supabaseClientProvider.client ?: return null
        val waitingMatch = client.from("matches")
            .select {
                filter {
                    eq("community_id", communityId)
                    eq("status", "waiting")
                    exact("partner_profile_id", null)
                }
                order("created_at", Order.ASCENDING)
                limit(8)
            }
            .decodeList<MatchDto>()
            .firstOrNull { it.initiatorProfileId != profileId }
            ?: return null

        return client.from("matches").update(JoinMatchDto(partnerProfileId = profileId)) {
            filter { eq("id", waitingMatch.id) }
            select()
        }.decodeSingleOrNull<MatchDto>()
    }

    private suspend fun createWaitingMatch(profileId: String, communityId: String): MatchDto {
        val client = supabaseClientProvider.client ?: error("Supabase client is not configured.")
        return client.from("matches").insert(
            CreateMatchDto(
                communityId = communityId,
                initiatorProfileId = profileId
            )
        ) {
            select()
        }.decodeSingle<MatchDto>()
    }

    private suspend fun ensureRemoteProfile(profileId: String, communityId: String) {
        val client = supabaseClientProvider.client ?: return
        runCatching {
            if (client.auth.currentUserOrNull() == null) {
                client.auth.signInAnonymously()
            }
            val userId = client.auth.currentUserOrNull()?.id ?: return
            client.from("anonymous_profiles").upsert(
                AnonymousProfileDto(
                    id = profileId,
                    userId = userId,
                    alias = "Quiet Cloud",
                    avatarSeed = "quiet-cloud",
                    communityId = communityId
                )
            )
        }
    }

    private fun demoMessages(match: AnonymousMatch, profileId: String): List<ChatMessage> {
        return listOf(
            ChatMessage(
                id = "demo-1",
                matchId = match.id,
                profileId = match.partnerProfileId ?: "demo-gentle-orbit",
                alias = match.partnerAlias,
                avatarSeed = match.partnerAvatarSeed,
                body = "I'm here with you. Want to just take a breath and tell me what feels heaviest right now?",
                createdAt = "now",
                isMine = false
            ),
            ChatMessage(
                id = "demo-2",
                matchId = match.id,
                profileId = profileId,
                alias = "You",
                avatarSeed = profileId,
                body = "I think I needed someone to hear me without trying to fix everything.",
                createdAt = "now",
                isMine = true
            )
        )
    }

    private fun localMessage(matchId: String, profileId: String, body: String, isMine: Boolean): ChatMessage {
        return ChatMessage(
            id = "local-${UUID.randomUUID()}",
            matchId = matchId,
            profileId = profileId,
            alias = if (isMine) "You" else "Gentle Orbit",
            avatarSeed = profileId,
            body = body,
            createdAt = "now",
            isMine = isMine
        )
    }
}
