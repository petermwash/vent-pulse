package com.nyoike.ventpulse.feature.identity.data

import com.nyoike.ventpulse.core.data.session.AnonymousSession
import com.nyoike.ventpulse.core.data.session.SessionPreferences
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentity
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreAnonymousIdentityRepository(
    private val sessionPreferences: SessionPreferences,
    private val generator: AnonymousIdentityGenerator
) : AnonymousIdentityRepository {

    override fun observeIdentity(): Flow<AnonymousIdentity?> {
        return sessionPreferences.anonymousSession.map { it.toIdentityOrNull() }
    }

    override suspend fun getOrCreateIdentity(): AnonymousIdentity {
        val current = sessionPreferences.anonymousSession.first().toIdentityOrNull()
        if (current != null) return current

        val created = generator.create()
        sessionPreferences.saveAnonymousSession(created.toSession())
        return created
    }

    override suspend fun updateCommunity(communityId: String) {
        val current = getOrCreateIdentity()
        sessionPreferences.saveAnonymousSession(current.copy(communityId = communityId).toSession())
    }
}

private fun AnonymousSession.toIdentityOrNull(): AnonymousIdentity? {
    val profileId = profileId ?: return null
    val alias = alias ?: return null
    val avatarSeed = avatarSeed ?: return null

    return AnonymousIdentity(
        profileId = profileId,
        alias = alias,
        avatarSeed = avatarSeed,
        communityId = communityId
    )
}

private fun AnonymousIdentity.toSession(): AnonymousSession {
    return AnonymousSession(
        profileId = profileId,
        alias = alias,
        avatarSeed = avatarSeed,
        communityId = communityId
    )
}
