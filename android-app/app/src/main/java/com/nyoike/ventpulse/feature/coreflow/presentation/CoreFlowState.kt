package com.nyoike.ventpulse.feature.coreflow.presentation

import com.nyoike.ventpulse.feature.coreflow.domain.Community
import com.nyoike.ventpulse.feature.coreflow.domain.Mood

data class CoreFlowState(
    val communities: List<Community> = emptyList(),
    val selectedCommunity: Community? = null,
    val selectedMood: Mood = Mood.CALM,
    val intensity: Int = 4,
    val ventText: String = "",
    val isSaving: Boolean = false,
    val message: String? = null
)

sealed interface CoreFlowAction {
    data object Load : CoreFlowAction
    data class PrepareVent(val moodId: String, val communityId: String) : CoreFlowAction
    data class SelectCommunity(val community: Community) : CoreFlowAction
    data class SelectMood(val mood: Mood) : CoreFlowAction
    data object SaveMood : CoreFlowAction
    data class ChangeVentText(val value: String) : CoreFlowAction
    data object SaveVent : CoreFlowAction
    data object DismissMessage : CoreFlowAction
}

sealed interface CoreFlowEvent {
    data class NavigateToVentWriting(val moodId: String, val communityId: String) : CoreFlowEvent
    data object NavigateToPulse : CoreFlowEvent
}
