package ru.shprot.sudokumobdevkz.feature.achievements.presentation.util

import androidx.compose.ui.graphics.Color
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementIconKey

object AchievementVisuals {

    fun resolve(iconKey: AchievementIconKey): AchievementVisual = when (iconKey) {
        AchievementIconKey.TROPHY_BRONZE -> AchievementVisual(
            emoji = "🏆",
            gradientStart = Color(0xFFFFD86B),
            gradientEnd = Color(0xFFFF9500),
            accentColor = Color(0xFFFFF1A8),
            ringColor = Color(0xFFB76E00),
            decoration = AchievementDecoration.RAYS,
        )

        AchievementIconKey.MEDAL_BRONZE -> AchievementVisual(
            emoji = "🥉",
            gradientStart = Color(0xFFE9A66B),
            gradientEnd = Color(0xFF9A5A2A),
            accentColor = Color(0xFFFFD3A6),
            ringColor = Color(0xFF6E3C12),
            decoration = AchievementDecoration.SPARKLES,
        )

        AchievementIconKey.MEDAL_SILVER -> AchievementVisual(
            emoji = "🥈",
            gradientStart = Color(0xFFE6EAF2),
            gradientEnd = Color(0xFF8D9AA8),
            accentColor = Color(0xFFFFFFFF),
            ringColor = Color(0xFF5A6470),
            decoration = AchievementDecoration.SPARKLES,
        )

        AchievementIconKey.MEDAL_GOLD -> AchievementVisual(
            emoji = "🥇",
            gradientStart = Color(0xFFFFE25B),
            gradientEnd = Color(0xFFFF9E00),
            accentColor = Color(0xFFFFFCDC),
            ringColor = Color(0xFFB46C00),
            decoration = AchievementDecoration.RAYS,
        )

        AchievementIconKey.CROWN -> AchievementVisual(
            emoji = "👑",
            gradientStart = Color(0xFFFFD24A),
            gradientEnd = Color(0xFF8E2BC0),
            accentColor = Color(0xFFFFE066),
            ringColor = Color(0xFF59148A),
            decoration = AchievementDecoration.HALO,
        )

        AchievementIconKey.LEAF -> AchievementVisual(
            emoji = "🌿",
            gradientStart = Color(0xFFA8E66E),
            gradientEnd = Color(0xFF1F8A3F),
            accentColor = Color(0xFFD9F7B5),
            ringColor = Color(0xFF0E5520),
            decoration = AchievementDecoration.SPARKLES,
        )

        AchievementIconKey.SUN -> AchievementVisual(
            emoji = "☀️",
            gradientStart = Color(0xFFFFE066),
            gradientEnd = Color(0xFFFF6A00),
            accentColor = Color(0xFFFFF6B0),
            ringColor = Color(0xFFB23E00),
            decoration = AchievementDecoration.RAYS,
        )

        AchievementIconKey.MOUNTAIN -> AchievementVisual(
            emoji = "🏔️",
            gradientStart = Color(0xFF8FB7E6),
            gradientEnd = Color(0xFF3D2C8E),
            accentColor = Color(0xFFFFFFFF),
            ringColor = Color(0xFF221852),
            decoration = AchievementDecoration.SPARKLES,
        )

        AchievementIconKey.COMPASS -> AchievementVisual(
            emoji = "🧭",
            gradientStart = Color(0xFF7CE5DC),
            gradientEnd = Color(0xFF0F7C9F),
            accentColor = Color(0xFFE0FAFF),
            ringColor = Color(0xFF064760),
            decoration = AchievementDecoration.RAYS,
        )

        AchievementIconKey.CHECK -> AchievementVisual(
            emoji = "✅",
            gradientStart = Color(0xFFB6F19E),
            gradientEnd = Color(0xFF2BB36C),
            accentColor = Color(0xFFE6FFD9),
            ringColor = Color(0xFF136B3D),
            decoration = AchievementDecoration.SPARKLES,
        )

        AchievementIconKey.TARGET -> AchievementVisual(
            emoji = "🎯",
            gradientStart = Color(0xFFFF8B6B),
            gradientEnd = Color(0xFFB80044),
            accentColor = Color(0xFFFFE2D6),
            ringColor = Color(0xFF6E0024),
            decoration = AchievementDecoration.RAYS,
        )

        AchievementIconKey.GEM -> AchievementVisual(
            emoji = "💎",
            gradientStart = Color(0xFFB8FFFC),
            gradientEnd = Color(0xFF1666FF),
            accentColor = Color(0xFFFFFFFF),
            ringColor = Color(0xFF0D389E),
            decoration = AchievementDecoration.SPARKLES,
        )

        AchievementIconKey.BOLT -> AchievementVisual(
            emoji = "⚡",
            gradientStart = Color(0xFFFFE96B),
            gradientEnd = Color(0xFFFF7A00),
            accentColor = Color(0xFFFFFCDC),
            ringColor = Color(0xFFA64500),
            decoration = AchievementDecoration.BOLTS,
        )

        AchievementIconKey.BOLT_DOUBLE -> AchievementVisual(
            emoji = "⚡",
            gradientStart = Color(0xFFFFAA47),
            gradientEnd = Color(0xFFD60039),
            accentColor = Color(0xFFFFE66B),
            ringColor = Color(0xFF7A0023),
            decoration = AchievementDecoration.BOLTS,
        )

        AchievementIconKey.BOLT_TRIPLE -> AchievementVisual(
            emoji = "⚡",
            gradientStart = Color(0xFFFF5BA0),
            gradientEnd = Color(0xFF5A0090),
            accentColor = Color(0xFFFFE066),
            ringColor = Color(0xFF330056),
            decoration = AchievementDecoration.BOLTS,
        )

        AchievementIconKey.FIRE_SMALL -> AchievementVisual(
            emoji = "🔥",
            gradientStart = Color(0xFFFFE066),
            gradientEnd = Color(0xFFE25400),
            accentColor = Color(0xFFFFF1A8),
            ringColor = Color(0xFF8B2D00),
            decoration = AchievementDecoration.FLAMES,
        )

        AchievementIconKey.FIRE_MEDIUM -> AchievementVisual(
            emoji = "🔥",
            gradientStart = Color(0xFFFF7A00),
            gradientEnd = Color(0xFFB80024),
            accentColor = Color(0xFFFFE066),
            ringColor = Color(0xFF640014),
            decoration = AchievementDecoration.FLAMES,
        )

        AchievementIconKey.FIRE_BIG -> AchievementVisual(
            emoji = "🔥",
            gradientStart = Color(0xFFFF3D5A),
            gradientEnd = Color(0xFF3A0050),
            accentColor = Color(0xFFFFE066),
            ringColor = Color(0xFF1A0024),
            decoration = AchievementDecoration.FLAMES,
        )

        AchievementIconKey.CALENDAR_CHECK -> AchievementVisual(
            emoji = "📅",
            gradientStart = Color(0xFF7AC0FF),
            gradientEnd = Color(0xFF3D32B3),
            accentColor = Color(0xFFE0EBFF),
            ringColor = Color(0xFF1F1773),
            decoration = AchievementDecoration.SPARKLES,
        )

        AchievementIconKey.CALENDAR_WEEK -> AchievementVisual(
            emoji = "🗓️",
            gradientStart = Color(0xFF7CE5C8),
            gradientEnd = Color(0xFF0E7C7B),
            accentColor = Color(0xFFE0FFF6),
            ringColor = Color(0xFF064848),
            decoration = AchievementDecoration.SPARKLES,
        )

        AchievementIconKey.CALENDAR_MONTH -> AchievementVisual(
            emoji = "📆",
            gradientStart = Color(0xFFFF9DD9),
            gradientEnd = Color(0xFF7A1AB3),
            accentColor = Color(0xFFFFE0F4),
            ringColor = Color(0xFF430872),
            decoration = AchievementDecoration.SPARKLES,
        )

        AchievementIconKey.CALENDAR_YEAR -> AchievementVisual(
            emoji = "🏅",
            gradientStart = Color(0xFFFFE66B),
            gradientEnd = Color(0xFFC8005A),
            accentColor = Color(0xFFFFF6B0),
            ringColor = Color(0xFF6E0036),
            decoration = AchievementDecoration.RAYS,
        )

        AchievementIconKey.MOON -> AchievementVisual(
            emoji = "🌙",
            gradientStart = Color(0xFF4A4FB3),
            gradientEnd = Color(0xFF0B0036),
            accentColor = Color(0xFFFFE066),
            ringColor = Color(0xFF050020),
            decoration = AchievementDecoration.STARS,
        )

        AchievementIconKey.SUNRISE -> AchievementVisual(
            emoji = "🌅",
            gradientStart = Color(0xFFFFC76B),
            gradientEnd = Color(0xFFE0306B),
            accentColor = Color(0xFFFFE7B0),
            ringColor = Color(0xFF8E1B40),
            decoration = AchievementDecoration.RAYS,
        )
    }
}
