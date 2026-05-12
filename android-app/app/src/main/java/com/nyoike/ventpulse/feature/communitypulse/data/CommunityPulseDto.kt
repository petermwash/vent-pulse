package com.nyoike.ventpulse.feature.communitypulse.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedVentDto(
    @SerialName("id") val id: String,
    @SerialName("profile_id") val profileId: String,
    @SerialName("community_id") val communityId: String,
    @SerialName("mood") val mood: String,
    @SerialName("body") val body: String,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("anonymous_profiles") val profile: FeedProfileDto? = null
)

@Serializable
data class FeedProfileDto(
    @SerialName("alias") val alias: String? = null,
    @SerialName("avatar_seed") val avatarSeed: String? = null
)

@Serializable
data class VentReactionDto(
    @SerialName("id") val id: String? = null,
    @SerialName("vent_id") val ventId: String,
    @SerialName("profile_id") val profileId: String,
    @SerialName("reaction_type") val reactionType: String
)

@Serializable
data class ReportDto(
    @SerialName("vent_id") val ventId: String,
    @SerialName("reporter_profile_id") val reporterProfileId: String,
    @SerialName("reason") val reason: String,
    @SerialName("details") val details: String? = null
)

@Serializable
data class MoodSummaryDto(
    @SerialName("mood") val mood: String,
    @SerialName("count") val count: Int
)

@Serializable
data class ExpertProfileDto(
    @SerialName("id") val id: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("role") val role: String,
    @SerialName("specialty") val specialty: String? = null,
    @SerialName("avatar_seed") val avatarSeed: String,
    @SerialName("availability_status") val availabilityStatus: String,
    @SerialName("bio") val bio: String? = null
)
