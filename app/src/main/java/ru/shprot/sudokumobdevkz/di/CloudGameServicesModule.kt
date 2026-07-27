package ru.shprot.sudokumobdevkz.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.NoOpCloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.PlayGamesCloudServices
import ru.shprot.sudokumobdevkz.core.base.data.util.GmsAvailability
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.SyncToCloudUseCase
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.SyncToCloudUseCaseImpl
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.ToggleShowNameOnLeaderboardUseCase
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.ToggleShowNameOnLeaderboardUseCaseImpl
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.UpdateLeaderboardIdentityUseCase
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.UpdateLeaderboardIdentityUseCaseImpl
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudGameServicesModule {

    @Provides
    @Singleton
    fun provideCloudGameServices(
        gmsAvailability: GmsAvailability,
        playGames: Provider<PlayGamesCloudServices>,
        noOp: Provider<NoOpCloudGameServices>,
    ): CloudGameServices =
        if (gmsAvailability.hasGms) playGames.get() else noOp.get()

    @Provides
    @Singleton
    fun provideSyncToCloudUseCase(impl: SyncToCloudUseCaseImpl): SyncToCloudUseCase = impl

    @Provides
    @Singleton
    fun provideToggleShowNameOnLeaderboardUseCase(
        impl: ToggleShowNameOnLeaderboardUseCaseImpl,
    ): ToggleShowNameOnLeaderboardUseCase = impl

    @Provides
    @Singleton
    fun provideUpdateLeaderboardIdentityUseCase(
        impl: UpdateLeaderboardIdentityUseCaseImpl,
    ): UpdateLeaderboardIdentityUseCase = impl
}
