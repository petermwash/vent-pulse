package com.nyoike.ventpulse.feature.communitypulse.domain

import com.nyoike.ventpulse.feature.coreflow.domain.Mood

data class CommunityVent(
    val id: String,
    val profileId: String,
    val alias: String,
    val avatarSeed: String,
    val mood: Mood,
    val body: String,
    val createdAt: String,
    val reactionCount: Int,
    val commentCount: Int,
    val hasReacted: Boolean
)

data class MoodShare(
    val mood: Mood,
    val percentage: Int,
    val count: Int
)

data class ExpertSupport(
    val id: String,
    val displayName: String,
    val role: String,
    val specialty: String,
    val avatarSeed: String,
    val availabilityStatus: String,
    val bio: String?
)
