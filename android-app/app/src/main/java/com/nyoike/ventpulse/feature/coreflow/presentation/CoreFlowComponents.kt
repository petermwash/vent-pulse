package com.nyoike.ventpulse.feature.coreflow.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
    val animatedColor by androidx.compose.animation.animateColorAsState(
        targetValue = mood.color.copy(alpha = 0.84f),
        animationSpec = tween(durationMillis = 520, easing = FastOutSlowInEasing),
        label = "mood blob color"
    )

    Box(
        modifier = modifier
            .gentleBreathingFloat(amplitudeY = 8.dp, scaleRange = 0.025f, durationMillis = 3400)
            .size(220.dp)
            .clip(CircleShape)
            .background(animatedColor),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(164.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.22f))
        )
        AnimatedContent(
            targetState = mood.emoji,
            transitionSpec = {
                (fadeIn(tween(420)) + scaleIn(initialScale = 0.92f, animationSpec = tween(420))) togetherWith
                    (fadeOut(tween(220)) + scaleOut(targetScale = 0.96f, animationSpec = tween(220)))
            },
            label = "mood emoji"
        ) { emoji ->
            Text(
                text = emoji,
                fontSize = 78.sp,
                textAlign = TextAlign.Center
            )
        }
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
            listOf("Pulse", "Community", "Connect", "Profile").forEach { item ->
                val isSelected = item == selected
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) BrandPurple else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .semantics {
                            role = Role.Tab
                            this.selected = isSelected
                            contentDescription = "$item tab"
                        }
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

@Composable
fun Modifier.gentleBreathingFloat(
    amplitudeY: Dp = 6.dp,
    scaleRange: Float = 0.018f,
    durationMillis: Int = 3000,
    delayMillis: Int = 0
): Modifier {
    val transition = rememberInfiniteTransition(label = "gentle floating motion")
    val scale by transition.animateFloat(
        initialValue = 1f - scaleRange,
        targetValue = 1f + scaleRange,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gentle scale"
    )
    val yOffset by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis + 420,
                delayMillis = delayMillis,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gentle y"
    )
    val amplitudePx = with(LocalDensity.current) { amplitudeY.toPx() }

    return graphicsLayer {
        scaleX = scale
        scaleY = scale
        translationY = yOffset * amplitudePx
    }
}

@Composable
fun WarmSkeletonCard(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    showAvatar: Boolean = false
) {
    val transition = rememberInfiniteTransition(label = "warm skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.34f,
        targetValue = 0.72f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeleton alpha"
    )
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = Color.White.copy(alpha = 0.86f),
        tonalElevation = 0.dp,
        shadowElevation = 5.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp)
        ) {
            if (showAvatar) {
                SkeletonBlock(
                    alpha = alpha,
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                repeat(rows) { index ->
                    SkeletonBlock(
                        alpha = alpha * (1f - index * 0.08f),
                        modifier = Modifier
                            .fillMaxWidth(if (index == rows - 1) 0.62f else 1f)
                            .height(if (index == 0) 18.dp else 12.dp)
                            .clip(RoundedCornerShape(999.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun SkeletonBlock(
    alpha: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(
            Brush.horizontalGradient(
                listOf(
                    BrandPurple.copy(alpha = 0.06f),
                    MintCalm.copy(alpha = alpha * 0.22f),
                    BrandPurple.copy(alpha = 0.08f)
                )
            )
        )
    )
}
