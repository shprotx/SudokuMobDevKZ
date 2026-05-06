package ru.shprot.sudokumobdevkz.feature.achievements.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.shprot.sudokumobdevkz.core.base.data.repository.AchievementsRepository
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementState
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.contract.AchievementsUIEffect
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.contract.AchievementsUIEvent
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.contract.AchievementsUIState
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val achievementsRepository: AchievementsRepository,
) : BaseViewModel<AchievementsUIEvent, AchievementsUIState, AchievementsUIEffect>(
    initialState = AchievementsUIState(),
) {

    init {
        observeAchievements()
    }

    override fun handleUIEvent(event: AchievementsUIEvent) =
        when (event) {
            AchievementsUIEvent.BackClicked ->
                setEffect(AchievementsUIEffect.NavigateBack)
        }

    private fun observeAchievements() {
        achievementsRepository.achievementsState
            .onEach { items -> updateState { fromItems(items) } }
            .launchIn(viewModelScope)
    }

    private fun AchievementsUIState.fromItems(items: List<AchievementState>): AchievementsUIState {
        val unlocked = items.filter { it.unlockedAt != null }
            .sortedByDescending { it.unlockedAt }
        val inProgress = items.filter {
            it.unlockedAt == null && !it.achievement.hidden && it.progress.current > 0
        }.sortedByDescending { it.progress.ratio }
        val locked = items.filter {
            it.unlockedAt == null && (it.achievement.hidden || it.progress.current == 0)
        }.sortedBy { it.achievement.hidden }
        return copy(
            isLoading = false,
            totalUnlocked = unlocked.size,
            totalCount = items.size,
            unlocked = unlocked,
            inProgress = inProgress,
            locked = locked,
        )
    }
}