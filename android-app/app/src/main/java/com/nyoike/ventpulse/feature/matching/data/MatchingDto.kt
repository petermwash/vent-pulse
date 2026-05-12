package com.nyoike.ventpulse.feature.matching.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchDto(
    @SerialName("id") val id: String,
    @SerialName("community_id") val communityId: String,
    @SerialName("initiator_profile_id") val initiatorProfileId: String,
    @SerialName("partner_profile_id") val partnerProfileId: String? = null,
    @SerialName("status") val status: String,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Serializable
data class CreateMatchDto(
    @SerialName("community_id") val communityId: String,
    @SerialName("initiator_profile_id") val initiatorProfileId: String,
    @SerialName("status") val status: String = "waiting"
)

@Serializable
data class JoinMatchDto(
    @SerialName("partner_profile_id") val partnerProfileId: String,
    @SerialName("status") val status: String = "active"
)

@Serializable
data class EndMatchDto(
    @SerialName("status") val status: String = "ended",
    @SerialName("ended_at") val endedAt: String
)

@Serializable
data class ChatMessageDto(
    @SerialName("id") val id: String? = null,
    @SerialName("match_id") val matchId: String,
    @SerialName("profile_id") val profileId: String,
    @SerialName("body") val body: String,
    @SerialName("moderation_status") val moderationStatus: String = "visible",
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("anonymous_profiles") val profile: ChatProfileDto? = null
)

@Serializable
data class ChatProfileDto(
    @SerialName("alias") val alias: String? = null,
    @SerialName("avatar_seed") val avatarSeed: String? = null
)

@Serializable
data class ChatReportDto(
    @SerialName("chat_message_id") val chatMessageId: String,
    @SerialName("reporter_profile_id") val reporterProfileId: String,
    @SerialName("reported_profile_id") val reportedProfileId: String? = null,
    @SerialName("reason") val reason: String = "harmful_content",
    @SerialName("details") val details: String = "Reported from Android anonymous chat."
)
