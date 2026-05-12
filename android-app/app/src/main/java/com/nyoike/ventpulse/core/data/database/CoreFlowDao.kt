package com.nyoike.ventpulse.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface CoreFlowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoodCheckIn(checkIn: CachedMoodCheckInEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVent(vent: CachedVentEntity)
}
