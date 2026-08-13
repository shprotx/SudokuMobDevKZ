package ru.shprot.sudokumobdevkz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.shprot.sudokumobdevkz.core.base.data.notification.AndroidNotificationPermissionChecker
import ru.shprot.sudokumobdevkz.core.base.data.notification.AndroidNotificationWorkGateway
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationPermissionChecker
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationScheduler
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationWorkGateway
import ru.shprot.sudokumobdevkz.core.base.data.notification.WorkManagerNotificationScheduler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindNotificationScheduler(impl: WorkManagerNotificationScheduler): NotificationScheduler

    @Binds
    @Singleton
    abstract fun bindNotificationWorkGateway(impl: AndroidNotificationWorkGateway): NotificationWorkGateway

    @Binds
    @Singleton
    abstract fun bindNotificationPermissionChecker(
        impl: AndroidNotificationPermissionChecker,
    ): NotificationPermissionChecker
}
