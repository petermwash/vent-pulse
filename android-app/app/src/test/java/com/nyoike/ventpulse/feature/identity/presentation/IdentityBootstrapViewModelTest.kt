package com.nyoike.ventpulse.feature.identity.presentation

import app.cash.turbine.test
import com.nyoike.ventpulse.MainDispatcherRule
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentity
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
        val viewModel = IdentityBootstrapViewModel(repository)

        viewModel.events.test {
            viewModel.onAction(IdentityBootstrapAction.Start)

            assertEquals(IdentityBootstrapEvent.IdentityReady, awaitItem())
            assertFalse(viewModel.state.value.isLoading)
            assertEquals("Quiet Cloud", viewModel.state.value.alias)
        }
    }
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
