package com.nyoike.ventpulse.feature.communitypulse.domain

interface CommunityPulseRepository {
    suspend fun loadFeed(communityId: String, profileId: String): List<CommunityVent>
    suspend fun loadMoodSummary(communityId: String): List<MoodShare>
    suspend fun loadExperts(communityId: String): List<ExpertSupport>
    suspend fun reactToVent(profileId: String, communityId: String, ventId: String)
    suspend fun reportVent(profileId: String, communityId: String, ventId: String)
}
