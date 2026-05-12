package com.nyoike.ventpulse.feature.onboarding.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nyoike.ventpulse.app.FoundationPlaceholder

@Composable
fun OnboardingRoot(
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FoundationPlaceholder(
            title = "A safe place to feel.",
            subtitle = "The screenshot-faithful onboarding journey lands in Phase 3.",
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onFinish,
            modifier = Modifier.padding(bottom = 28.dp)
        ) {
            Text(
                text = "Continue",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
