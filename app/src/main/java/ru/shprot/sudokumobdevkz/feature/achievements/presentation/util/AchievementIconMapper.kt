package ru.shprot.sudokumobdevkz.feature.achievements.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.ui.graphics.vector.ImageVector
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementIconKey

object AchievementIconMapper {

    fun resolve(iconKey: AchievementIconKey): ImageVector = when (iconKey) {
        AchievementIconKey.TROPHY_BRONZE -> Icons.Filled.EmojiEvents
        AchievementIconKey.MEDAL_BRONZE -> Icons.Filled.MilitaryTech
        AchievementIconKey.MEDAL_SILVER -> Icons.Filled.MilitaryTech
        AchievementIconKey.MEDAL_GOLD -> Icons.Filled.MilitaryTech
        AchievementIconKey.CROWN -> Icons.Filled.Star
        AchievementIconKey.LEAF -> Icons.Filled.Park
        AchievementIconKey.SUN -> Icons.Filled.WbSunny
        AchievementIconKey.MOUNTAIN -> Icons.Filled.Landscape
        AchievementIconKey.COMPASS -> Icons.Filled.Explore
        AchievementIconKey.CHECK -> Icons.Filled.CheckCircle
        AchievementIconKey.TARGET -> Icons.Filled.GpsFixed
        AchievementIconKey.GEM -> Icons.Filled.Diamond
        AchievementIconKey.BOLT -> Icons.Filled.Bolt
        AchievementIconKey.BOLT_DOUBLE -> Icons.Filled.Bolt
        AchievementIconKey.BOLT_TRIPLE -> Icons.Filled.Bolt
        AchievementIconKey.FIRE_SMALL -> Icons.Filled.LocalFireDepartment
        AchievementIconKey.FIRE_MEDIUM -> Icons.Filled.LocalFireDepartment
        AchievementIconKey.FIRE_BIG -> Icons.Filled.LocalFireDepartment
        AchievementIconKey.CALENDAR_CHECK -> Icons.Filled.CalendarToday
        AchievementIconKey.CALENDAR_WEEK -> Icons.Filled.CalendarViewWeek
        AchievementIconKey.CALENDAR_MONTH -> Icons.Filled.CalendarMonth
        AchievementIconKey.CALENDAR_YEAR -> Icons.Filled.CalendarMonth
        AchievementIconKey.MOON -> Icons.Filled.NightsStay
        AchievementIconKey.SUNRISE -> Icons.Filled.WbTwilight
    }
}
