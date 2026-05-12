package com.nyoike.ventpulse.feature.coreflow.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommunityDto(
    @SerialName("id") val id: String,
    @SerialName("parent_id") val parentId: String? = null,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String,
    @SerialName("path") val path: String,
    @SerialName("level") val level: String,
    @SerialName("sort_order") val sortOrder: Int = 0
)

@Serializable
data class AnonymousProfileDto(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("alias") val alias: String,
    @SerialName("avatar_seed") val avatarSeed: String,
    @SerialName("community_id") val communityId: String?
)

@Serializable
data class MoodCheckInDto(
    @SerialName("profile_id") val profileId: String,
    @SerialName("community_id") val communityId: String,
    @SerialName("mood") val mood: String,
    @SerialName("intensity") val intensity: Int
)

@Serializable
data class VentDto(
    @SerialName("profile_id") val profileId: String,
    @SerialName("community_id") val communityId: String,
    @SerialName("mood") val mood: String,
    @SerialName("body") val body: String
)
