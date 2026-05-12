package com.nyoike.ventpulse.feature.communitypulse.presentation

import com.nyoike.ventpulse.MainDispatcherRule
import com.nyoike.ventpulse.feature.communitypulse.domain.CommunityPulseRepository
import com.nyoike.ventpulse.feature.communitypulse.domain.CommunityVent
import com.nyoike.ventpulse.feature.communitypulse.domain.ExpertSupport
import com.nyoike.ventpulse.feature.communitypulse.domain.MoodShare
import com.nyoike.ventpulse.feature.coreflow.domain.Community
import com.nyoike.ventpulse.feature.coreflow.domain.CoreFlowRepository
import com.nyoike.ventpulse.feature.coreflow.domain.Mood
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentity
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CommunityPulseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadUsesSelectedCommunityAndFetchesPhaseFourData() = runTest {
        val repository = FakeCommunityPulseRepository()
        val viewModel = CommunityPulseViewModel(
            communityPulseRepository = repository,
            coreFlowRepository = FakeCoreFlowRepository(),
            identityRepository = FakeAnonymousIdentityRepository()
        )

        viewModel.onAction(CommunityPulseAction.Load)

        assertEquals(testCommunity, viewModel.state.value.selectedCommunity)
        assertEquals(1, viewModel.state.value.feed.size)
        assertEquals(1, viewModel.state.value.moodShares.size)
        assertEquals(1, viewModel.state.value.experts.size)
        assertEquals(testCommunity.id, repository.loadedCommunityId)
    }

    @Test
    fun reactToVentOptimisticallyMarksVentAsHeard() = runTest {
        val repository = FakeCommunityPulseRepository()
        val viewModel = CommunityPulseViewModel(
            communityPulseRepository = repository,
            coreFlowRepository = FakeCoreFlowRepository(),
            identityRepository = FakeAnonymousIdentityRepository()
        )

        viewModel.onAction(CommunityPulseAction.Load)
        viewModel.onAction(CommunityPulseAction.ReactToVent("vent-id"))

        val vent = viewModel.state.value.feed.first()
        assertTrue(vent.hasReacted)
        assertEquals(4, vent.reactionCount)
        assertEquals("vent-id", repository.reactedVentId)
    }

    @Test
    fun reportVentCallsRepositoryAndShowsMessage() = runTest {
        val repository = FakeCommunityPulseRepository()
        val viewModel = CommunityPulseViewModel(
            communityPulseRepository = repository,
            coreFlowRepository = FakeCoreFlowRepository(),
            identityRepository = FakeAnonymousIdentityRepository()
        )

        viewModel.onAction(CommunityPulseAction.Load)
        viewModel.onAction(CommunityPulseAction.ReportVent("vent-id"))

        assertEquals("vent-id", repository.reportedVentId)
        assertEquals("Report sent for moderator review.", viewModel.state.value.message)
    }
}

private class FakeCommunityPulseRepository : CommunityPulseRepository {
    var loadedCommunityId: String? = null
        private set
    var reactedVentId: String? = null
        private set
    var reportedVentId: String? = null
        private set

    override suspend fun loadFeed(communityId: String, profileId: String): List<CommunityVent> {
        loadedCommunityId = communityId
        return listOf(testVent)
    }

    override suspend fun loadMoodSummary(communityId: String): List<MoodShare> {
        return listOf(MoodShare(Mood.CALM, percentage = 100, count = 1))
    }

    override suspend fun loadExperts(communityId: String): List<ExpertSupport> {
        return listOf(
            ExpertSupport(
                id = "expert-id",
                displayName = "Dr. Sarah Chen",
                role = "Licensed Therapist",
                specialty = "Anxiety & Stress",
                avatarSeed = "sarah",
                availabilityStatus = "available",
                bio = null
            )
        )
    }

    override suspend fun reactToVent(profileId: String, communityId: String, ventId: String) {
        reactedVentId = ventId
    }

    override suspend fun reportVent(profileId: String, communityId: String, ventId: String) {
        reportedVentId = ventId
    }
}

private class FakeCoreFlowRepository : CoreFlowRepository {
    private val communities = MutableStateFlow(listOf(testCommunity))

    override fun observeCommunities(): Flow<List<Community>> = communities
    override suspend fun syncCommunities() = Unit
    override suspend fun saveMoodCheckIn(profileId: String, communityId: String, mood: Mood, intensity: Int) = Unit
    override suspend fun saveVent(profileId: String, communityId: String, mood: Mood, body: String) = Unit
}

private class FakeAnonymousIdentityRepository : AnonymousIdentityRepository {
    private val identity = AnonymousIdentity(
        profileId = "profile-id",
        alias = "Quiet Cloud",
        avatarSeed = "quiet-cloud",
        communityId = testCommunity.id
    )

    override fun observeIdentity(): Flow<AnonymousIdentity?> = MutableStateFlow(identity)
    override suspend fun getOrCreateIdentity(): AnonymousIdentity = identity
    override suspend fun updateCommunity(communityId: String) = Unit
}

private val testCommunity = Community(
    id = "community-id",
    parentId = "parent-id",
    name = "Brookside",
    path = "kenya/nairobi/westlands/brookside",
    level = "neighborhood",
    sortOrder = 1
)

private val testVent = CommunityVent(
    id = "vent-id",
    profileId = "author-id",
    alias = "Quiet Cloud",
    avatarSeed = "quiet-cloud",
    mood = Mood.CALM,
    body = "Small victories today.",
    createdAt = "5m ago",
    reactionCount = 3,
    commentCount = 3,
    hasReacted = false
)
