package com.nyoike.ventpulse.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_communities")
data class CachedCommunityEntity(
    @PrimaryKey val id: String,
    val parentId: String?,
    val name: String,
    val slug: String,
    val path: String,
    val level: String,
    val sortOrder: Int
)
