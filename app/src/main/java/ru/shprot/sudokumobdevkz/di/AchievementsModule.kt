package ru.shprot.sudokumobdevkz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.shprot.sudokumobdevkz.core.base.data.repository.AchievementsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.AchievementsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AchievementsModule {

    @Binds
    @Singleton
    abstract fun bindAchievementsRepository(impl: AchievementsRepositoryImpl): AchievementsRepository
}