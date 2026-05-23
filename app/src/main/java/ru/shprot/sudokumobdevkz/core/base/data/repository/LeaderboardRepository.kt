package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardData
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.LoadLeaderboardUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepository @Inject constructor(
    private val cloud: CloudGameServices,
    private val loadLeaderboard: LoadLeaderboardUseCase,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _data = MutableStateFlow<LeaderboardData?>(null)
    val data: StateFlow<LeaderboardData?> = _data.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var refreshJob: Job? = null

    fun refresh() {
        refreshJob?.cancel()
        refreshJob = scope.launch {
            if (!cloud.isAvailable || cloud.signInState.value !is SignInState.SignedIn) {
                _data.value = null
                return@launch
            }
            _isLoading.value = true
            runCatching { loadLeaderboard() }
                .onSuccess { _data.value = it }
            _isLoading.value = false
        }
    }

    fun clear() {
        refreshJob?.cancel()
        _data.value = null
        _isLoading.value = false
    }
}
