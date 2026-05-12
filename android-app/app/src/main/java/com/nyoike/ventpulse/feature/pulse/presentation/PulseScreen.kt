package com.nyoike.ventpulse.feature.pulse.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyoike.ventpulse.core.presentation.ObserveAsEvents
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun PulseRoot(
    onNavigateToVent: (moodId: String, communityId: String) -> Unit,
    viewModel: CoreFlowViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(CoreFlowAction.Load)
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CoreFlowEvent.NavigateToVentWriting -> onNavigateToVent(event.moodId, event.communityId)
            CoreFlowEvent.NavigateToPulse -> Unit
        }
    }

    PulseScreen(
        selectedMood = state.selectedMood,
        isSaving = state.isSaving,
        onSelectMood = { viewModel.onAction(CoreFlowAction.SelectMood(it)) },
        onSaveMood = { viewModel.onAction(CoreFlowAction.SaveMood) }
    )
}

@Composable
private fun PulseScreen(
    selectedMood: Mood,
    isSaving: Boolean,
    onSelectMood: (Mood) -> Unit,
    onSaveMood: () -> Unit
) {
    CoreFlowBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "How are you really feeling today?",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                text = "No pressure. Just be honest.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MoodStepButton(text = "<") {
                    onSelectMood(selectedMood.previous())
                }
                MoodBlob(mood = selectedMood)
                MoodStepButton(text = ">") {
                    onSelectMood(selectedMood.next())
                }
            }
            Text(
                text = selectedMood.label,
                style = MaterialTheme.typography.headlineSmall,
                color = BrandPurple,
                modifier = Modifier.padding(top = 18.dp)
            )
            MoodSelector(
                selectedMood = selectedMood,
                onSelectMood = onSelectMood,
                modifier = Modifier.padding(top = 24.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            PrimaryPulseButton(
                text = if (isSaving) "Saving..." else "I want to talk about this",
                onClick = onSaveMood,
                enabled = !isSaving
            )
            SoftBottomNavigation(
                selected = "Pulse",
                modifier = Modifier.padding(top = 18.dp)
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
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        Mood.entries.forEach { mood ->
            val size by animateDpAsState(
                targetValue = if (mood == selectedMood) 62.dp else 50.dp,
                label = "mood-size"
            )
            Surface(
                onClick = { onSelectMood(mood) },
                shape = CircleShape,
                color = mood.color.copy(alpha = if (mood == selectedMood) 0.95f else 0.38f),
                tonalElevation = 0.dp,
                shadowElevation = if (mood == selectedMood) 8.dp else 0.dp,
                modifier = Modifier.size(size)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = mood.emoji, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
private fun MoodStepButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.88f),
        tonalElevation = 0.dp,
        shadowElevation = 6.dp,
        modifier = Modifier.size(44.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = BrandPurple
            )
        }
    }
}

private fun Mood.next(): Mood {
    val moods = Mood.entries
    return moods[(moods.indexOf(this) + 1) % moods.size]
}

private fun Mood.previous(): Mood {
    val moods = Mood.entries
    return moods[(moods.indexOf(this) - 1 + moods.size) % moods.size]
}
