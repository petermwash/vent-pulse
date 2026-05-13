package com.nyoike.ventpulse.feature.safespace.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyoike.ventpulse.core.presentation.ObserveAsEvents
import com.nyoike.ventpulse.feature.communitypulse.domain.MoodShare
import com.nyoike.ventpulse.feature.communitypulse.presentation.CommunityPulseAction
import com.nyoike.ventpulse.feature.communitypulse.presentation.CommunityPulseViewModel
import com.nyoike.ventpulse.feature.coreflow.domain.Mood
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowAction
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowBackground
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowEvent
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowViewModel
import com.nyoike.ventpulse.feature.coreflow.presentation.PrimaryPulseButton
import com.nyoike.ventpulse.feature.coreflow.presentation.SoftBottomNavigation
import com.nyoike.ventpulse.feature.coreflow.presentation.color
import com.nyoike.ventpulse.ui.theme.BrandPurple
import com.nyoike.ventpulse.ui.theme.SecondaryBackground
import org.koin.androidx.compose.koinViewModel

@Composable
fun VentWritingRoot(
    moodId: String,
    communityId: String,
    onSaved: () -> Unit,
    onSkip: () -> Unit,
    viewModel: CoreFlowViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(moodId, communityId) {
        viewModel.onAction(CoreFlowAction.Load)
        viewModel.onAction(CoreFlowAction.PrepareVent(moodId = moodId, communityId = communityId))
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CoreFlowEvent.NavigateToPulse -> onSaved()
            is CoreFlowEvent.NavigateToVentWriting -> Unit
        }
    }

    VentWritingScreen(
        mood = state.selectedMood,
        ventText = state.ventText,
        isSaving = state.isSaving,
        onVentTextChanged = { viewModel.onAction(CoreFlowAction.ChangeVentText(it)) },
        onSave = { viewModel.onAction(CoreFlowAction.SaveVent) },
        onSkip = onSkip
    )
}

@Composable
fun SafeSpaceRoot(
    onNavigate: (String) -> Unit,
    viewModel: CommunityPulseViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(CommunityPulseAction.Load)
    }

    CoreFlowBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Emotional Pulse",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "How your community is feeling right now",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
            PulseDonut(
                shares = state.moodShares,
                modifier = Modifier.padding(top = 34.dp)
            )
            state.moodShares.forEach { share ->
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
                    text = "Your neighborhood is feeling mostly happy and calm today. You're part of a community that cares.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
            }
            SoftBottomNavigation(
                selected = "Safe Space",
                onNavigate = onNavigate,
                modifier = Modifier.padding(top = 18.dp, bottom = 18.dp)
            )
        }
    }
}

@Composable
private fun VentWritingScreen(
    mood: Mood,
    ventText: String,
    isSaving: Boolean,
    onVentTextChanged: (String) -> Unit,
    onSave: () -> Unit,
    onSkip: () -> Unit
) {
    CoreFlowBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Surface(
                shape = CircleShape,
                color = mood.color.copy(alpha = 0.72f),
                tonalElevation = 0.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.size(94.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = mood.emoji, fontSize = 40.sp)
                }
            }
            Text(
                text = mood.prompt,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                text = "Optionally share why you feel this way. You can skip and keep only the pulse check-in.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp)
            )
            OutlinedTextField(
                value = ventText,
                onValueChange = onVentTextChanged,
                placeholder = {
                    Text("Start with what happened, or just name the feeling.")
                },
                minLines = 8,
                maxLines = 10,
                shape = RoundedCornerShape(30.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.82f),
                    focusedBorderColor = BrandPurple.copy(alpha = 0.7f),
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color.White.copy(alpha = 0.86f),
                tonalElevation = 0.dp,
                shadowElevation = 4.dp
            ) {
                Text(
                    text = "Hold for voice venting coming soon",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryPulseButton(
                text = if (isSaving) "Sharing..." else "Share anonymously",
                onClick = onSave,
                enabled = !isSaving
            )
            Text(
                text = "Skip for now",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(top = 18.dp, bottom = 6.dp)
                    .clickable(onClick = onSkip)
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            )
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(8.dp)
                    .background(SecondaryBackground, RoundedCornerShape(999.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(share.percentage / 100f)
                        .height(8.dp)
                        .background(share.mood.color, RoundedCornerShape(999.dp))
                )
            }
        }
    }
}
