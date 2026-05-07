package ru.shprot.sudokumobdevkz.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.activity.achievement.AchievementUnlockedHost
import ru.shprot.sudokumobdevkz.activity.navigation.SudokuNavHost
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute
import ru.shprot.sudokumobdevkz.core.theme.SudokuTheme
import ru.shprot.sudokumobdevkz.feature.menu.presentation.navigation.MenuRoutes
import ru.shprot.sudokumobdevkz.feature.splash.presentation.navigation.SplashRoutes
import javax.inject.Inject

@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        val isAndroid12Plus = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        if (isAndroid12Plus) {
            val splash = installSplashScreen()
            var keepShown = true
            splash.setKeepOnScreenCondition { keepShown }
            lifecycleScope.launch {
                delay(SPLASH_MIN_DURATION_MS)
                keepShown = false
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ),
        )
        setContent {
            val settings by settingsRepository.settings.collectAsStateWithLifecycle(
                initialValue = settingsRepository.currentSettings,
            )

            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    val controller = WindowCompat.getInsetsController(window, view)
                    controller.isAppearanceLightStatusBars = !settings.isDarkTheme
                    controller.isAppearanceLightNavigationBars = !settings.isDarkTheme
                }
            }

            SudokuTheme(darkTheme = settings.isDarkTheme) {
                val navController = rememberNavController()
                val startDestination: NavRoute = if (isAndroid12Plus) {
                    MenuRoutes.MenuScreen
                } else {
                    SplashRoutes.SplashScreen
                }
                AchievementUnlockedHost(
                    modifier = Modifier,
                    navController = navController,
                    content = {
                        SudokuNavHost(
                            navController = navController,
                            startDestination = startDestination,
                        )
                    },
                )
            }
        }
    }

    private companion object {
        const val SPLASH_MIN_DURATION_MS = 1300L
    }
}