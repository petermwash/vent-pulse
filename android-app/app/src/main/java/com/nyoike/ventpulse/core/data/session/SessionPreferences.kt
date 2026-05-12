package com.nyoike.ventpulse.core.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class AnonymousSession(
    val profileId: String?,
    val alias: String?,
    val avatarSeed: String?,
    val communityId: String?
)

class SessionPreferences(
    private val dataStore: DataStore<Preferences>
) {
    val anonymousSession: Flow<AnonymousSession> = dataStore.data.map { preferences ->
        AnonymousSession(
            profileId = preferences[PROFILE_ID],
            alias = preferences[ALIAS],
            avatarSeed = preferences[AVATAR_SEED],
            communityId = preferences[COMMUNITY_ID]
        )
    }

    suspend fun saveAnonymousSession(session: AnonymousSession) {
        dataStore.edit { preferences ->
            session.profileId?.let { preferences[PROFILE_ID] = it } ?: preferences.remove(PROFILE_ID)
            session.alias?.let { preferences[ALIAS] = it } ?: preferences.remove(ALIAS)
            session.avatarSeed?.let { preferences[AVATAR_SEED] = it } ?: preferences.remove(AVATAR_SEED)
            session.communityId?.let { preferences[COMMUNITY_ID] = it } ?: preferences.remove(COMMUNITY_ID)
        }
    }

    private companion object {
        val PROFILE_ID = stringPreferencesKey("anonymous_profile_id")
        val ALIAS = stringPreferencesKey("anonymous_alias")
        val AVATAR_SEED = stringPreferencesKey("anonymous_avatar_seed")
        val COMMUNITY_ID = stringPreferencesKey("selected_community_id")
    }
}
