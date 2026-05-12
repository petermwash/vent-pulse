package com.nyoike.ventpulse.feature.coreflow.data

import com.nyoike.ventpulse.core.data.database.CachedMoodCheckInEntity
import com.nyoike.ventpulse.core.data.database.CachedVentEntity
import com.nyoike.ventpulse.core.data.database.CommunityDao
import com.nyoike.ventpulse.core.data.database.CoreFlowDao
import com.nyoike.ventpulse.core.data.supabase.SupabaseClientProvider
import com.nyoike.ventpulse.feature.coreflow.domain.Community
import com.nyoike.ventpulse.feature.coreflow.domain.CoreFlowRepository
import com.nyoike.ventpulse.feature.coreflow.domain.Mood
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class OfflineFirstCoreFlowRepository(
    private val communityDao: CommunityDao,
    private val coreFlowDao: CoreFlowDao,
    private val supabaseClientProvider: SupabaseClientProvider
) : CoreFlowRepository {

    override fun observeCommunities(): Flow<List<Community>> {
        return communityDao.observeCommunities().map { communities ->
            if (communities.isEmpty()) fallbackCommunities else communities.map { it.toCommunity() }
        }
    }

    override suspend fun syncCommunities() {
        val client = supabaseClientProvider.client ?: return
        runCatching {
            val remote = client
                .from("communities")
                .select()
                .decodeList<CommunityDto>()
            communityDao.upsertAll(remote.map { it.toEntity() })
        }
    }

    override suspend fun saveMoodCheckIn(
        profileId: String,
        communityId: String,
        mood: Mood,
        intensity: Int
    ) {
        coreFlowDao.insertMoodCheckIn(
            CachedMoodCheckInEntity(
                id = UUID.randomUUID().toString(),
                profileId = profileId,
                communityId = communityId,
                mood = mood.id,
                intensity = intensity,
                createdAtMillis = System.currentTimeMillis(),
                synced = false
            )
        )
        ensureRemoteProfile(profileId = profileId, communityId = communityId)
        supabaseClientProvider.client
            ?.from("mood_checkins")
            ?.insert(
                MoodCheckInDto(
                    profileId = profileId,
                    communityId = communityId,
                    mood = mood.id,
                    intensity = intensity
                )
            )
    }

    override suspend fun saveVent(
        profileId: String,
        communityId: String,
        mood: Mood,
        body: String
    ) {
        coreFlowDao.insertVent(
            CachedVentEntity(
                id = UUID.randomUUID().toString(),
                profileId = profileId,
                communityId = communityId,
                mood = mood.id,
                body = body,
                createdAtMillis = System.currentTimeMillis(),
                synced = false
            )
        )
        ensureRemoteProfile(profileId = profileId, communityId = communityId)
        supabaseClientProvider.client
            ?.from("vents")
            ?.insert(
                VentDto(
                    profileId = profileId,
                    communityId = communityId,
                    mood = mood.id,
                    body = body
                )
            )
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

    private companion object {
        val fallbackCommunities = listOf(
            Community(
                id = "00000000-0000-4000-8000-000000000002",
                parentId = "00000000-0000-4000-8000-000000000001",
                name = "Nairobi",
                path = "kenya/nairobi",
                level = "city",
                sortOrder = 1
            ),
            Community(
                id = "00000000-0000-4000-8000-000000000003",
                parentId = "00000000-0000-4000-8000-000000000002",
                name = "Westlands",
                path = "kenya/nairobi/westlands",
                level = "local_community",
                sortOrder = 1
            ),
            Community(
                id = "00000000-0000-4000-8000-000000000006",
                parentId = "00000000-0000-4000-8000-000000000003",
                name = "Brookside",
                path = "kenya/nairobi/westlands/brookside",
                level = "neighborhood",
                sortOrder = 1
            )
        )
    }
}
