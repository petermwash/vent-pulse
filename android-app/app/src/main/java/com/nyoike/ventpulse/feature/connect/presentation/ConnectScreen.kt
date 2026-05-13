package com.nyoike.ventpulse.feature.connect.presentation

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyoike.ventpulse.core.presentation.ObserveAsEvents
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowBackground
import com.nyoike.ventpulse.feature.coreflow.presentation.PrimaryPulseButton
import com.nyoike.ventpulse.feature.coreflow.presentation.SoftBottomNavigation
import com.nyoike.ventpulse.feature.matching.domain.ChatMessage
import com.nyoike.ventpulse.feature.matching.presentation.MatchingAction
import com.nyoike.ventpulse.feature.matching.presentation.MatchingEvent
import com.nyoike.ventpulse.feature.matching.presentation.MatchingPhase
import com.nyoike.ventpulse.feature.matching.presentation.MatchingState
import com.nyoike.ventpulse.feature.matching.presentation.MatchingViewModel
import com.nyoike.ventpulse.ui.theme.BrandPurple
import com.nyoike.ventpulse.ui.theme.CalmMood
import com.nyoike.ventpulse.ui.theme.SadMood
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConnectRoot(
    onNavigate: (String) -> Unit,
    viewModel: MatchingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            MatchingEvent.NavigateToExpertSupport -> onNavigate("Profile")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onAction(MatchingAction.Load)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onAction(MatchingAction.ScreenHidden)
        }
    }

    ConnectScreen(
        state = state,
        onNavigate = onNavigate,
        onAction = viewModel::onAction
    )
}

@Composable
fun ConnectScreen(
    state: MatchingState,
    onNavigate: (String) -> Unit,
    onAction: (MatchingAction) -> Unit
) {
    CoreFlowBackground(modifier = Modifier.fillMaxSize()) {
        when (state.phase) {
            MatchingPhase.MATCHING -> MatchingContent(
                state = state,
                onNavigate = onNavigate,
                onAction = onAction
            )

            MatchingPhase.CHAT -> ChatContent(
                state = state,
                onNavigate = onNavigate,
                onAction = onAction
            )

            MatchingPhase.ENDED -> EndedContent(
                state = state,
                onNavigate = onNavigate,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun MatchingContent(
    state: MatchingState,
    onNavigate: (String) -> Unit,
    onAction: (MatchingAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp)
                .padding(bottom = 104.dp)
        ) {
            Spacer(modifier = Modifier.height(110.dp))
            Box(
                modifier = Modifier.size(282.dp),
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
                        .fillMaxWidth(0.42f)
                        .background(BrandPurple.copy(alpha = 0.16f))
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
                modifier = Modifier.padding(top = 32.dp)
            )
            Text(
                text = "We're connecting you with someone who feels similarly. This is a safe, anonymous space.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 14.dp)
            )
            LoadingDots(modifier = Modifier.padding(top = 54.dp))
            state.message?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandPurple,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 22.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryPulseButton(
                text = "Skip to Chat Demo",
                onClick = { onAction(MatchingAction.SkipToChatDemo) }
            )
        }
        SoftBottomNavigation(
            selected = "Connect",
            onNavigate = onNavigate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 18.dp)
        )
    }
}

@Composable
private fun ChatContent(
    state: MatchingState,
    onNavigate: (String) -> Unit,
    onAction: (MatchingAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 20.dp)
                .padding(bottom = 104.dp)
        ) {
            ChatHeader(
                partnerAlias = state.match?.partnerAlias ?: "Gentle Orbit",
                onEnd = { onAction(MatchingAction.EndConversation) },
                onExpert = { onAction(MatchingAction.RequestExpert) }
            )
            state.message?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandPurple,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp)
            ) {
                if (state.messages.isEmpty()) {
                    item {
                        EmptyChatCard()
                    }
                }
                items(state.messages, key = { it.id }) { message ->
                    ChatBubble(
                        message = message,
                        onReport = { onAction(MatchingAction.ReportMessage(message.id)) }
                    )
                }
            }
            ChatComposer(
                value = state.draft,
                onValueChange = { onAction(MatchingAction.ChangeDraft(it)) },
                onSend = { onAction(MatchingAction.SendMessage) }
            )
        }
        SoftBottomNavigation(
            selected = "Connect",
            onNavigate = onNavigate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 18.dp)
        )
    }
}

@Composable
private fun EmptyChatCard() {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color.White.copy(alpha = 0.88f),
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(text = "🫶", fontSize = 40.sp)
            Text(
                text = "The room is open",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = "Start gently, or wait for the other anonymous person to share first.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun EndedContent(
    state: MatchingState,
    onNavigate: (String) -> Unit,
    onAction: (MatchingAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp)
                .padding(bottom = 104.dp)
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Text(text = "🫶", fontSize = 72.sp)
            Text(
                text = "Conversation ended",
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 22.dp)
            )
            Text(
                text = state.message ?: "You stayed in control of your safe space.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 14.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            PrimaryPulseButton(
                text = "Start New Match",
                onClick = { onAction(MatchingAction.StartMatching) }
            )
            PrimaryPulseButton(
                text = "Request Expert Support",
                onClick = { onAction(MatchingAction.RequestExpert) },
                modifier = Modifier.padding(top = 12.dp)
            )
        }
        SoftBottomNavigation(
            selected = "Connect",
            onNavigate = onNavigate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 18.dp)
        )
    }
}

@Composable
private fun ChatHeader(
    partnerAlias: String,
    onEnd: () -> Unit,
    onExpert: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color.White.copy(alpha = 0.9f),
        shadowElevation = 10.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(CalmMood.copy(alpha = 0.34f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "😌", fontSize = 26.sp)
                }
                Column(modifier = Modifier.padding(start = 14.dp)) {
                    Text(
                        text = partnerAlias,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Anonymous support chat",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(top = 14.dp)
            ) {
                SafetyPill(text = "Request expert", onClick = onExpert)
                SafetyPill(text = "End", onClick = onEnd)
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    onReport: () -> Unit
) {
    Column(
        horizontalAlignment = if (message.isMine) Alignment.End else Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomStart = if (message.isMine) 24.dp else 8.dp,
                bottomEnd = if (message.isMine) 8.dp else 24.dp
            ),
            color = if (message.isMine) BrandPurple.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.92f),
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth(0.82f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (message.isMine) "You" else message.alias,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (message.isMine) Color.White.copy(alpha = 0.82f) else BrandPurple
                )
                Text(
                    text = message.body,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (message.isMine) Color.White else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
        if (!message.isMine) {
            Text(
                text = "Report",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(top = 5.dp, start = 10.dp)
                    .clickable(onClick = onReport)
            )
        }
    }
}

@Composable
private fun ChatComposer(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Share gently...") },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.weight(1f)
        )
        Surface(
            shape = CircleShape,
            color = BrandPurple,
            modifier = Modifier
                .size(54.dp)
                .clickable(onClick = onSend)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "Send", color = Color.White, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun SafetyPill(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = BrandPurple.copy(alpha = 0.1f),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = BrandPurple,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun LoadingDots(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        listOf(0.96f, 0.48f, 0.28f).forEach { alpha ->
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(BrandPurple.copy(alpha = alpha))
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
            .size(134.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.82f)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = 42.sp)
    }
}
