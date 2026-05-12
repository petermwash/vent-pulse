package com.nyoike.ventpulse.feature.pulse.presentation

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyoike.ventpulse.feature.communitypulse.domain.MoodShare
import com.nyoike.ventpulse.feature.communitypulse.presentation.CommunityPulseAction
import com.nyoike.ventpulse.feature.communitypulse.presentation.CommunityPulseViewModel
import com.nyoike.ventpulse.feature.coreflow.domain.Mood
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowAction
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowBackground
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowEvent
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowViewModel
import com.nyoike.ventpulse.feature.coreflow.presentation.MoodBlob
import com.nyoike.ventpulse.feature.coreflow.presentation.PrimaryPulseButton
import com.nyoike.ventpulse.feature.coreflow.presentation.SoftBottomNavigation
import com.nyoike.ventpulse.feature.coreflow.presentation.color
import com.nyoike.ventpulse.ui.theme.BrandPurple
import com.nyoike.ventpulse.ui.theme.SecondaryBackground
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun PulseRoot(
    onNavigateToVent: (moodId: String, communityId: String) -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: CoreFlowViewModel = koinViewModel(),
    communityPulseViewModel: CommunityPulseViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val communityState by communityPulseViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(CoreFlowAction.Load)
    }

    LaunchedEffect(state.hasCheckedInToday) {
        if (state.hasCheckedInToday) {
            communityPulseViewModel.onAction(CommunityPulseAction.Load)
        }
    }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is CoreFlowEvent.NavigateToVentWriting -> onNavigateToVent(event.moodId, event.communityId)
                CoreFlowEvent.NavigateToPulse -> Unit
            }
        }
    }

    if (state.hasCheckedInToday) {
        EmotionalPulseScreen(
            shares = communityState.moodShares,
            message = communityState.message,
            onShareAnonymously = {
                val communityId = state.selectedCommunity?.id ?: return@EmotionalPulseScreen
                onNavigateToVent(state.selectedMood.id, communityId)
            },
            onNavigate = onNavigate
        )
    } else {
        PulseCheckInScreen(
            selectedMood = state.selectedMood,
            isSaving = state.isSaving,
            onSelectMood = { viewModel.onAction(CoreFlowAction.SelectMood(it)) },
            onSaveMood = { viewModel.onAction(CoreFlowAction.SaveMood) },
            onNavigate = onNavigate
        )
    }
}

@Composable
private fun PulseCheckInScreen(
    selectedMood: Mood,
    isSaving: Boolean,
    onSelectMood: (Mood) -> Unit,
    onSaveMood: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val moods = Mood.entries
    val pagerState = rememberPagerState(
        initialPage = moods.indexOf(selectedMood).coerceAtLeast(0),
        pageCount = { moods.size }
    )

    LaunchedEffect(selectedMood) {
        val selectedIndex = moods.indexOf(selectedMood).coerceAtLeast(0)
        if (pagerState.currentPage != selectedIndex) {
            pagerState.animateScrollToPage(selectedIndex)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collectLatest { page ->
            val mood = moods.getOrNull(page) ?: return@collectLatest
            if (mood != selectedMood) {
                onSelectMood(mood)
            }
        }
    }

    CoreFlowBackground(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 28.dp)
                    .padding(bottom = 104.dp)
            ) {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "How are you really feeling today?",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Swipe across the mood to find the feeling that fits best.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val mood = moods[page]
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MoodBlob(mood = mood)
                        Text(
                            text = mood.label,
                            style = MaterialTheme.typography.headlineSmall,
                            color = BrandPurple,
                            modifier = Modifier.padding(top = 18.dp)
                        )
                    }
                }
                MoodSelector(
                    selectedMood = selectedMood,
                    onSelectMood = onSelectMood,
                    modifier = Modifier.padding(top = 24.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                PrimaryPulseButton(
                    text = if (isSaving) "Saving..." else "Save today's check-in",
                    onClick = onSaveMood,
                    enabled = !isSaving
                )
            }
            SoftBottomNavigation(
                selected = "Pulse",
                onNavigate = onNavigate,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            )
        }
    }
}

@Composable
private fun EmotionalPulseScreen(
    shares: List<MoodShare>,
    message: String?,
    onShareAnonymously: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val visibleShares = shares.ifEmpty {
        listOf(
            MoodShare(Mood.HAPPY, 42, 42),
            MoodShare(Mood.CALM, 28, 28),
            MoodShare(Mood.ANXIOUS, 15, 15),
            MoodShare(Mood.SAD, 10, 10),
            MoodShare(Mood.ANGRY, 5, 5)
        )
    }

    CoreFlowBackground(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 28.dp)
                    .padding(bottom = 120.dp)
            ) {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Emotional Pulse",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Your pulse is logged for today. Here's how your community feels right now.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
                PulseDonut(
                    shares = visibleShares,
                    modifier = Modifier.padding(top = 34.dp)
                )
                visibleShares.forEach { share ->
                    MoodShareRow(
                        share = share,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = BrandPurple.copy(alpha = 0.86f),
                    tonalElevation = 0.dp,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text(
                        text = message ?: "Your neighborhood is feeling mostly happy and calm today. You're part of a community that cares.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }
                PrimaryPulseButton(
                    text = "Share anonymously",
                    onClick = onShareAnonymously,
                    modifier = Modifier.padding(top = 22.dp)
                )
            }
            SoftBottomNavigation(
                selected = "Pulse",
                onNavigate = onNavigate,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            )
        }
    }
}

@Composable
private fun MoodSelector(
    selectedMood: Mood,
    onSelectMood: (Mood) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Mood.entries.forEach { mood ->
            Surface(
                onClick = { onSelectMood(mood) },
                shape = CircleShape,
                color = mood.color.copy(alpha = if (mood == selectedMood) 0.95f else 0.38f),
                tonalElevation = 0.dp,
                shadowElevation = if (mood == selectedMood) 8.dp else 0.dp,
                modifier = Modifier.size(if (mood == selectedMood) 62.dp else 50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = mood.emoji, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
private fun PulseDonut(
    shares: List<MoodShare>,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(36.dp),
        color = SecondaryBackground.copy(alpha = 0.58f),
        tonalElevation = 0.dp,
        shadowElevation = 6.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(310.dp)
        ) {
            Canvas(modifier = Modifier.size(190.dp)) {
                var startAngle = -90f
                shares.forEach { share ->
                    val sweep = (share.percentage / 100f) * 360f
                    drawArc(
                        color = share.mood.color,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = 28.dp.toPx(), cap = StrokeCap.Butt)
                    )
                    startAngle += sweep
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = shares.size.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "emotions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MoodShareRow(
    share: MoodShare,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.White.copy(alpha = 0.94f),
        tonalElevation = 0.dp,
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(share.mood.color.copy(alpha = 0.18f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = share.mood.emoji, fontSize = 22.sp)
                }
                Text(
                    text = share.mood.label,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )
                Text(
                    text = "${share.percentage}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = share.mood.color
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(Color(0xFFF2EFFF), RoundedCornerShape(999.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(share.percentage / 100f)
                        .height(10.dp)
                        .background(share.mood.color, RoundedCornerShape(999.dp))
                )
            }
        }
    }
}
