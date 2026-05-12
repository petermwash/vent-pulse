package com.nyoike.ventpulse.feature.communitypulse.presentation

import com.nyoike.ventpulse.feature.communitypulse.domain.CommunityVent
import com.nyoike.ventpulse.feature.communitypulse.domain.ExpertSupport
import com.nyoike.ventpulse.feature.communitypulse.domain.MoodShare
import com.nyoike.ventpulse.feature.coreflow.domain.Community

data class CommunityPulseState(
    val selectedCommunity: Community? = null,
    val feed: List<CommunityVent> = emptyList(),
    val moodShares: List<MoodShare> = emptyList(),
    val experts: List<ExpertSupport> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)

sealed interface CommunityPulseAction {
    data object Load : CommunityPulseAction
    data class ReactToVent(val ventId: String) : CommunityPulseAction
    data class ReportVent(val ventId: String) : CommunityPulseAction
    data object DismissMessage : CommunityPulseAction
}
