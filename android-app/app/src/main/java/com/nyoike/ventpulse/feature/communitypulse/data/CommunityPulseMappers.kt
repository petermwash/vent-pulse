package com.nyoike.ventpulse.feature.communitypulse.data

import com.nyoike.ventpulse.feature.communitypulse.domain.CommunityVent
import com.nyoike.ventpulse.feature.communitypulse.domain.ExpertSupport
import com.nyoike.ventpulse.feature.communitypulse.domain.MoodShare
import com.nyoike.ventpulse.feature.coreflow.domain.Mood

fun FeedVentDto.toCommunityVent(
    reactionCount: Int,
    hasReacted: Boolean
): CommunityVent {
    val alias = profile?.alias ?: fallbackAlias(profileId)
    return CommunityVent(
        id = id,
        profileId = profileId,
        alias = alias,
        avatarSeed = profile?.avatarSeed ?: alias.lowercase().replace(" ", "-"),
        mood = Mood.fromId(mood),
        body = body,
        createdAt = createdAt ?: "now",
        reactionCount = reactionCount,
        commentCount = reactionCount * 2,
        hasReacted = hasReacted
    )
}

fun ExpertProfileDto.toExpertSupport(): ExpertSupport {
    return ExpertSupport(
        id = id,
        displayName = displayName,
        role = role.replace('_', ' ').replaceFirstChar { it.uppercase() },
        specialty = specialty ?: "Emotional support",
        avatarSeed = avatarSeed,
        availabilityStatus = availabilityStatus,
        bio = bio
    )
}

fun List<MoodSummaryDto>.toMoodShares(): List<MoodShare> {
    val total = sumOf { it.count }.coerceAtLeast(1)
    val mapped = map {
        MoodShare(
            mood = Mood.fromId(it.mood),
            percentage = ((it.count.toFloat() / total) * 100).toInt(),
            count = it.count
        )
    }
    return mapped.ifEmpty { fallbackMoodShares }
}

private fun fallbackAlias(profileId: String): String {
    val names = listOf("Quiet Cloud", "Soft River", "Gentle Orbit", "Moon Pebble")
    val index = kotlin.math.abs(profileId.hashCode()) % names.size
    return names[index]
}

val fallbackMoodShares = listOf(
    MoodShare(Mood.HAPPY, percentage = 42, count = 42),
    MoodShare(Mood.CALM, percentage = 28, count = 28),
    MoodShare(Mood.ANXIOUS, percentage = 15, count = 15),
    MoodShare(Mood.SAD, percentage = 10, count = 10),
    MoodShare(Mood.ANGRY, percentage = 5, count = 5)
)
