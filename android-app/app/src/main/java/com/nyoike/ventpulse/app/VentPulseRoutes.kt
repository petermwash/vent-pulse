package com.nyoike.ventpulse.app

import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

@Serializable
data object OnboardingRoute

@Serializable
data object PulseRoute

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
