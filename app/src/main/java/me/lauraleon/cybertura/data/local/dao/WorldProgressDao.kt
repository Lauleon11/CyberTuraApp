package me.lauraleon.cybertura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.lauraleon.cybertura.data.local.entity.WorldProgressEntity

@Dao
interface WorldProgressDao {
    @Query("SELECT * FROM world_progress")
    fun observeAll(): Flow<List<WorldProgressEntity>>

    @Query("SELECT * FROM world_progress WHERE worldId = :worldId")
    suspend fun getByWorld(worldId: Int): WorldProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: WorldProgressEntity)

    @Query("DELETE FROM world_progress")
    suspend fun deleteAll()
}
