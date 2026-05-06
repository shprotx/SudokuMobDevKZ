package ru.shprot.sudokumobdevkz.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.shprot.sudokumobdevkz.activity.achievement.AchievementUnlockedHost
import ru.shprot.sudokumobdevkz.activity.navigation.SudokuNavHost
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.theme.SudokuTheme
import javax.inject.Inject

@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ),
        )
        setContent {
            val settings by settingsRepository.settings.collectAsStateWithLifecycle(
                initialValue = settingsRepository.currentSettings,
            )

            SudokuTheme(darkTheme = settings.isDarkTheme) {
                val navController = rememberNavController()
                AchievementUnlockedHost(
                    modifier = Modifier,
                    navController = navController,
                    content = {
                        SudokuNavHost(navController = navController)
                    },
                )
            }
        }
    }
}