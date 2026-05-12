package com.nyoike.ventpulse.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nyoike.ventpulse.feature.community.presentation.CommunityFeedRoot
import com.nyoike.ventpulse.feature.community.presentation.CommunityRoot
import com.nyoike.ventpulse.feature.connect.presentation.ConnectRoot
import com.nyoike.ventpulse.feature.onboarding.presentation.OnboardingRoot
import com.nyoike.ventpulse.feature.profile.presentation.ProfileRoot
import com.nyoike.ventpulse.feature.pulse.presentation.PulseRoot
import com.nyoike.ventpulse.feature.safespace.presentation.SafeSpaceRoot
import com.nyoike.ventpulse.feature.safespace.presentation.VentWritingRoot
import com.nyoike.ventpulse.feature.identity.presentation.SplashRoot

@Composable
fun VentPulseApp() {
    val navController = rememberNavController()
    val onNavigate: (String) -> Unit = { destination ->
        when (destination) {
            "Pulse" -> navController.navigate(PulseRoute) { launchSingleTop = true }
            "Community" -> navController.navigate(CommunityRoute) { launchSingleTop = true }
            "Connect" -> navController.navigate(ConnectRoute) { launchSingleTop = true }
            "Safe Space" -> navController.navigate(SafeSpaceRoute) { launchSingleTop = true }
            "Profile" -> navController.navigate(ProfileRoute) { launchSingleTop = true }
        }
    }

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SplashRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<SplashRoute> {
                SplashRoot(
                    onIdentityReady = {
                        navController.navigate(OnboardingRoute) {
                            popUpTo<SplashRoute> { inclusive = true }
                        }
                    }
                )
            }
            composable<OnboardingRoute> {
                OnboardingRoot(
                    onFinish = {
                        navController.navigate(CommunitySelectionRoute) {
                            popUpTo<OnboardingRoute> { inclusive = true }
                        }
                    }
                )
            }
            composable<PulseRoute> {
                PulseRoot(
                    onNavigateToVent = { moodId, communityId ->
                        navController.navigate(
                            VentWritingRoute(
                                moodId = moodId,
                                communityId = communityId
                            )
                        )
                    },
                    onNavigate = onNavigate
                )
            }
            composable<CommunitySelectionRoute> {
                CommunityRoot(
                    onCommunitySelected = {
                        navController.navigate(PulseRoute) {
                            popUpTo<CommunitySelectionRoute> { inclusive = true }
                        }
                    }
                )
            }
            composable<CommunityRoute> {
                CommunityFeedRoot(onNavigate = onNavigate)
            }
            composable<ConnectRoute> {
                ConnectRoot(onNavigate = onNavigate)
            }
            composable<SafeSpaceRoute> {
                SafeSpaceRoot(onNavigate = onNavigate)
            }
            composable<VentWritingRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<VentWritingRoute>()
                VentWritingRoot(
                    moodId = route.moodId,
                    communityId = route.communityId,
                    onSaved = {
                        navController.navigate(CommunityRoute) {
                            popUpTo<VentWritingRoute> { inclusive = true }
                        }
                    },
                    onNavigate = onNavigate
                )
            }
            composable<ProfileRoute> {
                ProfileRoot(onNavigate = onNavigate)
            }
        }
    }
}
