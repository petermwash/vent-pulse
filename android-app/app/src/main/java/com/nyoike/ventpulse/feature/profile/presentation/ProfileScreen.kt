package com.nyoike.ventpulse.feature.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyoike.ventpulse.feature.communitypulse.domain.ExpertSupport
import com.nyoike.ventpulse.feature.communitypulse.presentation.CommunityPulseAction
import com.nyoike.ventpulse.feature.communitypulse.presentation.CommunityPulseViewModel
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowBackground
import com.nyoike.ventpulse.feature.coreflow.presentation.SoftBottomNavigation
import com.nyoike.ventpulse.feature.coreflow.presentation.WarmSkeletonCard
import com.nyoike.ventpulse.ui.theme.BrandPurple
import com.nyoike.ventpulse.ui.theme.CalmMood
import com.nyoike.ventpulse.ui.theme.MintCalm
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileRoot(
    onNavigate: (String) -> Unit,
    onExpertChat: (ExpertSupport) -> Unit,
    onExpertCall: (ExpertSupport) -> Unit,
    viewModel: CommunityPulseViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(CommunityPulseAction.Load)
    }

    CoreFlowBackground(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 28.dp)
                    .padding(bottom = 104.dp)
            ) {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Expert Support",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Connect with trained professionals when you need extra support",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AnonymousProfileCard(communityName = state.selectedCommunity?.name ?: "Your community")
                CrisisSupportCard()
                if (state.isLoading && state.experts.isEmpty()) {
                    repeat(2) {
                        WarmSkeletonCard(showAvatar = true, rows = 3)
                    }
                }
                state.experts.forEach { expert ->
                    FadeInProfileCard {
                        ExpertCard(
                            expert = expert,
                            onChat = { onExpertChat(expert) },
                            onCall = { onExpertCall(expert) }
                        )
                    }
                }
            }
            SoftBottomNavigation(
                selected = "Profile",
                onNavigate = onNavigate,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            )
        }
    }
}

@Composable
private fun FadeInProfileCard(content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(durationMillis = 520, easing = FastOutSlowInEasing)) +
            slideInVertically(
                animationSpec = tween(durationMillis = 520, easing = FastOutSlowInEasing),
                initialOffsetY = { it / 5 }
            )
    ) {
        content()
    }
}

@Composable
private fun AnonymousProfileCard(communityName: String) {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color.White.copy(alpha = 0.9f),
        tonalElevation = 0.dp,
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(BrandPurple.copy(alpha = 0.78f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "QC",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = "Quiet Cloud",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Anonymous in $communityName",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CrisisSupportCard() {
    Surface(
        shape = RoundedCornerShape(32.dp),
        tonalElevation = 0.dp,
        shadowElevation = 10.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Brush.horizontalGradient(listOf(MintCalm, CalmMood)))
                .padding(26.dp)
        ) {
            Text(text = "💚", style = MaterialTheme.typography.displayMedium)
            Column(modifier = Modifier.padding(start = 18.dp)) {
                Text(
                    text = "Crisis Support 24/7",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = "If you're in crisis, immediate help is available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.86f)
                )
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White,
                    modifier = Modifier.padding(top = 14.dp)
                ) {
                    Text(
                        text = "Get Help Now",
                        style = MaterialTheme.typography.labelLarge,
                        color = CalmMood,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpertCard(
    expert: ExpertSupport,
    onChat: () -> Unit,
    onCall: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = Color.White.copy(alpha = 0.94f),
        tonalElevation = 0.dp,
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(BrandPurple.copy(alpha = 0.82f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = expert.displayName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = expert.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = expert.role,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BrandPurple
                    )
                    Text(
                        text = expert.specialty,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = expert.availabilityStatus.replace('_', ' '),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MintCalm,
                    modifier = Modifier
                        .background(MintCalm.copy(alpha = 0.14f), RoundedCornerShape(999.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ExpertAction(text = "Chat", onClick = onChat, modifier = Modifier.weight(1f))
                ExpertAction(text = "Call", onClick = onCall, modifier = Modifier.weight(1f))
                ExpertAction(text = "Plan", modifier = Modifier.weight(0.7f))
            }
        }
    }
}

@Composable
private fun ExpertAction(
    text: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = BrandPurple.copy(alpha = 0.08f),
        modifier = modifier.then(
            if (onClick != null) {
                Modifier.clickable(onClick = onClick)
            } else {
                Modifier
            }
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = BrandPurple,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp)
        )
    }
}
