package ru.shprot.sudokumobdevkz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.shprot.sudokumobdevkz.core.base.data.repository.ILeaderboardRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.IThemeRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.LeaderboardRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ThemeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindingsModule {

    @Binds
    @Singleton
    abstract fun bindThemeRepository(impl: ThemeRepository): IThemeRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepository): ISettingsRepository

    @Binds
    @Singleton
    abstract fun bindLeaderboardRepository(impl: LeaderboardRepository): ILeaderboardRepository
}