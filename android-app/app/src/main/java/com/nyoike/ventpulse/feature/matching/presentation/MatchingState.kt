package com.nyoike.ventpulse.feature.matching.presentation

import com.nyoike.ventpulse.feature.coreflow.domain.Community
import com.nyoike.ventpulse.feature.matching.domain.AnonymousMatch
import com.nyoike.ventpulse.feature.matching.domain.ChatMessage

data class MatchingState(
    val isLoading: Boolean = false,
    val selectedCommunity: Community? = null,
    val phase: MatchingPhase = MatchingPhase.MATCHING,
    val match: AnonymousMatch? = null,
    val messages: List<ChatMessage> = emptyList(),
    val draft: String = "",
    val message: String? = null
)

enum class MatchingPhase {
    MATCHING,
    CHAT,
    ENDED
}

sealed interface MatchingAction {
    data object Load : MatchingAction
    data object StartMatching : MatchingAction
    data object SkipToChatDemo : MatchingAction
    data class ChangeDraft(val value: String) : MatchingAction
    data object SendMessage : MatchingAction
    data object EndConversation : MatchingAction
    data class ReportMessage(val messageId: String) : MatchingAction
    data object RequestExpert : MatchingAction
    data object DismissMessage : MatchingAction
}

sealed interface MatchingEvent {
    data object NavigateToExpertSupport : MatchingEvent
}
