package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import ru.shprot.sudokumobdevkz.core.base.data.util.safeRunCatching
import javax.inject.Inject

class AutoImportSnapshotUseCase @Inject constructor(
    private val importFromCloud: ImportFromCloudUseCase,
) {

    suspend operator fun invoke(): Result = safeRunCatching {
        val cloudProgress = importFromCloud.loadCloudSnapshot()
            ?: return@safeRunCatching Result.NO_CLOUD_SNAPSHOT
        if (ImportFromCloudUseCase.isEmpty(cloudProgress)) {
            return@safeRunCatching Result.CLOUD_EMPTY
        }
        val localProgress = importFromCloud.currentLocalProgress()
        if (!ImportFromCloudUseCase.isEmpty(localProgress)) {
            return@safeRunCatching Result.LOCAL_NOT_EMPTY
        }
        importFromCloud.applyProgress(cloudProgress)
        Result.IMPORTED
    }.getOrElse { Result.ERROR }

    enum class Result {
        IMPORTED,
        NO_CLOUD_SNAPSHOT,
        CLOUD_EMPTY,
        LOCAL_NOT_EMPTY,
        ERROR,
    }
}
