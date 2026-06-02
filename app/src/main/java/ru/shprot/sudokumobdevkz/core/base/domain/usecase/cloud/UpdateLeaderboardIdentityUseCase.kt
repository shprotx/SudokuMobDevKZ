package ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud

import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.remote.LeaderboardCfApiHolder
import ru.shprot.sudokumobdevkz.core.base.data.remote.LeaderboardIdentityDto
import ru.shprot.sudokumobdevkz.core.base.data.repository.StableIdProvider
import javax.inject.Inject

class UpdateLeaderboardIdentityUseCase @Inject constructor(
    private val cfApiHolder: LeaderboardCfApiHolder,
    private val stableIdProvider: StableIdProvider,
    private val cloud: CloudGameServices,
) {

    suspend operator fun invoke(showName: Boolean) {
        val api = cfApiHolder.value ?: return
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
        val body = LeaderboardIdentityDto(
            stableId = stableIdProvider.current(),
            platform = PLATFORM,
            displayName = displayName,
            avatarUrl = avatarUrl,
        )
        runCatching { api.updateIdentity(body) }
    }

    private companion object {
        const val PLATFORM = "android"
        const val ANONYMOUS = "Anonymous"
        const val MAX_NAME_LENGTH = 32
    }
}
