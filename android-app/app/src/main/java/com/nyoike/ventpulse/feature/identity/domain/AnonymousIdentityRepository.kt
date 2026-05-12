package com.nyoike.ventpulse.feature.identity.domain

import kotlinx.coroutines.flow.Flow

interface AnonymousIdentityRepository {
    fun observeIdentity(): Flow<AnonymousIdentity?>
    suspend fun getOrCreateIdentity(): AnonymousIdentity
    suspend fun updateCommunity(communityId: String)
}
