package com.nyoike.ventpulse.feature.communitypulse.data

import com.nyoike.ventpulse.feature.communitypulse.domain.CommunityVent
import com.nyoike.ventpulse.feature.communitypulse.domain.ExpertSupport
import com.nyoike.ventpulse.feature.communitypulse.domain.MoodShare
import com.nyoike.ventpulse.feature.coreflow.domain.Mood
import kotlin.math.floor

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
    val counts = groupingBy { Mood.fromId(it.mood) }.fold(0) { total, item -> total + item.count }
    return counts.toMoodShares(
        moodOrder = map { Mood.fromId(it.mood) }.distinct()
    )
}

fun List<MoodShare>.withSeededDemoPulse(): List<MoodShare> {
    if (isEmpty()) return fallbackMoodShares

    val mergedCounts = mutableMapOf<Mood, Int>()
    fallbackMoodShares.forEach { share ->
        mergedCounts[share.mood] = (mergedCounts[share.mood] ?: 0) + share.count
    }
    forEach { share ->
        mergedCounts[share.mood] = (mergedCounts[share.mood] ?: 0) + share.count
    }

    val moodOrder = (fallbackMoodShares.map { it.mood } + map { it.mood }).distinct()
    return mergedCounts.toMoodShares(moodOrder)
}

private fun Map<Mood, Int>.toMoodShares(moodOrder: List<Mood>): List<MoodShare> {
    val orderedCounts = moodOrder.mapNotNull { mood ->
        val count = this[mood]?.takeIf { it > 0 } ?: return@mapNotNull null
        mood to count
    }
    if (orderedCounts.isEmpty()) return fallbackMoodShares

    val total = orderedCounts.sumOf { it.second }.coerceAtLeast(1)
    val rawShares = orderedCounts.map { (mood, count) ->
        val exact = (count.toDouble() / total) * 100
        MoodPercentage(
            mood = mood,
            count = count,
            percentage = floor(exact).toInt().coerceAtLeast(1),
            remainder = exact - floor(exact)
        )
    }.toMutableList()

    var remaining = 100 - rawShares.sumOf { it.percentage }
    if (remaining > 0) {
        rawShares.indices
            .sortedByDescending { rawShares[it].remainder }
            .forEach { index ->
                if (remaining > 0) {
                    rawShares[index] = rawShares[index].copy(percentage = rawShares[index].percentage + 1)
                    remaining -= 1
                }
            }
    } else if (remaining < 0) {
        rawShares.indices
            .sortedBy { rawShares[it].remainder }
            .forEach { index ->
                while (remaining < 0 && rawShares[index].percentage > 1) {
                    rawShares[index] = rawShares[index].copy(percentage = rawShares[index].percentage - 1)
                    remaining += 1
                }
            }
    }

    return rawShares.map {
        MoodShare(
            mood = it.mood,
            percentage = it.percentage,
            count = it.count
        )
    }
}

private data class MoodPercentage(
    val mood: Mood,
    val count: Int,
    val percentage: Int,
    val remainder: Double
)

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
