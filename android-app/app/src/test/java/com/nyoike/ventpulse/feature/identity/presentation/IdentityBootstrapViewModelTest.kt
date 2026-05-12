package com.nyoike.ventpulse.feature.identity.presentation

import app.cash.turbine.test
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.nyoike.ventpulse.core.data.session.AnonymousSession
import com.nyoike.ventpulse.core.data.session.SessionPreferences
import com.nyoike.ventpulse.MainDispatcherRule
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentity
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class IdentityBootstrapViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun startCreatesIdentityAndEmitsReadyEvent() = runTest {
        val repository = FakeAnonymousIdentityRepository()
        val viewModel = IdentityBootstrapViewModel(repository, testSessionPreferences())

        viewModel.events.test {
            viewModel.onAction(IdentityBootstrapAction.Start)

            assertEquals(
                IdentityBootstrapEvent.IdentityReady(LaunchDestination.ONBOARDING),
                awaitItem()
            )
            assertFalse(viewModel.state.value.isLoading)
            assertEquals("Quiet Cloud", viewModel.state.value.alias)
        }
    }
}

private fun testSessionPreferences(): SessionPreferences {
    return FakeSessionPreferences()
}

private class FakeAnonymousIdentityRepository : AnonymousIdentityRepository {
    private val identity = AnonymousIdentity(
        profileId = "profile-id",
        alias = "Quiet Cloud",
        avatarSeed = "quiet-cloud"
    )
    private val identities = MutableStateFlow<AnonymousIdentity?>(null)

    override fun observeIdentity(): Flow<AnonymousIdentity?> = identities

    override suspend fun getOrCreateIdentity(): AnonymousIdentity {
        identities.value = identity
        return identity
    }

    override suspend fun updateCommunity(communityId: String) {
        identities.value = identity.copy(communityId = communityId)
    }
}

private class FakeSessionPreferences : SessionPreferences(EmptyPreferencesDataStore) {
    override val hasSeenOnboarding: Flow<Boolean> = flowOf(false)
    override val lastMoodCheckInDate: Flow<String?> = flowOf(null)
    override val anonymousSession: Flow<AnonymousSession> = flowOf(
        AnonymousSession(
            profileId = "profile-id",
            alias = "Quiet Cloud",
            avatarSeed = "quiet-cloud",
            communityId = null
        )
    )
}

private object EmptyPreferencesDataStore : DataStore<Preferences> {
    override val data: Flow<Preferences> = flowOf(emptyPreferences())

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        return transform(emptyPreferences())
    }
}
