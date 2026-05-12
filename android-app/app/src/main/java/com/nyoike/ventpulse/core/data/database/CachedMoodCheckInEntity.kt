package com.nyoike.ventpulse.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_mood_checkins")
data class CachedMoodCheckInEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val communityId: String,
    val mood: String,
    val intensity: Int,
    val createdAtMillis: Long,
    val synced: Boolean
)
