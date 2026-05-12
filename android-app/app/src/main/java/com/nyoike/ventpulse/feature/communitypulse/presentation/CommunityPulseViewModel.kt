package com.nyoike.ventpulse.feature.communitypulse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyoike.ventpulse.feature.communitypulse.domain.CommunityPulseRepository
import com.nyoike.ventpulse.feature.coreflow.domain.Community
import com.nyoike.ventpulse.feature.coreflow.domain.CoreFlowRepository
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentity
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityPulseViewModel(
    private val communityPulseRepository: CommunityPulseRepository,
    private val coreFlowRepository: CoreFlowRepository,
    private val identityRepository: AnonymousIdentityRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CommunityPulseState())
    val state = _state.asStateFlow()

    private var identity: AnonymousIdentity? = null
    private var loaded = false

    fun onAction(action: CommunityPulseAction) {
        when (action) {
            CommunityPulseAction.Load -> load()
            is CommunityPulseAction.ReactToVent -> reactToVent(action.ventId)
            is CommunityPulseAction.ReportVent -> reportVent(action.ventId)
            CommunityPulseAction.DismissMessage -> _state.update { it.copy(message = null) }
        }
    }

    private fun load(force: Boolean = false) {
        if (loaded && !force) return
        loaded = true
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            identity = identityRepository.getOrCreateIdentity()
            coreFlowRepository.syncCommunities()
            val communities = coreFlowRepository.observeCommunities().first()
            val selectedCommunity = resolveSelectedCommunity(communities)
            if (selectedCommunity == null) {
                _state.update { it.copy(isLoading = false, message = "Choose a community first.") }
                return@launch
            }
            refreshCommunityData(selectedCommunity)
        }
    }

    private suspend fun refreshCommunityData(community: Community) {
        val currentIdentity = identity ?: identityRepository.getOrCreateIdentity().also { identity = it }
        val feed = communityPulseRepository.loadFeed(
            communityId = community.id,
            profileId = currentIdentity.profileId
        )
        val moodShares = communityPulseRepository.loadMoodSummary(community.id)
        val experts = communityPulseRepository.loadExperts(community.id)
        _state.update {
            it.copy(
                selectedCommunity = community,
                feed = feed,
                moodShares = moodShares,
                experts = experts,
                isLoading = false
            )
        }
    }

    private fun reactToVent(ventId: String) {
        viewModelScope.launch {
            val community = _state.value.selectedCommunity ?: return@launch
            val currentIdentity = identity ?: identityRepository.getOrCreateIdentity().also { identity = it }
            _state.update { state ->
                state.copy(
                    feed = state.feed.map { vent ->
                        if (vent.id == ventId && !vent.hasReacted) {
                            vent.copy(hasReacted = true, reactionCount = vent.reactionCount + 1)
                        } else {
                            vent
                        }
                    },
                    message = "You sent support anonymously."
                )
            }
            runCatching {
                communityPulseRepository.reactToVent(
                    profileId = currentIdentity.profileId,
                    communityId = community.id,
                    ventId = ventId
                )
            }
        }
    }

    private fun reportVent(ventId: String) {
        viewModelScope.launch {
            val community = _state.value.selectedCommunity ?: return@launch
            val currentIdentity = identity ?: identityRepository.getOrCreateIdentity().also { identity = it }
            _state.update { it.copy(message = "Report sent for moderator review.") }
            runCatching {
                communityPulseRepository.reportVent(
                    profileId = currentIdentity.profileId,
                    communityId = community.id,
                    ventId = ventId
                )
            }
        }
    }

    private fun resolveSelectedCommunity(communities: List<Community>): Community? {
        val selectedCommunityId = identity?.communityId
        return communities.firstOrNull { it.id == selectedCommunityId }
            ?: communities.firstOrNull { it.name == "Brookside" }
            ?: communities.firstOrNull()
    }
}
