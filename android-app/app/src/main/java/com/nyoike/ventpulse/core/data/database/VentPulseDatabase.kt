package com.nyoike.ventpulse.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CachedCommunityEntity::class],
    version = 1,
    exportSchema = true
)
abstract class VentPulseDatabase : RoomDatabase() {
    abstract fun communityDao(): CommunityDao
}
