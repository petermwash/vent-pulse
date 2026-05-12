package com.nyoike.ventpulse.feature.safespace.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyoike.ventpulse.core.presentation.ObserveAsEvents
import com.nyoike.ventpulse.feature.coreflow.domain.Mood
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowAction
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowBackground
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowEvent
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowViewModel
import com.nyoike.ventpulse.feature.coreflow.presentation.PrimaryPulseButton
import com.nyoike.ventpulse.feature.coreflow.presentation.SoftBottomNavigation
import com.nyoike.ventpulse.feature.coreflow.presentation.color
import com.nyoike.ventpulse.ui.theme.BrandPurple
import org.koin.androidx.compose.koinViewModel

@Composable
fun VentWritingRoot(
    moodId: String,
    communityId: String,
    onSaved: () -> Unit,
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
        onSave = { viewModel.onAction(CoreFlowAction.SaveVent) }
    )
}

@Composable
fun SafeSpaceRoot() {
    VentWritingScreen(
        mood = Mood.CALM,
        ventText = "",
        isSaving = false,
        onVentTextChanged = {},
        onSave = {}
    )
}

@Composable
private fun VentWritingScreen(
    mood: Mood,
    ventText: String,
    isSaving: Boolean,
    onVentTextChanged: (String) -> Unit,
    onSave: () -> Unit
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
                text = "Write freely. It posts anonymously to your selected community pulse.",
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
                text = if (isSaving) "Saving..." else "Save anonymous vent",
                onClick = onSave,
                enabled = !isSaving
            )
            SoftBottomNavigation(
                selected = "Vent",
                modifier = Modifier.padding(top = 18.dp)
            )
        }
    }
}
