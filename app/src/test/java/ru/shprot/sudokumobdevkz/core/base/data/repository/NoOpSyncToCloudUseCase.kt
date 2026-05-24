package ru.shprot.sudokumobdevkz.core.base.data.repository

import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.SyncToCloudUseCase

internal object NoOpSyncToCloudUseCase : SyncToCloudUseCase {
    override fun trigger() = Unit
    override suspend fun syncNow() = Unit
    override suspend fun observeAndSync() = Unit
    override fun beginImport() = Unit
    override fun endImport() = Unit
}
