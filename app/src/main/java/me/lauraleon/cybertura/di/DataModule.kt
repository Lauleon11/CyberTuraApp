package me.lauraleon.cybertura.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.lauraleon.cybertura.data.local.CyberturaDatabase
import me.lauraleon.cybertura.data.local.dao.PlayerDao
import me.lauraleon.cybertura.data.local.dao.WorldProgressDao
import me.lauraleon.cybertura.data.remote.PlayerIdProvider
import me.lauraleon.cybertura.data.remote.SharedPreferencesPlayerIdStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CyberturaDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            CyberturaDatabase::class.java,
            CyberturaDatabase.NAME
        ).fallbackToDestructiveMigration(true).build()

    @Provides
    fun providePlayerDao(database: CyberturaDatabase): PlayerDao = database.playerDao()

    @Provides
    fun provideWorldProgressDao(database: CyberturaDatabase): WorldProgressDao =
        database.worldProgressDao()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance().apply {
        // Starts the client on launch so writes queued in an earlier session get sent right away.
        enableNetwork()
    }

    @Provides
    @Singleton
    fun providePlayerIdProvider(@ApplicationContext context: Context): PlayerIdProvider =
        PlayerIdProvider(SharedPreferencesPlayerIdStore(context))
}
