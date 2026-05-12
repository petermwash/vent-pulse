package com.nyoike.ventpulse.feature.communitypulse.data

import com.nyoike.ventpulse.core.data.supabase.SupabaseClientProvider
import com.nyoike.ventpulse.feature.communitypulse.domain.CommunityPulseRepository
import com.nyoike.ventpulse.feature.communitypulse.domain.CommunityVent
import com.nyoike.ventpulse.feature.communitypulse.domain.ExpertSupport
import com.nyoike.ventpulse.feature.communitypulse.domain.MoodShare
import com.nyoike.ventpulse.feature.coreflow.data.AnonymousProfileDto
import com.nyoike.ventpulse.feature.coreflow.domain.Mood
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class SupabaseCommunityPulseRepository(
    private val supabaseClientProvider: SupabaseClientProvider
) : CommunityPulseRepository {

    override suspend fun loadFeed(communityId: String, profileId: String): List<CommunityVent> {
        val client = supabaseClientProvider.client ?: return fallbackFeed
        ensureRemoteProfile(profileId = profileId, communityId = communityId)
        return runCatching {
            val vents = client.from("vents")
                .select(
                    columns = Columns.raw(
                        "id,profile_id,community_id,mood,body,created_at,anonymous_profiles(alias,avatar_seed)"
                    )
                ) {
                    filter {
                        eq("community_id", communityId)
                        eq("moderation_status", "visible")
                    }
                    order("created_at", Order.DESCENDING)
                    limit(20)
                }
                .decodeList<FeedVentDto>()
            if (vents.isEmpty()) return@runCatching fallbackFeed

            val ventIds = vents.map { it.id }
            val reactions = client.from("vent_reactions")
                .select {
                    filter { isIn("vent_id", ventIds) }
                }
                .decodeList<VentReactionDto>()
            val reactionCounts = reactions.groupingBy { it.ventId }.eachCount()
            val reactedVentIds = reactions
                .filter { it.profileId == profileId }
                .map { it.ventId }
                .toSet()

            vents.map {
                it.toCommunityVent(
                    reactionCount = reactionCounts[it.id] ?: 0,
                    hasReacted = it.id in reactedVentIds
                )
            }
        }.getOrDefault(fallbackFeed)
    }

    override suspend fun loadMoodSummary(communityId: String): List<MoodShare> {
        val client = supabaseClientProvider.client ?: return fallbackMoodShares
        ensureAnonymousSession()
        return runCatching {
            val checkIns = client.from("mood_checkins")
                .select(columns = Columns.list("mood")) {
                    filter { eq("community_id", communityId) }
                    limit(100)
                }
                .decodeList<MoodOnlyDto>()
            val counts = checkIns.groupingBy { it.mood }.eachCount()
            counts.map { MoodSummaryDto(mood = it.key, count = it.value) }.toMoodShares()
        }.getOrDefault(fallbackMoodShares)
    }

    override suspend fun loadExperts(communityId: String): List<ExpertSupport> {
        val client = supabaseClientProvider.client ?: return fallbackExperts
        return runCatching {
            val experts = client.from("expert_profiles")
                .select {
                    filter {
                        or {
                            eq("community_id", communityId)
                            exact("community_id", null)
                        }
                    }
                    order("availability_status", Order.ASCENDING)
                }
                .decodeList<ExpertProfileDto>()
            experts.map { it.toExpertSupport() }.ifEmpty { fallbackExperts }
        }.getOrDefault(fallbackExperts)
    }

    override suspend fun reactToVent(profileId: String, communityId: String, ventId: String) {
        ensureRemoteProfile(profileId = profileId, communityId = communityId)
        supabaseClientProvider.client?.from("vent_reactions")?.insert(
            VentReactionDto(
                ventId = ventId,
                profileId = profileId,
                reactionType = "i_hear_you"
            )
        )
    }

    override suspend fun reportVent(profileId: String, communityId: String, ventId: String) {
        ensureRemoteProfile(profileId = profileId, communityId = communityId)
        supabaseClientProvider.client?.from("reports")?.insert(
            ReportDto(
                ventId = ventId,
                reporterProfileId = profileId,
                reason = "harmful_content",
                details = "Reported from Android community pulse."
            )
        )
    }

    private suspend fun ensureRemoteProfile(profileId: String, communityId: String) {
        val client = supabaseClientProvider.client ?: return
        runCatching {
            ensureAnonymousSession()
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

    private suspend fun ensureAnonymousSession() {
        val client = supabaseClientProvider.client ?: return
        if (client.auth.currentUserOrNull() == null) {
            client.auth.signInAnonymously()
        }
    }

    private companion object {
        val fallbackFeed = listOf(
            CommunityVent(
                id = "fallback-anxious",
                profileId = "quiet-cloud",
                alias = "Quiet Cloud",
                avatarSeed = "quiet-cloud",
                mood = Mood.ANXIOUS,
                body = "Sometimes I feel like I'm the only one who doesn't have it all figured out. Watching everyone else move forward while I'm stuck in the same place.",
                createdAt = "5m ago",
                reactionCount = 12,
                commentCount = 12,
                hasReacted = false
            ),
            CommunityVent(
                id = "fallback-sad",
                profileId = "soft-river",
                alias = "Soft River",
                avatarSeed = "soft-river",
                mood = Mood.SAD,
                body = "Missing someone who was never really mine to miss. It's a strange kind of loss.",
                createdAt = "23m ago",
                reactionCount = 28,
                commentCount = 28,
                hasReacted = false
            ),
            CommunityVent(
                id = "fallback-calm",
                profileId = "gentle-orbit",
                alias = "Gentle Orbit",
                avatarSeed = "gentle-orbit",
                mood = Mood.CALM,
                body = "Today I chose to rest instead of pushing through. Small victories.",
                createdAt = "1h ago",
                reactionCount = 45,
                commentCount = 45,
                hasReacted = false
            )
        )

        val fallbackExperts = listOf(
            ExpertSupport(
                id = "sarah-chen",
                displayName = "Dr. Sarah Chen",
                role = "Licensed Therapist",
                specialty = "Anxiety & Stress",
                avatarSeed = "sarah-chen",
                availabilityStatus = "available",
                bio = "Supports grounding, stress cycles, and gentle next steps."
            ),
            ExpertSupport(
                id = "michael-torres",
                displayName = "Michael Torres",
                role = "Emotional Support Listener",
                specialty = "Depression & Loneliness",
                avatarSeed = "michael-torres",
                availabilityStatus = "available",
                bio = "Trained listener for reflective emotional support."
            )
        )
    }
}

@kotlinx.serialization.Serializable
private data class MoodOnlyDto(
    @kotlinx.serialization.SerialName("mood") val mood: String
)
