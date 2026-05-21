package ru.shprot.sudokumobdevkz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.NoOpCloudGameServices
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CloudGameServicesModule {

    @Binds
    @Singleton
    abstract fun bindCloudGameServices(impl: NoOpCloudGameServices): CloudGameServices
}
