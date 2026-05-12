package com.nyoike.ventpulse.feature.coreflow.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyoike.ventpulse.feature.coreflow.domain.Mood
import com.nyoike.ventpulse.ui.theme.AngryMood
import com.nyoike.ventpulse.ui.theme.AnxiousMood
import com.nyoike.ventpulse.ui.theme.BrandPurple
import com.nyoike.ventpulse.ui.theme.CalmMood
import com.nyoike.ventpulse.ui.theme.HappyMood
import com.nyoike.ventpulse.ui.theme.LonelyMood
import com.nyoike.ventpulse.ui.theme.MainBackground
import com.nyoike.ventpulse.ui.theme.MintCalm
import com.nyoike.ventpulse.ui.theme.SadMood
import com.nyoike.ventpulse.ui.theme.SecondaryBackground

@Composable
fun CoreFlowBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(MainBackground, SecondaryBackground.copy(alpha = 0.72f))
                )
            )
    ) {
        Box(
            modifier = Modifier
                .offset(x = (-52).dp, y = 72.dp)
                .size(164.dp)
                .clip(CircleShape)
                .background(MintCalm.copy(alpha = 0.28f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 52.dp, y = (-28).dp)
                .size(188.dp)
                .clip(CircleShape)
                .background(BrandPurple.copy(alpha = 0.1f))
        )
        content()
    }
}

@Composable
fun PrimaryPulseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(999.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandPurple,
            contentColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun MoodBlob(
    mood: Mood,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(220.dp)
            .clip(CircleShape)
            .background(mood.color.copy(alpha = 0.84f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(164.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.22f))
        )
        Text(
            text = mood.emoji,
            fontSize = 78.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SoftBottomNavigation(
    selected: String,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color.White.copy(alpha = 0.92f),
        tonalElevation = 0.dp,
        shadowElevation = 12.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            listOf("Pulse", "Community", "Connect", "Safe Space", "Profile").forEach { item ->
                val isSelected = item == selected
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) BrandPurple else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .clickable { onNavigate(item) }
                        .background(if (isSelected) BrandPurple.copy(alpha = 0.12f) else Color.Transparent)
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }
        }
    }
}

val Mood.color: Color
    get() = when (this) {
        Mood.HAPPY -> HappyMood
        Mood.CALM -> CalmMood
        Mood.SAD -> SadMood
        Mood.ANGRY -> AngryMood
        Mood.ANXIOUS -> AnxiousMood
        Mood.LONELY -> LonelyMood
    }
