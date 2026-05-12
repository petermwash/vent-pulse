package com.nyoike.ventpulse.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_vents")
data class CachedVentEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val communityId: String,
    val mood: String,
    val body: String,
    val createdAtMillis: Long,
    val synced: Boolean
)
