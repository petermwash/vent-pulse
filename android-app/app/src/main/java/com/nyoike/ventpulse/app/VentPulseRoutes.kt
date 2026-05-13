package com.nyoike.ventpulse.app

import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

@Serializable
data object OnboardingRoute

@Serializable
data object PulseRoute

@Serializable
data object CommunitySelectionRoute

@Serializable
data object CommunityRoute

@Serializable
data object ConnectRoute

@Serializable
data object SafeSpaceRoute

@Serializable
data class VentWritingRoute(
    val moodId: String,
    val communityId: String
)

@Serializable
data object ProfileRoute

@Serializable
data class ExpertCallRoute(
    val expertId: String,
    val displayName: String,
    val role: String
)

@Serializable
data class ExpertChatRoute(
    val expertId: String,
    val displayName: String,
    val role: String
)
