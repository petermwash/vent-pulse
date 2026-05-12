package com.nyoike.ventpulse.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nyoike.ventpulse.feature.community.presentation.CommunityRoot
import com.nyoike.ventpulse.feature.connect.presentation.ConnectRoot
import com.nyoike.ventpulse.feature.onboarding.presentation.OnboardingRoot
import com.nyoike.ventpulse.feature.profile.presentation.ProfileRoot
import com.nyoike.ventpulse.feature.pulse.presentation.PulseRoot
import com.nyoike.ventpulse.feature.safespace.presentation.SafeSpaceRoot
import com.nyoike.ventpulse.feature.identity.presentation.SplashRoot

@Composable
fun VentPulseApp() {
    val navController = rememberNavController()

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
                        navController.navigate(PulseRoute) {
                            popUpTo<OnboardingRoute> { inclusive = true }
                        }
                    }
                )
            }
            composable<PulseRoute> {
                PulseRoot()
            }
            composable<CommunityRoute> {
                CommunityRoot()
            }
            composable<ConnectRoute> {
                ConnectRoot()
            }
            composable<SafeSpaceRoute> {
                SafeSpaceRoot()
            }
            composable<ProfileRoute> {
                ProfileRoot()
            }
        }
    }
}
