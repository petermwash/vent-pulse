package com.nyoike.ventpulse.feature.community.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyoike.ventpulse.feature.communitypulse.domain.CommunityVent
import com.nyoike.ventpulse.feature.communitypulse.presentation.CommunityPulseAction
import com.nyoike.ventpulse.feature.communitypulse.presentation.CommunityPulseViewModel
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowBackground
import com.nyoike.ventpulse.feature.coreflow.presentation.SoftBottomNavigation
import com.nyoike.ventpulse.feature.coreflow.presentation.WarmSkeletonCard
import com.nyoike.ventpulse.feature.coreflow.presentation.color
import com.nyoike.ventpulse.ui.theme.BrandPurple
import org.koin.androidx.compose.koinViewModel

@Composable
fun CommunityFeedRoot(
    onNavigate: (String) -> Unit,
    viewModel: CommunityPulseViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(CommunityPulseAction.Load)
    }

    CoreFlowBackground(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(22.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 28.dp)
                    .padding(bottom = 104.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Community Pulse",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "You're not alone in what you feel",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    state.selectedCommunity?.let {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = BrandPurple,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
                items(state.feed, key = { it.id }) { vent ->
                    FadeInCard {
                        VentCard(
                            vent = vent,
                            onReact = { viewModel.onAction(CommunityPulseAction.ReactToVent(vent.id)) },
                            onReport = { viewModel.onAction(CommunityPulseAction.ReportVent(vent.id)) }
                        )
                    }
                }
                if (state.isLoading && state.feed.isEmpty()) {
                    items(3) {
                        WarmSkeletonCard(showAvatar = true, rows = 4)
                    }
                }
                if (!state.isLoading && state.feed.isEmpty()) {
                    item {
                        GentleStateCard(
                            title = "This space is quiet right now",
                            body = "When neighbors share, their vents will appear here with supportive ways to respond."
                        )
                    }
                }
                item {
                    Text(
                        text = state.message ?: "These are real feelings from people in your community",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
            }
            SoftBottomNavigation(
                selected = "Community",
                onNavigate = onNavigate,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            )
        }
    }
}

@Composable
private fun FadeInCard(content: @Composable () -> Unit) {
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
private fun GentleStateCard(
    title: String,
    body: String
) {
    Surface(
        shape = RoundedCornerShape(34.dp),
        color = Color.White.copy(alpha = 0.9f),
        tonalElevation = 0.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun VentCard(
    vent: CommunityVent,
    onReact: () -> Unit,
    onReport: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(34.dp),
        color = Color.White.copy(alpha = 0.94f),
        tonalElevation = 0.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            Box(
                modifier = Modifier
                    .widthMarker(vent)
                    .height(230.dp)
                    .clip(RoundedCornerShape(topStart = 34.dp, bottomStart = 34.dp))
                    .background(vent.mood.color.copy(alpha = 0.72f))
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(22.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Avatar(alias = vent.alias, color = vent.mood.color)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = vent.alias,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = vent.createdAt,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    MoodPill(text = vent.mood.id)
                }
                Text(
                    text = vent.body,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ActionPill(
                        text = if (vent.hasReacted) "Heard" else "I hear you",
                        onClick = onReact
                    )
                    ActionPill(text = "${vent.commentCount}", onClick = {})
                    ActionPill(text = "Report", onClick = onReport)
                }
            }
        }
    }
}

@Composable
private fun Avatar(alias: String, color: Color) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.82f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = alias.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}

@Composable
private fun MoodPill(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = BrandPurple,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(BrandPurple.copy(alpha = 0.08f))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    )
}

@Composable
private fun ActionPill(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        color = BrandPurple.copy(alpha = 0.08f),
        tonalElevation = 0.dp
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = BrandPurple,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

private fun Modifier.widthMarker(vent: CommunityVent): Modifier = this.then(
    Modifier.size(width = 5.dp, height = 230.dp)
)
