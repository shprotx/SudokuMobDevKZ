package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import javax.inject.Inject

interface ToggleShowNameOnLeaderboardUseCase {
    suspend operator fun invoke()
}

class ToggleShowNameOnLeaderboardUseCaseImpl @Inject constructor(
    private val settingsRepository: ISettingsRepository,
    private val updateLeaderboardIdentity: UpdateLeaderboardIdentityUseCase,
) : ToggleShowNameOnLeaderboardUseCase {

    override suspend fun invoke() {
        val newValue = !settingsRepository.currentSettings.showNameOnLeaderboard
        settingsRepository.update { copy(showNameOnLeaderboard = newValue) }
        updateLeaderboardIdentity(showName = newValue)
    }
}