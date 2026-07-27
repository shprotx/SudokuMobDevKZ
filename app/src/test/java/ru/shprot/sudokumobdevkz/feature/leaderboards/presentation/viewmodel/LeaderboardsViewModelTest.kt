package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.viewmodel

import android.app.Activity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardData
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardRow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.PlayerScore
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInResult
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.repository.ILeaderboardRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.AppSettings
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.ToggleShowNameOnLeaderboardUseCase
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.UpdateLeaderboardIdentityUseCase
import ru.shprot.sudokumobdevkz.core.base.presentation.snackbar.SnackbarManager
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEffect
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEvent

@OptIn(ExperimentalCoroutinesApi::class)
class LeaderboardsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var cloud: FakeCloudGameServices
    private lateinit var leaderboardRepository: FakeLeaderboardRepository
    private lateinit var settingsRepository: FakeSettingsRepository
    private lateinit var updateLeaderboardIdentity: FakeUpdateLeaderboardIdentityUseCase
    private lateinit var toggleShowNameOnLeaderboard: FakeToggleShowNameOnLeaderboardUseCase
    private lateinit var viewModel: LeaderboardsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        cloud = FakeCloudGameServices()
        leaderboardRepository = FakeLeaderboardRepository()
        settingsRepository = FakeSettingsRepository()
        updateLeaderboardIdentity = FakeUpdateLeaderboardIdentityUseCase()
        toggleShowNameOnLeaderboard = FakeToggleShowNameOnLeaderboardUseCase(settingsRepository)
        viewModel = LeaderboardsViewModel(
            cloud = cloud,
            leaderboardRepository = leaderboardRepository,
            settingsRepository = settingsRepository,
            updateLeaderboardIdentity = updateLeaderboardIdentity,
            toggleShowNameOnLeaderboard = toggleShowNameOnLeaderboard,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ToggleShowName when signed in invokes shared use case`() = runTest {
        cloud.setSignedIn()
        advanceUntilIdle()

        viewModel.setEvent(LeaderboardsUIEvent.ToggleShowName)
        advanceUntilIdle()

        assertEquals(1, toggleShowNameOnLeaderboard.invokeCount)
    }

    @Test
    fun `ToggleShowName when signed out navigates to settings and shows snackbar`() = runTest {
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isSignedIn)

        val effects = mutableListOf<LeaderboardsUIEffect>()
        val effectJob = launch { viewModel.effect.collect { effects.add(it) } }
        val snackbarMessages = mutableListOf<Int>()
        val snackbarJob = launch {
            SnackbarManager.messages.collect { snackbarMessages.add(it.messageRes) }
        }

        viewModel.setEvent(LeaderboardsUIEvent.ToggleShowName)
        advanceUntilIdle()

        assertTrue(effects.contains(LeaderboardsUIEffect.NavigateToSettings))
        assertTrue(snackbarMessages.contains(R.string.leaderboard_show_name_sign_in_required))
        assertEquals(0, toggleShowNameOnLeaderboard.invokeCount)

        effectJob.cancel()
        snackbarJob.cancel()
    }

    @Test
    fun `state reflects showNameOnLeaderboard from settings`() = runTest {
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.showNameOnLeaderboard)

        settingsRepository.update { copy(showNameOnLeaderboard = true) }
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.showNameOnLeaderboard)
    }
}

private class FakeCloudGameServices : CloudGameServices {
    override val isAvailable: Boolean = true
    private val _signInState = MutableStateFlow<SignInState>(SignInState.SignedOut)
    override val signInState: StateFlow<SignInState> = _signInState.asStateFlow()

    fun setSignedIn() {
        _signInState.value = SignInState.SignedIn("player-1", "Player", null)
    }

    override fun attachActivity(activity: Activity) = Unit
    override fun detachActivity() = Unit
    override suspend fun trySilentSignIn(): SignInResult = SignInResult.Success
    override suspend fun requestSignIn(): SignInResult = SignInResult.Success
    override suspend fun signOut() = Unit
    override suspend fun unlockAchievement(pgsId: String) = Unit
    override suspend fun incrementAchievement(pgsId: String, steps: Int) = Unit
    override suspend fun submitScore(leaderboardId: String, score: Long) = Unit
    override suspend fun loadTopScores(leaderboardId: String, limit: Int): List<LeaderboardRow> = emptyList()
    override suspend fun loadPlayerScore(leaderboardId: String): PlayerScore? = null
    override suspend fun readSnapshot(name: String): ByteArray? = null
    override suspend fun writeSnapshot(name: String, bytes: ByteArray, description: String) = Unit
}

private class FakeLeaderboardRepository : ILeaderboardRepository {
    private val _data = MutableStateFlow<LeaderboardData?>(null)
    override val data: StateFlow<LeaderboardData?> = _data.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    var refreshCount = 0

    override fun refresh() {
        refreshCount++
    }

    override fun clear() {
        _data.value = null
    }
}

private class FakeSettingsRepository : ISettingsRepository {
    private var _settings = MutableStateFlow(AppSettings())
    override val settings: Flow<AppSettings> = _settings.asStateFlow()
    override val currentSettings: AppSettings get() = _settings.value

    override fun update(transform: AppSettings.() -> AppSettings) {
        _settings.value = _settings.value.transform()
    }

    override fun isLeaderboardNamePromptShown(): Flow<Boolean> = flowOf(true)

    override fun markLeaderboardNamePromptShown() = Unit
}

private class FakeUpdateLeaderboardIdentityUseCase : UpdateLeaderboardIdentityUseCase {
    var invokeCount = 0
    var lastShowName: Boolean? = null

    override suspend fun invoke(showName: Boolean) {
        invokeCount++
        lastShowName = showName
    }
}

private class FakeToggleShowNameOnLeaderboardUseCase(
    private val settingsRepository: FakeSettingsRepository,
) : ToggleShowNameOnLeaderboardUseCase {
    var invokeCount = 0

    override suspend fun invoke() {
        invokeCount++
        val newValue = !settingsRepository.currentSettings.showNameOnLeaderboard
        settingsRepository.update { copy(showNameOnLeaderboard = newValue) }
    }
}