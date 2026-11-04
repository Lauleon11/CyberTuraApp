package me.lauraleon.cybertura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import me.lauraleon.cybertura.data.local.entity.PlayerProfileEntity

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player_profile WHERE id = 1")
    fun observePlayer(): Flow<PlayerProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlayer(profile: PlayerProfileEntity)

    @Query("DELETE FROM player_profile")
    suspend fun deleteAll()
}
