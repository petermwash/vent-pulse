package com.nyoike.ventpulse.core.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityDao {
    @Query("select * from cached_communities order by path asc, sortOrder asc")
    fun observeCommunities(): Flow<List<CachedCommunityEntity>>

    @Upsert
    suspend fun upsertAll(communities: List<CachedCommunityEntity>)
}
