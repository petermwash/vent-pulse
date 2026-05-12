package com.nyoike.ventpulse.feature.identity.presentation

data class IdentityBootstrapState(
    val isLoading: Boolean = true,
    val alias: String? = null,
    val avatarSeed: String? = null
)

sealed interface IdentityBootstrapAction {
    data object Start : IdentityBootstrapAction
}

enum class LaunchDestination {
    ONBOARDING,
    COMMUNITY_SELECTION,
    PULSE
}

sealed interface IdentityBootstrapEvent {
    data class IdentityReady(val destination: LaunchDestination) : IdentityBootstrapEvent
}
