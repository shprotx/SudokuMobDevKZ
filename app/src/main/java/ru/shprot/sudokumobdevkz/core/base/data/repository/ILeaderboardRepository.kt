package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.flow.StateFlow
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardData

interface ILeaderboardRepository {
    val data: StateFlow<LeaderboardData?>
    val isLoading: StateFlow<Boolean>
    fun refresh()
    fun clear()
}