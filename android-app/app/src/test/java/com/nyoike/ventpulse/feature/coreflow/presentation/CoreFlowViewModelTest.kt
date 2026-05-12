package com.nyoike.ventpulse.feature.coreflow.presentation

import app.cash.turbine.test
import com.nyoike.ventpulse.MainDispatcherRule
import com.nyoike.ventpulse.feature.coreflow.domain.Community
import com.nyoike.ventpulse.feature.coreflow.domain.CoreFlowRepository
import com.nyoike.ventpulse.feature.coreflow.domain.Mood
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentity
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CoreFlowViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun saveMoodPersistsCheckInAndEmitsVentRoute() = runTest {
        val repository = FakeCoreFlowRepository()
        val viewModel = CoreFlowViewModel(repository, FakeAnonymousIdentityRepository())

        viewModel.events.test {
            viewModel.onAction(CoreFlowAction.Load)
            viewModel.onAction(CoreFlowAction.SelectCommunity(testCommunity))
            viewModel.onAction(CoreFlowAction.SelectMood(Mood.LONELY))
            viewModel.onAction(CoreFlowAction.SaveMood)

            assertEquals(
                CoreFlowEvent.NavigateToVentWriting(
                    moodId = Mood.LONELY.id,
                    communityId = testCommunity.id
                ),
                awaitItem()
            )
            assertEquals(Mood.LONELY, repository.savedMood)
            assertEquals(testCommunity.id, repository.savedMoodCommunityId)
        }
    }

    @Test
    fun saveVentPersistsBodyAndReturnsToPulse() = runTest {
        val repository = FakeCoreFlowRepository()
        val viewModel = CoreFlowViewModel(repository, FakeAnonymousIdentityRepository())

        viewModel.events.test {
            viewModel.onAction(CoreFlowAction.Load)
            viewModel.onAction(CoreFlowAction.PrepareVent(Mood.SAD.id, testCommunity.id))
            viewModel.onAction(CoreFlowAction.ChangeVentText("I feel unseen today."))
            viewModel.onAction(CoreFlowAction.SaveVent)

            assertEquals(CoreFlowEvent.NavigateToPulse, awaitItem())
            assertEquals(Mood.SAD, repository.savedVentMood)
            assertEquals("I feel unseen today.", repository.savedVentBody)
            assertEquals("", viewModel.state.value.ventText)
        }
    }
}

private class FakeCoreFlowRepository : CoreFlowRepository {
    private val communities = MutableStateFlow(listOf(testCommunity))

    var savedMood: Mood? = null
        private set
    var savedMoodCommunityId: String? = null
        private set
    var savedVentMood: Mood? = null
        private set
    var savedVentBody: String? = null
        private set

    override fun observeCommunities(): Flow<List<Community>> = communities

    override suspend fun syncCommunities() = Unit

    override suspend fun saveMoodCheckIn(
        profileId: String,
        communityId: String,
        mood: Mood,
        intensity: Int
    ) {
        savedMood = mood
        savedMoodCommunityId = communityId
    }

    override suspend fun saveVent(
        profileId: String,
        communityId: String,
        mood: Mood,
        body: String
    ) {
        savedVentMood = mood
        savedVentBody = body
    }
}

private class FakeAnonymousIdentityRepository : AnonymousIdentityRepository {
    private var identity = AnonymousIdentity(
        profileId = "profile-id",
        alias = "Quiet Cloud",
        avatarSeed = "quiet-cloud",
        communityId = testCommunity.id
    )
    private val identities = MutableStateFlow<AnonymousIdentity?>(identity)

    override fun observeIdentity(): Flow<AnonymousIdentity?> = identities

    override suspend fun getOrCreateIdentity(): AnonymousIdentity = identity

    override suspend fun updateCommunity(communityId: String) {
        identity = identity.copy(communityId = communityId)
        identities.value = identity
    }
}

private val testCommunity = Community(
    id = "community-id",
    parentId = "parent-id",
    name = "Brookside",
    path = "kenya/nairobi/westlands/brookside",
    level = "neighborhood",
    sortOrder = 1
)
