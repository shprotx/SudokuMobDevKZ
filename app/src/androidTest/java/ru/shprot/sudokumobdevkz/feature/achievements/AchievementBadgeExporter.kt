package ru.shprot.sudokumobdevkz.feature.achievements

import android.graphics.Bitmap
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementIconKey
import ru.shprot.sudokumobdevkz.core.theme.AppColors
import ru.shprot.sudokumobdevkz.core.theme.SudokuTheme
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.AchievementIcon
import java.io.File
import java.io.FileOutputStream

class AchievementBadgeExporter {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun exportAllBadges() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val outDir = File(context.getExternalFilesDir(null), "badge-export")
        outDir.mkdirs()

        var currentKey by mutableStateOf(AchievementIconKey.entries.first())

        composeRule.setContent {
            SudokuTheme(colors = AppColors.LightColors) {
                CompositionLocalProvider(LocalDensity provides Density(1f)) {
                    AchievementIcon(
                        modifier = Modifier,
                        iconKey = currentKey,
                        size = EXPORT_SIZE.dp,
                    )
                }
            }
        }

        AchievementIconKey.entries.forEach { key ->
            currentKey = key
            composeRule.waitForIdle()
            val bitmap: Bitmap = composeRule.onRoot().captureToImage().asAndroidBitmap()
            val file = File(outDir, "${key.name.lowercase()}.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        }
    }

    private companion object {
        const val EXPORT_SIZE = 512
    }
}