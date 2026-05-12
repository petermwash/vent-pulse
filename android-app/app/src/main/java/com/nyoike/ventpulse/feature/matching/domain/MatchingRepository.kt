package com.nyoike.ventpulse.feature.matching.domain

import kotlinx.coroutines.flow.Flow

interface MatchingRepository {
    suspend fun startOrJoinMatch(profileId: String, communityId: String): AnonymousMatch
    suspend fun createDemoMatch(profileId: String, communityId: String): AnonymousMatch
    fun observeMatch(matchId: String, fallback: AnonymousMatch): Flow<AnonymousMatch>
    fun observeMessages(matchId: String, profileId: String, fallback: AnonymousMatch): Flow<List<ChatMessage>>
    suspend fun sendMessage(match: AnonymousMatch, profileId: String, body: String): ChatMessage
    suspend fun endMatch(match: AnonymousMatch, profileId: String)
    suspend fun reportMessage(profileId: String, message: ChatMessage)
}
