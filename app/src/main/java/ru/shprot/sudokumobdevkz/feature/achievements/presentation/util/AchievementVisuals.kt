package ru.shprot.sudokumobdevkz.feature.achievements.presentation.util

import androidx.compose.ui.graphics.Color
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementIconKey

object AchievementVisuals {

    fun resolve(iconKey: AchievementIconKey): AchievementVisual = when (iconKey) {
        AchievementIconKey.TROPHY_BRONZE -> AchievementVisual(
            gradientStart = Color(0xFFFFD86B),
            gradientEnd = Color(0xFFFF9500),
            accentColor = Color(0xFFFFF1A8),
        )

        AchievementIconKey.MEDAL_BRONZE -> AchievementVisual(
            gradientStart = Color(0xFFE9A66B),
            gradientEnd = Color(0xFF9A5A2A),
            accentColor = Color(0xFFFFD3A6),
        )

        AchievementIconKey.MEDAL_SILVER -> AchievementVisual(
            gradientStart = Color(0xFFE6EAF2),
            gradientEnd = Color(0xFF8D9AA8),
            accentColor = Color(0xFFFFFFFF),
        )

        AchievementIconKey.MEDAL_GOLD -> AchievementVisual(
            gradientStart = Color(0xFFFFE25B),
            gradientEnd = Color(0xFFFF9E00),
            accentColor = Color(0xFFFFFCDC),
        )

        AchievementIconKey.CROWN -> AchievementVisual(
            gradientStart = Color(0xFFFFD24A),
            gradientEnd = Color(0xFF8E2BC0),
            accentColor = Color(0xFFFFE066),
        )

        AchievementIconKey.LEAF -> AchievementVisual(
            gradientStart = Color(0xFFA8E66E),
            gradientEnd = Color(0xFF1F8A3F),
            accentColor = Color(0xFFD9F7B5),
        )

        AchievementIconKey.SUN -> AchievementVisual(
            gradientStart = Color(0xFFFFE066),
            gradientEnd = Color(0xFFFF6A00),
            accentColor = Color(0xFFFFF6B0),
        )

        AchievementIconKey.MOUNTAIN -> AchievementVisual(
            gradientStart = Color(0xFF8FB7E6),
            gradientEnd = Color(0xFF3D2C8E),
            accentColor = Color(0xFFFFFFFF),
        )

        AchievementIconKey.COMPASS -> AchievementVisual(
            gradientStart = Color(0xFF7CE5DC),
            gradientEnd = Color(0xFF0F7C9F),
            accentColor = Color(0xFFE0FAFF),
        )

        AchievementIconKey.CHECK -> AchievementVisual(
            gradientStart = Color(0xFFB6F19E),
            gradientEnd = Color(0xFF2BB36C),
            accentColor = Color(0xFFE6FFD9),
        )

        AchievementIconKey.TARGET -> AchievementVisual(
            gradientStart = Color(0xFFFF8B6B),
            gradientEnd = Color(0xFFB80044),
            accentColor = Color(0xFFFFE2D6),
        )

        AchievementIconKey.GEM -> AchievementVisual(
            gradientStart = Color(0xFFB8FFFC),
            gradientEnd = Color(0xFF1666FF),
            accentColor = Color(0xFFFFFFFF),
        )

        AchievementIconKey.BOLT -> AchievementVisual(
            gradientStart = Color(0xFFFFE96B),
            gradientEnd = Color(0xFFFF7A00),
            accentColor = Color(0xFFFFFCDC),
        )

        AchievementIconKey.BOLT_DOUBLE -> AchievementVisual(
            gradientStart = Color(0xFFFFAA47),
            gradientEnd = Color(0xFFD60039),
            accentColor = Color(0xFFFFE66B),
        )

        AchievementIconKey.BOLT_TRIPLE -> AchievementVisual(
            gradientStart = Color(0xFFFF5BA0),
            gradientEnd = Color(0xFF5A0090),
            accentColor = Color(0xFFFFE066),
        )

        AchievementIconKey.FIRE_SMALL -> AchievementVisual(
            gradientStart = Color(0xFFFFE066),
            gradientEnd = Color(0xFFE25400),
            accentColor = Color(0xFFFFF1A8),
        )

        AchievementIconKey.FIRE_MEDIUM -> AchievementVisual(
            gradientStart = Color(0xFFFF7A00),
            gradientEnd = Color(0xFFB80024),
            accentColor = Color(0xFFFFE066),
        )

        AchievementIconKey.FIRE_BIG -> AchievementVisual(
            gradientStart = Color(0xFFFF3D5A),
            gradientEnd = Color(0xFF3A0050),
            accentColor = Color(0xFFFFE066),
        )

        AchievementIconKey.CALENDAR_CHECK -> AchievementVisual(
            gradientStart = Color(0xFF7AC0FF),
            gradientEnd = Color(0xFF3D32B3),
            accentColor = Color(0xFFE0EBFF),
        )

        AchievementIconKey.CALENDAR_WEEK -> AchievementVisual(
            gradientStart = Color(0xFF7CE5C8),
            gradientEnd = Color(0xFF0E7C7B),
            accentColor = Color(0xFFE0FFF6),
        )

        AchievementIconKey.CALENDAR_MONTH -> AchievementVisual(
            gradientStart = Color(0xFFFF9DD9),
            gradientEnd = Color(0xFF7A1AB3),
            accentColor = Color(0xFFFFE0F4),
        )

        AchievementIconKey.CALENDAR_YEAR -> AchievementVisual(
            gradientStart = Color(0xFFFFE66B),
            gradientEnd = Color(0xFFC8005A),
            accentColor = Color(0xFFFFF6B0),
        )

        AchievementIconKey.MOON -> AchievementVisual(
            gradientStart = Color(0xFF4A4FB3),
            gradientEnd = Color(0xFF0B0036),
            accentColor = Color(0xFFFFE066),
        )

        AchievementIconKey.SUNRISE -> AchievementVisual(
            gradientStart = Color(0xFFFFC76B),
            gradientEnd = Color(0xFFE0306B),
            accentColor = Color(0xFFFFE7B0),
        )
    }
}
