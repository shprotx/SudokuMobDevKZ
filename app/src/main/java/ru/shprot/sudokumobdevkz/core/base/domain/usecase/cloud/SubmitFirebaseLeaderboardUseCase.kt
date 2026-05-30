package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.remote.GameContextDto
import ru.shprot.sudokumobdevkz.core.base.data.remote.LeaderboardCfApiHolder
import ru.shprot.sudokumobdevkz.core.base.data.remote.LeaderboardSubmitDto
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.StableIdProvider
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import javax.inject.Inject

class SubmitFirebaseLeaderboardUseCase @Inject constructor(
    private val cfApiHolder: LeaderboardCfApiHolder,
    private val stableIdProvider: StableIdProvider,
    private val cloud: CloudGameServices,
    private val settingsRepository: SettingsRepository,
) {

    suspend operator fun invoke(
        scoreDelta: Long,
        difficulty: Difficulty,
        timeSeconds: Int,
        errors: Int,
        hintsUsed: Int,
        isDaily: Boolean,
    ) {
        val api = cfApiHolder.value ?: return
        if (scoreDelta <= 0L) return

        val showName = settingsRepository.currentSettings.showNameOnLeaderboard
        val signedIn = cloud.signInState.value

        val displayName = if (showName && signedIn is SignInState.SignedIn) {
            signedIn.displayName.take(MAX_NAME_LENGTH).ifBlank { ANONYMOUS }
        } else {
            ANONYMOUS
        }
        val avatarUrl = if (showName && signedIn is SignInState.SignedIn) {
            signedIn.avatarUrl
        } else {
            null
        }

        val body = LeaderboardSubmitDto(
            stableId = stableIdProvider.current(),
            platform = PLATFORM,
            displayName = displayName,
            avatarUrl = avatarUrl,
            scoreDelta = scoreDelta,
            gameContext = GameContextDto(
                difficulty = difficulty.firebaseKey,
                timeSeconds = timeSeconds,
                errors = errors,
                hintsUsed = hintsUsed,
                isDaily = isDaily,
            ),
        )

        runCatching { api.submit(body) }
    }

    private companion object {
        const val PLATFORM = "android"
        const val ANONYMOUS = "Anonymous"
        const val MAX_NAME_LENGTH = 32
    }
}