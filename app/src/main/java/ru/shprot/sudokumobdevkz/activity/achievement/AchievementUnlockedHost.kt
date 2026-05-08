package ru.shprot.sudokumobdevkz.activity.achievement

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.repository.AchievementsRepository
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.AchievementUnlockedSnackbar
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.navigation.AchievementsRoutes
import javax.inject.Inject

@Composable
fun AchievementUnlockedHost(
    modifier: Modifier,
    navController: NavHostController,
    content: @Composable () -> Unit,
) {
    val viewModel: AchievementUnlockedHostViewModel = hiltViewModel()
    var visibleMessage by remember { mutableStateOf<String?>(null) }
    var actionConsumed by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.messages.consumeAsFlow().collect { message ->
            visibleMessage = message
            actionConsumed = false
            delay(SNACKBAR_DURATION_MS)
            visibleMessage = null
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        content()

        visibleMessage?.let { message ->
            AchievementUnlockedSnackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(AppTheme.paddings.large),
                message = message,
                onAction = {
                    if (!actionConsumed) {
                        actionConsumed = true
                        visibleMessage = null
                        navController.navigate(AchievementsRoutes.AchievementsScreen) {
                            launchSingleTop = true
                        }
                    }
                },
            )
        }
    }
}

@HiltViewModel
class AchievementUnlockedHostViewModel @Inject constructor(
    private val achievementsRepository: AchievementsRepository,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {

    val messages: Channel<String> = Channel(capacity = 16)

    init {
        achievementsRepository.newlyUnlocked
            .onEach { event ->
                val title = appContext.getString(event.achievement.titleRes)
                val message = appContext.getString(R.string.achievement_unlocked_toast, title)
                messages.send(message)
            }
            .launchIn(viewModelScope)

        achievementsRepository.retroactiveBatch
            .onEach { count ->
                val message = appContext.getString(R.string.achievements_retroactive_toast, count)
                messages.send(message)
            }
            .launchIn(viewModelScope)
    }
}

private const val SNACKBAR_DURATION_MS = 4_000L