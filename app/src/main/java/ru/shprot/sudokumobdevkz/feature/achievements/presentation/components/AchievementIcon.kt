package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementIconKey
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge.drawRichBadgeGloss
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge.drawRichBadgeObject
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge.drawRichBadgeRing
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge.drawRichBadgeScene
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.util.AchievementVisuals

@Composable
internal fun AchievementIcon(
    modifier: Modifier,
    iconKey: AchievementIconKey,
    size: Dp = AppTheme.sizes.iconXL,
) {
    val visual = AchievementVisuals.resolve(iconKey)

    Box(modifier = modifier.size(size)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRichBadgeScene(visual)
            drawRichBadgeObject(iconKey, visual)
            drawRichBadgeGloss()
            drawRichBadgeRing()
        }
    }
}