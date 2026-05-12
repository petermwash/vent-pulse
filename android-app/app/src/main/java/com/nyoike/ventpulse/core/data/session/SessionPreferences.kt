package com.nyoike.ventpulse.core.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate

data class AnonymousSession(
    val profileId: String?,
    val alias: String?,
    val avatarSeed: String?,
    val communityId: String?
)

open class SessionPreferences(
    private val dataStore: DataStore<Preferences>
) {
    open val hasSeenOnboarding: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[HAS_SEEN_ONBOARDING] ?: false
    }

    open val lastMoodCheckInDate: Flow<String?> = dataStore.data.map { preferences ->
        preferences[LAST_MOOD_CHECK_IN_DATE]
    }

    open val anonymousSession: Flow<AnonymousSession> = dataStore.data.map { preferences ->
        AnonymousSession(
            profileId = preferences[PROFILE_ID],
            alias = preferences[ALIAS],
            avatarSeed = preferences[AVATAR_SEED],
            communityId = preferences[COMMUNITY_ID]
        )
    }

    open suspend fun saveAnonymousSession(session: AnonymousSession) {
        dataStore.edit { preferences ->
            session.profileId?.let { preferences[PROFILE_ID] = it } ?: preferences.remove(PROFILE_ID)
            session.alias?.let { preferences[ALIAS] = it } ?: preferences.remove(ALIAS)
            session.avatarSeed?.let { preferences[AVATAR_SEED] = it } ?: preferences.remove(AVATAR_SEED)
            session.communityId?.let { preferences[COMMUNITY_ID] = it } ?: preferences.remove(COMMUNITY_ID)
        }
    }

    open suspend fun markOnboardingSeen() {
        dataStore.edit { preferences ->
            preferences[HAS_SEEN_ONBOARDING] = true
        }
    }

    open suspend fun saveMoodCheckInForToday() {
        val today = LocalDate.now().toString()
        dataStore.edit { preferences ->
            preferences[LAST_MOOD_CHECK_IN_DATE] = today
        }
    }

    open suspend fun hasCheckedInToday(): Boolean {
        return lastMoodCheckInDate.map { it == LocalDate.now().toString() }.first()
    }

    private companion object {
        val PROFILE_ID = stringPreferencesKey("anonymous_profile_id")
        val ALIAS = stringPreferencesKey("anonymous_alias")
        val AVATAR_SEED = stringPreferencesKey("anonymous_avatar_seed")
        val COMMUNITY_ID = stringPreferencesKey("selected_community_id")
        val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
        val LAST_MOOD_CHECK_IN_DATE = stringPreferencesKey("last_mood_check_in_date")
    }
}
