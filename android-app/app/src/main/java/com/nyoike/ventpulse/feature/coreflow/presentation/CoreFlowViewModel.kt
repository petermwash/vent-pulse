package com.nyoike.ventpulse.feature.coreflow.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyoike.ventpulse.core.data.session.SessionPreferences
import com.nyoike.ventpulse.feature.coreflow.domain.Community
import com.nyoike.ventpulse.feature.coreflow.domain.CoreFlowRepository
import com.nyoike.ventpulse.feature.coreflow.domain.Mood
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentity
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentityRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoreFlowViewModel(
    private val coreFlowRepository: CoreFlowRepository,
    private val identityRepository: AnonymousIdentityRepository,
    private val sessionPreferences: SessionPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(CoreFlowState())
    val state = _state.asStateFlow()

    private val _events = Channel<CoreFlowEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var identity: AnonymousIdentity? = null
    private var loadJob: Job? = null
    private var selectedRouteCommunityId: String? = null

    fun onAction(action: CoreFlowAction) {
        when (action) {
            CoreFlowAction.Load -> load()
            is CoreFlowAction.PrepareVent -> prepareVent(action.moodId, action.communityId)
            is CoreFlowAction.SelectCommunity -> selectCommunity(action.community)
            is CoreFlowAction.SelectMood -> _state.update { it.copy(selectedMood = action.mood) }
            CoreFlowAction.SaveMood -> saveMood()
            is CoreFlowAction.ChangeVentText -> _state.update { it.copy(ventText = action.value) }
            CoreFlowAction.SaveVent -> saveVent()
            CoreFlowAction.DismissMessage -> _state.update { it.copy(message = null) }
        }
    }

    private fun load() {
        if (loadJob != null) return
        viewModelScope.launch {
            identity = identityRepository.getOrCreateIdentity()
            coreFlowRepository.syncCommunities()
            _state.update { it.copy(hasCheckedInToday = sessionPreferences.hasCheckedInToday()) }
        }
        loadJob = viewModelScope.launch {
            coreFlowRepository.observeCommunities().collect { communities ->
                val selected = resolveSelectedCommunity(communities)
                _state.update {
                    it.copy(
                        communities = communities,
                        selectedCommunity = selected,
                        hasCheckedInToday = it.hasCheckedInToday
                    )
                }
            }
        }
    }

    private fun prepareVent(moodId: String, communityId: String) {
        selectedRouteCommunityId = communityId
        _state.update { state ->
            state.copy(
                selectedMood = Mood.fromId(moodId),
                selectedCommunity = state.communities.firstOrNull { it.id == communityId }
                    ?: state.selectedCommunity
            )
        }
    }

    private fun selectCommunity(community: Community) {
        viewModelScope.launch {
            identityRepository.updateCommunity(community.id)
            identity = identityRepository.getOrCreateIdentity()
            _state.update { it.copy(selectedCommunity = community) }
        }
    }

    private fun saveMood() {
        viewModelScope.launch {
            val currentIdentity = identity ?: identityRepository.getOrCreateIdentity().also { identity = it }
            val community = _state.value.selectedCommunity ?: return@launch
            _state.update { it.copy(isSaving = true) }
            runCatching {
                coreFlowRepository.saveMoodCheckIn(
                    profileId = currentIdentity.profileId,
                    communityId = community.id,
                    mood = _state.value.selectedMood,
                    intensity = _state.value.intensity
                )
                sessionPreferences.saveMoodCheckInForToday()
            }
            _state.update { it.copy(isSaving = false, hasCheckedInToday = true) }
            _events.send(
                CoreFlowEvent.NavigateToVentWriting(
                    moodId = _state.value.selectedMood.id,
                    communityId = community.id
                )
            )
        }
    }

    private fun saveVent() {
        viewModelScope.launch {
            val currentIdentity = identity ?: identityRepository.getOrCreateIdentity().also { identity = it }
            val community = _state.value.selectedCommunity ?: return@launch
            val body = _state.value.ventText.trim()
            _state.update { it.copy(isSaving = true) }
            if (body.isNotBlank()) {
                runCatching {
                    coreFlowRepository.saveVent(
                        profileId = currentIdentity.profileId,
                        communityId = community.id,
                        mood = _state.value.selectedMood,
                        body = body
                    )
                }
            }
            _state.update {
                it.copy(
                    isSaving = false,
                    ventText = "",
                    message = "Your pulse was saved anonymously."
                )
            }
            _events.send(CoreFlowEvent.NavigateToPulse)
        }
    }

    private fun resolveSelectedCommunity(communities: List<Community>): Community? {
        val selectedCommunityId = identity?.communityId
        return communities.firstOrNull { it.id == selectedRouteCommunityId }
            ?: communities.firstOrNull { it.id == selectedCommunityId }
            ?: communities.firstOrNull { it.name == "Brookside" }
            ?: communities.firstOrNull()
    }
}
