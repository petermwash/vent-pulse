package com.nyoike.ventpulse.feature.matching.domain

data class AnonymousMatch(
    val id: String,
    val communityId: String,
    val initiatorProfileId: String,
    val partnerProfileId: String?,
    val status: MatchStatus,
    val partnerAlias: String,
    val partnerAvatarSeed: String,
    val currentProfileId: String,
    val isDemo: Boolean = false
)

enum class MatchStatus {
    WAITING,
    ACTIVE,
    ENDED,
    CANCELLED;

    companion object {
        fun fromRemote(value: String): MatchStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: WAITING
        }
    }
}

data class ChatMessage(
    val id: String,
    val matchId: String,
    val profileId: String,
    val alias: String,
    val avatarSeed: String,
    val body: String,
    val createdAt: String,
    val isMine: Boolean,
    val moderationStatus: String = "visible"
)
