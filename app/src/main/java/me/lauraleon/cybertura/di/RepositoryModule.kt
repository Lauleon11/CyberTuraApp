package me.lauraleon.cybertura.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.lauraleon.cybertura.data.repository.PlayerRepositoryImpl
import me.lauraleon.cybertura.data.repository.WorldRepositoryImpl
import me.lauraleon.cybertura.domain.repository.PlayerRepository
import me.lauraleon.cybertura.domain.repository.WorldRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlayerRepository(impl: PlayerRepositoryImpl): PlayerRepository

    @Binds
    @Singleton
    abstract fun bindWorldRepository(impl: WorldRepositoryImpl): WorldRepository
}
