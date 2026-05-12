package com.nyoike.ventpulse.core.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CachedCommunityEntity::class,
        CachedMoodCheckInEntity::class,
        CachedVentEntity::class
    ],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true
)
abstract class VentPulseDatabase : RoomDatabase() {
    abstract fun communityDao(): CommunityDao
    abstract fun coreFlowDao(): CoreFlowDao
}
