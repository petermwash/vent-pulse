package com.nyoike.ventpulse.feature.community.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyoike.ventpulse.feature.coreflow.domain.Community
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowAction
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowBackground
import com.nyoike.ventpulse.feature.coreflow.presentation.CoreFlowViewModel
import com.nyoike.ventpulse.feature.coreflow.presentation.PrimaryPulseButton
import com.nyoike.ventpulse.ui.theme.BrandPurple
import com.nyoike.ventpulse.ui.theme.CalmMood
import com.nyoike.ventpulse.ui.theme.MintCalm
import org.koin.androidx.compose.koinViewModel

@Composable
fun CommunityRoot(
    onCommunitySelected: () -> Unit,
    viewModel: CoreFlowViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(CoreFlowAction.Load)
    }

    CommunityScreen(
        communities = state.communities,
        selectedCommunity = state.selectedCommunity,
        onSelectCommunity = { viewModel.onAction(CoreFlowAction.SelectCommunity(it)) },
        onContinue = onCommunitySelected
    )
}

@Composable
private fun CommunityScreen(
    communities: List<Community>,
    selectedCommunity: Community?,
    onSelectCommunity: (Community) -> Unit,
    onContinue: () -> Unit
) {
    CoreFlowBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Where should your pulse be felt?",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Choose the community closest to you. Your identity stays anonymous.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            CommunityMap(selectedCommunity = selectedCommunity)
            SelectedCommunityCard(selectedCommunity = selectedCommunity)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                communities.forEach { community ->
                    CommunityChip(
                        community = community,
                        selected = community.id == selectedCommunity?.id,
                        onClick = { onSelectCommunity(community) }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryPulseButton(
                text = "Continue to my pulse",
                onClick = onContinue,
                enabled = selectedCommunity != null
            )
        }
    }
}

@Composable
private fun CommunityMap(selectedCommunity: Community?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        contentAlignment = Alignment.Center
    ) {
        listOf(210.dp, 158.dp, 106.dp).forEachIndexed { index, size ->
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(
                        when (index) {
                            0 -> BrandPurple.copy(alpha = 0.08f)
                            1 -> CalmMood.copy(alpha = 0.18f)
                            else -> MintCalm.copy(alpha = 0.45f)
                        }
                    )
            )
        }
        Text(
            text = selectedCommunity?.name ?: "Find your circle",
            style = MaterialTheme.typography.titleMedium,
            color = BrandPurple,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SelectedCommunityCard(selectedCommunity: Community?) {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color.White.copy(alpha = 0.88f),
        tonalElevation = 0.dp,
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(22.dp)
        ) {
            Text(
                text = selectedCommunity?.level?.replace('_', ' ')?.uppercase() ?: "COMMUNITY",
                style = MaterialTheme.typography.bodyMedium,
                color = BrandPurple
            )
            Text(
                text = selectedCommunity?.name ?: "Loading community options...",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = selectedCommunity?.path ?: "VentPulse keeps the shared pulse local enough to feel real.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CommunityChip(
    community: Community,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        color = if (selected) BrandPurple else Color.White.copy(alpha = 0.82f),
        tonalElevation = 0.dp,
        shadowElevation = if (selected) 8.dp else 0.dp
    ) {
        Text(
            text = community.name,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
        )
    }
}
