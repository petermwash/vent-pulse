package com.nyoike.ventpulse.feature.matching.data

import com.nyoike.ventpulse.feature.matching.domain.AnonymousMatch
import com.nyoike.ventpulse.feature.matching.domain.ChatMessage
import com.nyoike.ventpulse.feature.matching.domain.MatchStatus

fun MatchDto.toAnonymousMatch(profileId: String): AnonymousMatch {
    val partnerId = when (profileId) {
        initiatorProfileId -> partnerProfileId
        partnerProfileId -> initiatorProfileId
        else -> partnerProfileId ?: initiatorProfileId
    }
    return AnonymousMatch(
        id = id,
        communityId = communityId,
        initiatorProfileId = initiatorProfileId,
        partnerProfileId = partnerProfileId,
        status = MatchStatus.fromRemote(status),
        partnerAlias = if (partnerId == null) "Someone nearby" else "Gentle Orbit",
        partnerAvatarSeed = partnerId ?: "gentle-orbit",
        currentProfileId = profileId
    )
}

fun ChatMessageDto.toChatMessage(currentProfileId: String): ChatMessage {
    return ChatMessage(
        id = id ?: "pending-${createdAt.orEmpty()}",
        matchId = matchId,
        profileId = profileId,
        alias = profile?.alias ?: if (profileId == currentProfileId) "You" else "Gentle Orbit",
        avatarSeed = profile?.avatarSeed ?: profileId,
        body = body,
        createdAt = formatChatTime(createdAt),
        isMine = profileId == currentProfileId,
        moderationStatus = moderationStatus
    )
}

private fun formatChatTime(value: String?): String {
    if (value.isNullOrBlank()) return "now"
    return runCatching {
        value.substringAfter("T").take(5)
    }.getOrDefault("now")
}
