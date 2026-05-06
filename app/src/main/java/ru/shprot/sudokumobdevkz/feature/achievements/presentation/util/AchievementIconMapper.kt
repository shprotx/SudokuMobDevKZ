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
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.ui.graphics.vector.ImageVector

object AchievementIconMapper {

    fun resolve(iconKey: String): ImageVector = when (iconKey) {
        "trophy_bronze" -> Icons.Filled.EmojiEvents
        "medal_bronze" -> Icons.Filled.MilitaryTech
        "medal_silver" -> Icons.Filled.MilitaryTech
        "medal_gold" -> Icons.Filled.MilitaryTech
        "crown" -> Icons.Filled.Star
        "leaf" -> Icons.Filled.Park
        "sun" -> Icons.Filled.WbSunny
        "mountain" -> Icons.Filled.Landscape
        "compass" -> Icons.Filled.Explore
        "check" -> Icons.Filled.CheckCircle
        "target" -> Icons.Filled.GpsFixed
        "gem" -> Icons.Filled.Diamond
        "bolt" -> Icons.Filled.Bolt
        "bolt_double" -> Icons.Filled.Bolt
        "bolt_triple" -> Icons.Filled.Bolt
        "fire_small" -> Icons.Filled.LocalFireDepartment
        "fire_medium" -> Icons.Filled.LocalFireDepartment
        "fire_big" -> Icons.Filled.LocalFireDepartment
        "calendar_check" -> Icons.Filled.CalendarToday
        "calendar_week" -> Icons.Filled.CalendarViewWeek
        "calendar_month" -> Icons.Filled.CalendarMonth
        "calendar_year" -> Icons.Filled.CalendarMonth
        "moon" -> Icons.Filled.NightsStay
        "sunrise" -> Icons.Filled.WbTwilight
        else -> Icons.Filled.Workspaces
    }
}