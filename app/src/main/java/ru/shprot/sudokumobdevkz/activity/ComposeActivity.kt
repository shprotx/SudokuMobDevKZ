package ru.shprot.sudokumobdevkz.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.IThemeRepository
import ru.shprot.sudokumobdevkz.core.base.presentation.navigation.NavRoute
import ru.shprot.sudokumobdevkz.core.theme.AppColors
import ru.shprot.sudokumobdevkz.core.theme.SudokuTheme
import ru.shprot.sudokumobdevkz.core.uicommon.snackbar.AppSnackbarHost
import ru.shprot.sudokumobdevkz.feature.menu.presentation.navigation.MenuRoutes
import ru.shprot.sudokumobdevkz.feature.splash.presentation.navigation.SplashRoutes
import javax.inject.Inject

@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var cloudGameServices: CloudGameServices

    @Inject
    lateinit var themeRepository: IThemeRepository

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
        cloudGameServices.attachActivity(this)
        lifecycleScope.launch {
            cloudGameServices.trySilentSignIn()
        }
        lifecycleScope.launch {
            themeRepository.seedBuiltIns()
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
        }
        setContent {
            val settings by settingsRepository.settings.collectAsStateWithLifecycle(
                initialValue = settingsRepository.currentSettings,
            )

            val isSystemDark = isSystemInDarkTheme()
            val resolvedColors by themeRepository.resolveColors(settings.themeModeId, isSystemDark)
                .collectAsStateWithLifecycle(initialValue = AppColors.LightColors)
            val isDark = resolvedColors == AppColors.DarkColors ||
                (resolvedColors != AppColors.LightColors && isBackgroundDark(resolvedColors.background))

            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    val controller = WindowCompat.getInsetsController(window, view)
                    controller.isAppearanceLightStatusBars = !isDark
                    controller.isAppearanceLightNavigationBars = !isDark
                }
            }

            SudokuTheme(colors = resolvedColors) {
                val navController = rememberNavController()
                val startDestination: NavRoute = if (isAndroid12Plus) {
                    MenuRoutes.MenuScreen
                } else {
                    SplashRoutes.SplashScreen
                }
                Box(modifier = Modifier.fillMaxSize()) {
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
                    AppSnackbarHost(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding(),
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        cloudGameServices.detachActivity()
        super.onDestroy()
    }

    private companion object {
        const val SPLASH_MIN_DURATION_MS = 1300L

        fun isBackgroundDark(color: Color): Boolean {
            val r = color.red * 0.2126f
            val g = color.green * 0.7152f
            val b = color.blue * 0.0722f
            return (r + g + b) < 0.35f
        }
    }
}