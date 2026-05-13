package com.nyoike.ventpulse.feature.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowBackground
import com.nyoike.ventpulse.feature.coreflow.presentation.PrimaryPulseButton
import com.nyoike.ventpulse.feature.coreflow.presentation.gentleBreathingFloat
import com.nyoike.ventpulse.ui.theme.BrandPurple
import com.nyoike.ventpulse.ui.theme.MintCalm
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingRoot(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    var step by remember { mutableIntStateOf(0) }
    val pages = onboardingPages
    val page = pages[step]

    CoreFlowBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 26.dp, vertical = 34.dp)
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Box(
                modifier = Modifier
                    .gentleBreathingFloat(amplitudeY = 10.dp, scaleRange = 0.022f, durationMillis = 3600)
                    .size(238.dp)
                    .clip(CircleShape)
                    .background(page.color.copy(alpha = 0.76f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(166.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.24f))
                )
                Text(text = page.emoji, style = MaterialTheme.typography.displayMedium)
            }
            Text(
                text = page.title,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 34.dp)
            )
            Text(
                text = page.subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 14.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 26.dp)
            ) {
                pages.forEachIndexed { index, _ ->
                    Surface(
                        shape = CircleShape,
                        color = if (index == step) BrandPurple else BrandPurple.copy(alpha = 0.18f),
                        modifier = Modifier.size(if (index == step) 12.dp else 9.dp)
                    ) {}
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryPulseButton(
                text = if (step == pages.lastIndex) "Choose my community" else "Continue",
                onClick = {
                    if (step == pages.lastIndex) {
                        viewModel.completeOnboarding(onFinish)
                    } else {
                        step += 1
                    }
                }
            )
        }
    }
}

private data class OnboardingPage(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val color: Color
)

private val onboardingPages = listOf(
    OnboardingPage(
        emoji = "🌤",
        title = "A safe place to feel.",
        subtitle = "Check in honestly without turning your emotions into a performance.",
        color = MintCalm
    ),
    OnboardingPage(
        emoji = "🫧",
        title = "Anonymous by design.",
        subtitle = "Your local pulse matters, but your identity stays protected.",
        color = BrandPurple
    ),
    OnboardingPage(
        emoji = "💬",
        title = "Vent, then breathe.",
        subtitle = "Share what is heavy and see the community pulse move with you.",
        color = Color(0xFFF4A6C8)
    )
)
