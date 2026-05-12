package com.nyoike.ventpulse.feature.matching.presentation

import com.nyoike.ventpulse.MainDispatcherRule
import com.nyoike.ventpulse.feature.coreflow.domain.Community
import com.nyoike.ventpulse.feature.coreflow.domain.CoreFlowRepository
import com.nyoike.ventpulse.feature.coreflow.domain.Mood
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentity
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentityRepository
import com.nyoike.ventpulse.feature.matching.domain.AnonymousMatch
import com.nyoike.ventpulse.feature.matching.domain.ChatMessage
import com.nyoike.ventpulse.feature.matching.domain.MatchStatus
import com.nyoike.ventpulse.feature.matching.domain.MatchingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MatchingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadStartsMatchingAgainstSelectedCommunity() = runTest {
        val repository = FakeMatchingRepository(startMatch = waitingMatch)
        val viewModel = MatchingViewModel(
            matchingRepository = repository,
            coreFlowRepository = FakeCoreFlowRepository(),
            identityRepository = FakeAnonymousIdentityRepository()
        )

        viewModel.onAction(MatchingAction.Load)

        assertEquals(MatchingPhase.MATCHING, viewModel.state.value.phase)
        assertEquals(waitingMatch, viewModel.state.value.match)
        assertEquals(testCommunity.id, repository.startedCommunityId)
    }

    @Test
    fun activeMatchLoadsChatMessages() = runTest {
        val repository = FakeMatchingRepository(startMatch = activeMatch)
        val viewModel = MatchingViewModel(
            matchingRepository = repository,
            coreFlowRepository = FakeCoreFlowRepository(),
            identityRepository = FakeAnonymousIdentityRepository()
        )

        viewModel.onAction(MatchingAction.Load)

        assertEquals(MatchingPhase.CHAT, viewModel.state.value.phase)
        assertEquals(1, viewModel.state.value.messages.size)
    }

    @Test
    fun sendMessageAppendsMessageAndClearsDraft() = runTest {
        val repository = FakeMatchingRepository(startMatch = activeMatch)
        val viewModel = MatchingViewModel(
            matchingRepository = repository,
            coreFlowRepository = FakeCoreFlowRepository(),
            identityRepository = FakeAnonymousIdentityRepository()
        )

        viewModel.onAction(MatchingAction.Load)
        viewModel.onAction(MatchingAction.ChangeDraft("I need a quiet moment."))
        viewModel.onAction(MatchingAction.SendMessage)

        assertEquals("", viewModel.state.value.draft)
        assertTrue(viewModel.state.value.messages.any { it.body == "I need a quiet moment." })
        assertEquals("I need a quiet moment.", repository.sentBody)
    }

    @Test
    fun reportAndEndConversationUpdateSafetyState() = runTest {
        val repository = FakeMatchingRepository(startMatch = activeMatch)
        val viewModel = MatchingViewModel(
            matchingRepository = repository,
            coreFlowRepository = FakeCoreFlowRepository(),
            identityRepository = FakeAnonymousIdentityRepository()
        )

        viewModel.onAction(MatchingAction.Load)
        viewModel.onAction(MatchingAction.ReportMessage("message-id"))
        viewModel.onAction(MatchingAction.EndConversation)

        assertEquals("message-id", repository.reportedMessageId)
        assertEquals(MatchingPhase.ENDED, viewModel.state.value.phase)
        assertTrue(repository.ended)
    }
}

private class FakeMatchingRepository(
    private val startMatch: AnonymousMatch
) : MatchingRepository {
    var startedCommunityId: String? = null
        private set
    var sentBody: String? = null
        private set
    var reportedMessageId: String? = null
        private set
    var ended = false
        private set

    private val matchFlow = MutableStateFlow(startMatch)
    private val messagesFlow = MutableStateFlow(listOf(remoteMessage))

    override suspend fun startOrJoinMatch(profileId: String, communityId: String): AnonymousMatch {
        startedCommunityId = communityId
        return startMatch
    }

    override suspend fun createDemoMatch(profileId: String, communityId: String): AnonymousMatch {
        return activeMatch.copy(id = "demo-match", isDemo = true)
    }

    override fun observeMatch(matchId: String, fallback: AnonymousMatch): Flow<AnonymousMatch> = matchFlow

    override fun observeMessages(
        matchId: String,
        profileId: String,
        fallback: AnonymousMatch
    ): Flow<List<ChatMessage>> = messagesFlow

    override suspend fun sendMessage(match: AnonymousMatch, profileId: String, body: String): ChatMessage {
        sentBody = body
        return localMessage.copy(body = body)
    }

    override suspend fun endMatch(match: AnonymousMatch, profileId: String) {
        ended = true
    }

    override suspend fun reportMessage(profileId: String, message: ChatMessage) {
        reportedMessageId = message.id
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

private val waitingMatch = AnonymousMatch(
    id = "match-id",
    communityId = testCommunity.id,
    initiatorProfileId = "profile-id",
    partnerProfileId = null,
    status = MatchStatus.WAITING,
    partnerAlias = "Someone nearby",
    partnerAvatarSeed = "nearby",
    currentProfileId = "profile-id"
)

private val activeMatch = waitingMatch.copy(
    partnerProfileId = "partner-id",
    status = MatchStatus.ACTIVE,
    partnerAlias = "Gentle Orbit",
    partnerAvatarSeed = "gentle-orbit"
)

private val remoteMessage = ChatMessage(
    id = "message-id",
    matchId = activeMatch.id,
    profileId = "partner-id",
    alias = "Gentle Orbit",
    avatarSeed = "gentle-orbit",
    body = "I am listening.",
    createdAt = "now",
    isMine = false
)

private val localMessage = ChatMessage(
    id = "local-id",
    matchId = activeMatch.id,
    profileId = "profile-id",
    alias = "You",
    avatarSeed = "quiet-cloud",
    body = "Draft",
    createdAt = "now",
    isMine = true
)
