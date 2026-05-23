package ru.shprot.sudokumobdevkz.feature.settings.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CloudProgress

sealed interface CloudImportState {

    data object Idle : CloudImportState

    data object Loading : CloudImportState

    data class Choosing(
        val local: CloudProgress,
        val cloud: CloudProgress,
    ) : CloudImportState

    data object Applying : CloudImportState
}
