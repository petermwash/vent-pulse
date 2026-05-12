package com.nyoike.ventpulse.feature.matching.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyoike.ventpulse.feature.coreflow.domain.Community
import com.nyoike.ventpulse.feature.coreflow.domain.CoreFlowRepository
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentity
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentityRepository
import com.nyoike.ventpulse.feature.matching.domain.AnonymousMatch
import com.nyoike.ventpulse.feature.matching.domain.ChatMessage
import com.nyoike.ventpulse.feature.matching.domain.MatchStatus
import com.nyoike.ventpulse.feature.matching.domain.MatchingRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MatchingViewModel(
    private val matchingRepository: MatchingRepository,
    private val coreFlowRepository: CoreFlowRepository,
    private val identityRepository: AnonymousIdentityRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MatchingState())
    val state = _state.asStateFlow()

    private val _events = Channel<MatchingEvent>()
    val events = _events.receiveAsFlow()

    private var identity: AnonymousIdentity? = null
    private var loaded = false
    private var matchJob: Job? = null
    private var messagesJob: Job? = null

    fun onAction(action: MatchingAction) {
        when (action) {
            MatchingAction.Load -> load()
            MatchingAction.StartMatching -> startMatching()
            MatchingAction.SkipToChatDemo -> skipToDemoChat()
            is MatchingAction.ChangeDraft -> _state.update { it.copy(draft = action.value) }
            MatchingAction.SendMessage -> sendMessage()
            MatchingAction.EndConversation -> endConversation()
            is MatchingAction.ReportMessage -> reportMessage(action.messageId)
            MatchingAction.RequestExpert -> requestExpert()
            MatchingAction.DismissMessage -> _state.update { it.copy(message = null) }
        }
    }

    private fun load() {
        if (loaded) return
        loaded = true
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            identity = identityRepository.getOrCreateIdentity()
            coreFlowRepository.syncCommunities()
            val communities = coreFlowRepository.observeCommunities().first()
            val selected = resolveSelectedCommunity(communities)
            _state.update {
                it.copy(
                    isLoading = false,
                    selectedCommunity = selected,
                    message = if (selected == null) "Choose a community first." else null
                )
            }
            if (selected != null) startMatching()
        }
    }

    private fun startMatching() {
        viewModelScope.launch {
            val currentIdentity = identity ?: identityRepository.getOrCreateIdentity().also { identity = it }
            val community = _state.value.selectedCommunity ?: return@launch
            _state.update {
                it.copy(
                    isLoading = true,
                    phase = MatchingPhase.MATCHING,
                    match = null,
                    messages = emptyList(),
                    message = null
                )
            }
            val match = matchingRepository.startOrJoinMatch(
                profileId = currentIdentity.profileId,
                communityId = community.id
            )
            activateMatch(match)
        }
    }

    private fun skipToDemoChat() {
        viewModelScope.launch {
            val currentIdentity = identity ?: identityRepository.getOrCreateIdentity().also { identity = it }
            val community = _state.value.selectedCommunity ?: return@launch
            activateMatch(
                matchingRepository.createDemoMatch(
                    profileId = currentIdentity.profileId,
                    communityId = community.id
                )
            )
        }
    }

    private fun activateMatch(match: AnonymousMatch) {
        _state.update {
            it.copy(
                isLoading = false,
                phase = if (match.status == MatchStatus.ACTIVE) MatchingPhase.CHAT else MatchingPhase.MATCHING,
                match = match,
                message = if (match.status == MatchStatus.WAITING) {
                    "Waiting for a safe anonymous match. You can skip to the chat demo."
                } else {
                    "Connected anonymously with ${match.partnerAlias}."
                }
            )
        }
        observeMatch(match)
        if (match.status == MatchStatus.ACTIVE) observeMessages(match)
    }

    private fun observeMatch(match: AnonymousMatch) {
        matchJob?.cancel()
        matchJob = viewModelScope.launch {
            matchingRepository.observeMatch(match.id, match).collect { updatedMatch ->
                _state.update {
                    it.copy(
                        match = updatedMatch,
                        phase = when (updatedMatch.status) {
                            MatchStatus.ACTIVE -> MatchingPhase.CHAT
                            MatchStatus.ENDED, MatchStatus.CANCELLED -> MatchingPhase.ENDED
                            MatchStatus.WAITING -> MatchingPhase.MATCHING
                        }
                    )
                }
                if (updatedMatch.status == MatchStatus.ACTIVE) observeMessages(updatedMatch)
            }
        }
    }

    private fun observeMessages(match: AnonymousMatch) {
        if (messagesJob?.isActive == true && _state.value.match?.id == match.id) return
        messagesJob?.cancel()
        val currentIdentity = identity ?: return
        messagesJob = viewModelScope.launch {
            matchingRepository.observeMessages(
                matchId = match.id,
                profileId = currentIdentity.profileId,
                fallback = match
            ).collect { messages ->
                _state.update { it.copy(messages = messages.visibleOnly()) }
            }
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            val currentIdentity = identity ?: identityRepository.getOrCreateIdentity().also { identity = it }
            val match = _state.value.match ?: return@launch
            val body = _state.value.draft.trim()
            if (body.isBlank()) return@launch
            _state.update { it.copy(draft = "") }
            val sentMessage = matchingRepository.sendMessage(match, currentIdentity.profileId, body)
            _state.update { state ->
                state.copy(messages = (state.messages + sentMessage).distinctBy { it.id }.visibleOnly())
            }
        }
    }

    private fun endConversation() {
        viewModelScope.launch {
            val currentIdentity = identity ?: identityRepository.getOrCreateIdentity().also { identity = it }
            val match = _state.value.match ?: return@launch
            matchingRepository.endMatch(match, currentIdentity.profileId)
            messagesJob?.cancel()
            _state.update {
                it.copy(
                    phase = MatchingPhase.ENDED,
                    message = "Conversation ended. You can start a new anonymous match whenever you are ready."
                )
            }
        }
    }

    private fun reportMessage(messageId: String) {
        viewModelScope.launch {
            val currentIdentity = identity ?: identityRepository.getOrCreateIdentity().also { identity = it }
            val message = _state.value.messages.firstOrNull { it.id == messageId } ?: return@launch
            matchingRepository.reportMessage(currentIdentity.profileId, message)
            _state.update { it.copy(message = "Report sent for moderator review.") }
        }
    }

    private fun requestExpert() {
        viewModelScope.launch {
            _events.send(MatchingEvent.NavigateToExpertSupport)
        }
    }

    private fun resolveSelectedCommunity(communities: List<Community>): Community? {
        val selectedCommunityId = identity?.communityId
        return communities.firstOrNull { it.id == selectedCommunityId }
            ?: communities.firstOrNull { it.name == "Brookside" }
            ?: communities.firstOrNull()
    }

    private fun List<ChatMessage>.visibleOnly(): List<ChatMessage> {
        return filter { it.moderationStatus == "visible" }
    }
}
