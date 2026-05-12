package com.nyoike.ventpulse.feature.identity.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyoike.ventpulse.core.presentation.ObserveAsEvents
import com.nyoike.ventpulse.ui.theme.BrandPurple
import com.nyoike.ventpulse.ui.theme.MainBackground
import com.nyoike.ventpulse.ui.theme.MintCalm
import com.nyoike.ventpulse.ui.theme.VentPulseTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashRoot(
    onIdentityReady: () -> Unit,
    viewModel: IdentityBootstrapViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(IdentityBootstrapAction.Start)
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            IdentityBootstrapEvent.IdentityReady -> onIdentityReady()
        }
    }

    SplashScreen(state = state)
}

@Composable
fun SplashScreen(
    state: IdentityBootstrapState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MainBackground)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(MintCalm.copy(alpha = 0.72f))
                    .padding(44.dp),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = BrandPurple)
                }
            }
            Text(
                text = "VentPulse",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                text = state.alias?.let { "Welcome, $it" } ?: "Preparing your safe space...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    VentPulseTheme {
        SplashScreen(
            state = IdentityBootstrapState(
                isLoading = false,
                alias = "Quiet Cloud",
                avatarSeed = "quiet-cloud"
            )
        )
    }
}
