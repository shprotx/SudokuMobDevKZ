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

        AchievementIconKey.TROPHY_SILVER -> AchievementVisual(
            gradientStart = Color(0xFFC9D6E6),
            gradientEnd = Color(0xFF54627A),
            accentColor = Color(0xFFFFFFFF),
        )

        AchievementIconKey.TROPHY_GOLD -> AchievementVisual(
            gradientStart = Color(0xFFFFE25B),
            gradientEnd = Color(0xFFB4560A),
            accentColor = Color(0xFFFFFCDC),
        )

        AchievementIconKey.TROPHY_PLATINUM -> AchievementVisual(
            gradientStart = Color(0xFF9FE8FF),
            gradientEnd = Color(0xFF2B3D8F),
            accentColor = Color(0xFFFFFFFF),
        )

        AchievementIconKey.DIAMOND_PINK -> AchievementVisual(
            gradientStart = Color(0xFFFFB8E0),
            gradientEnd = Color(0xFF8F1666),
            accentColor = Color(0xFFFFFFFF),
        )

        AchievementIconKey.DIAMOND_RING -> AchievementVisual(
            gradientStart = Color(0xFFE8D8FF),
            gradientEnd = Color(0xFF5A2B9E),
            accentColor = Color(0xFFFFF0FF),
        )

        AchievementIconKey.FIRE_BLUE -> AchievementVisual(
            gradientStart = Color(0xFF6BD5FF),
            gradientEnd = Color(0xFF0B1E66),
            accentColor = Color(0xFFB0F0FF),
        )

        AchievementIconKey.CALENDAR_FORTNIGHT -> AchievementVisual(
            gradientStart = Color(0xFF8FD8B0),
            gradientEnd = Color(0xFF0E5E52),
            accentColor = Color(0xFFDFFFEE),
        )

        AchievementIconKey.CALENDAR_STACK -> AchievementVisual(
            gradientStart = Color(0xFF9CC8FF),
            gradientEnd = Color(0xFF1E3E8F),
            accentColor = Color(0xFFE0EEFF),
        )

        AchievementIconKey.HOURGLASS_GOLD -> AchievementVisual(
            gradientStart = Color(0xFFFFD98A),
            gradientEnd = Color(0xFF7A3A9E),
            accentColor = Color(0xFFFFF3C8),
        )

        AchievementIconKey.GLOBE -> AchievementVisual(
            gradientStart = Color(0xFF8FE0DC),
            gradientEnd = Color(0xFF0E4C7C),
            accentColor = Color(0xFFE0FAFF),
        )

        AchievementIconKey.STOPWATCH -> AchievementVisual(
            gradientStart = Color(0xFFFFC76B),
            gradientEnd = Color(0xFF8F3A0E),
            accentColor = Color(0xFFFFF1A8),
        )

        AchievementIconKey.COMET_GREEN -> AchievementVisual(
            gradientStart = Color(0xFFA8F0B8),
            gradientEnd = Color(0xFF0E6E3C),
            accentColor = Color(0xFFE0FFE8),
        )

        AchievementIconKey.COMET_ORANGE -> AchievementVisual(
            gradientStart = Color(0xFFFFC98A),
            gradientEnd = Color(0xFFA33A0E),
            accentColor = Color(0xFFFFEAD0),
        )

        AchievementIconKey.COMET_PURPLE -> AchievementVisual(
            gradientStart = Color(0xFFD0A8FF),
            gradientEnd = Color(0xFF3E0E7C),
            accentColor = Color(0xFFF0E0FF),
        )

        AchievementIconKey.OWL -> AchievementVisual(
            gradientStart = Color(0xFFC8B89A),
            gradientEnd = Color(0xFF4A3520),
            accentColor = Color(0xFFFFE9B8),
        )

        AchievementIconKey.SHIELD_CRACKED -> AchievementVisual(
            gradientStart = Color(0xFFFF9A6B),
            gradientEnd = Color(0xFF7C0E2E),
            accentColor = Color(0xFFFFD9C0),
        )
    }
}
