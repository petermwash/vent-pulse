package com.nyoike.ventpulse.feature.identity.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyoike.ventpulse.core.data.session.SessionPreferences
import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentityRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IdentityBootstrapViewModel(
    private val identityRepository: AnonymousIdentityRepository,
    private val sessionPreferences: SessionPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(IdentityBootstrapState())
    val state = _state.asStateFlow()

    private val _events = Channel<IdentityBootstrapEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: IdentityBootstrapAction) {
        when (action) {
            IdentityBootstrapAction.Start -> bootstrap()
        }
    }

    private fun bootstrap() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val identity = identityRepository.getOrCreateIdentity()
            _state.update {
                it.copy(
                    isLoading = false,
                    alias = identity.alias,
                    avatarSeed = identity.avatarSeed
                )
            }
            val hasSeenOnboarding = sessionPreferences.hasSeenOnboarding.first()
            val destination = when {
                !hasSeenOnboarding -> LaunchDestination.ONBOARDING
                identity.communityId.isNullOrBlank() -> LaunchDestination.COMMUNITY_SELECTION
                else -> LaunchDestination.PULSE
            }
            _events.send(IdentityBootstrapEvent.IdentityReady(destination))
        }
    }
}
