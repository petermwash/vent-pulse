package com.nyoike.ventpulse.feature.identity.presentation

data class IdentityBootstrapState(
    val isLoading: Boolean = true,
    val alias: String? = null,
    val avatarSeed: String? = null
)

sealed interface IdentityBootstrapAction {
    data object Start : IdentityBootstrapAction
}

sealed interface IdentityBootstrapEvent {
    data object IdentityReady : IdentityBootstrapEvent
}
