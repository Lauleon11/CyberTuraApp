package me.lauraleon.cybertura.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.lauraleon.cybertura.data.local.dao.PlayerDao
import me.lauraleon.cybertura.data.local.dao.WorldProgressDao
import me.lauraleon.cybertura.data.local.entity.PlayerProfileEntity
import me.lauraleon.cybertura.data.local.entity.WorldProgressEntity

@Database(
    entities = [PlayerProfileEntity::class, WorldProgressEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CyberturaDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun worldProgressDao(): WorldProgressDao

    companion object {
        const val NAME = "cybertura.db"

        fun build(context: Context): CyberturaDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                CyberturaDatabase::class.java,
                NAME
            ).build()
    }
}
