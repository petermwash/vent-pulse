package com.nyoike.ventpulse.feature.connect.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowBackground
import com.nyoike.ventpulse.feature.coreflow.presentation.PrimaryPulseButton
import com.nyoike.ventpulse.feature.coreflow.presentation.SoftBottomNavigation
import com.nyoike.ventpulse.ui.theme.BrandPurple
import com.nyoike.ventpulse.ui.theme.CalmMood
import com.nyoike.ventpulse.ui.theme.SadMood

@Composable
fun ConnectRoot(
    onNavigate: (String) -> Unit
) {
    CoreFlowBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Box(
                modifier = Modifier
                    .size(260.dp),
                contentAlignment = Alignment.Center
            ) {
                MoodCircle(
                    emoji = "😟",
                    color = SadMood,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .size(width = 120.dp, height = 8.dp)
                        .background(BrandPurple.copy(alpha = 0.22f))
                )
                MoodCircle(
                    emoji = "😌",
                    color = CalmMood,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            Text(
                text = "Finding someone who understands...",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 38.dp)
            )
            Text(
                text = "Realtime matching and chat land in Phase 5. This keeps the demo path focused without inventing unfinished behavior.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 14.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            PrimaryPulseButton(
                text = "Back to Community Pulse",
                onClick = { onNavigate("Community") }
            )
            SoftBottomNavigation(
                selected = "Connect",
                onNavigate = onNavigate,
                modifier = Modifier.padding(top = 18.dp)
            )
        }
    }
}

@Composable
private fun MoodCircle(
    emoji: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(126.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.82f)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = 42.sp)
    }
}
