package com.nyoike.ventpulse.feature.coreflow.domain

import kotlinx.coroutines.flow.Flow

interface CoreFlowRepository {
    fun observeCommunities(): Flow<List<Community>>
    suspend fun syncCommunities()
    suspend fun saveMoodCheckIn(
        profileId: String,
        communityId: String,
        mood: Mood,
        intensity: Int
    )
    suspend fun saveVent(
        profileId: String,
        communityId: String,
        mood: Mood,
        body: String
    )
}
