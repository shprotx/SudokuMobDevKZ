package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementIconKey
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.util.AchievementVisual

internal fun DrawScope.drawRichBadgeObject(
    iconKey: AchievementIconKey,
    visual: AchievementVisual,
) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val r = size.minDimension / 2f * 0.52f

    when (iconKey) {
        AchievementIconKey.TROPHY_BRONZE -> drawTrophy(center, r, bronzeMetal)

        AchievementIconKey.MEDAL_BRONZE -> drawRibbonMedal(
            center = center, r = r, metal = bronzeMetal,
            ribbonLight = ribbonBronzeLight, ribbonDark = ribbonBronzeDark,
            numeral = 3, withLaurels = false, accent = visual.accentColor,
        )

        AchievementIconKey.MEDAL_SILVER -> drawRibbonMedal(
            center = center, r = r, metal = silverMetal,
            ribbonLight = ribbonBlueLight, ribbonDark = ribbonBlueDark,
            numeral = 2, withLaurels = false, accent = visual.accentColor,
        )

        AchievementIconKey.MEDAL_GOLD -> drawRibbonMedal(
            center = center, r = r, metal = goldMetal,
            ribbonLight = ribbonRedLight, ribbonDark = ribbonRedDark,
            numeral = 1, withLaurels = true, accent = visual.accentColor,
        )

        AchievementIconKey.CROWN -> drawCrown(center, r, visual.accentColor)

        AchievementIconKey.LEAF -> drawLeafBranch(center, r)

        AchievementIconKey.SUN -> drawSunBadge(center, r)

        AchievementIconKey.MOUNTAIN -> drawMountainBadge(center, r)

        AchievementIconKey.COMPASS -> drawCompassBadge(center, r)

        AchievementIconKey.CHECK -> drawCheckBadge(center, r)

        AchievementIconKey.TARGET -> drawTargetBadge(center, r)

        AchievementIconKey.GEM -> drawGemBadge(center, r)

        AchievementIconKey.BOLT -> drawBoltSingle(center, r)

        AchievementIconKey.BOLT_DOUBLE -> drawBoltDouble(center, r)

        AchievementIconKey.BOLT_TRIPLE -> drawBoltTriple(center, r)

        AchievementIconKey.FIRE_SMALL -> drawFireSmall(center, r)

        AchievementIconKey.FIRE_MEDIUM -> drawFireMedium(center, r)

        AchievementIconKey.FIRE_BIG -> drawFireBig(center, r)

        AchievementIconKey.CALENDAR_CHECK -> drawCalendarCheckBadge(center, r)

        AchievementIconKey.CALENDAR_WEEK -> drawCalendarWeekBadge(center, r)

        AchievementIconKey.CALENDAR_MONTH -> drawCalendarMonthBadge(center, r)

        AchievementIconKey.CALENDAR_YEAR -> drawYearMedalBadge(center, r, visual.accentColor)

        AchievementIconKey.MOON -> drawMoonBadge(center, r)

        AchievementIconKey.SUNRISE -> drawSunriseBadge(center, r)

        AchievementIconKey.TROPHY_SILVER -> drawTrophy(center, r, silverMetal)

        AchievementIconKey.TROPHY_GOLD -> drawTrophy(center, r, goldMetal, withStar = true)

        AchievementIconKey.TROPHY_PLATINUM ->
            drawTrophy(center, r, platinumMetal, withStar = true, withAura = true)

        AchievementIconKey.DIAMOND_PINK -> drawGemColored(center, r, gemPink)

        AchievementIconKey.TIARA -> drawTiara(center, r, visual.accentColor)

        AchievementIconKey.FIRE_BLUE -> drawFireBlue(center, r)

        AchievementIconKey.CALENDAR_FORTNIGHT -> drawCalendarFortnightBadge(center, r)

        AchievementIconKey.CALENDAR_STACK -> drawCalendarStackBadge(center, r)

        AchievementIconKey.HOURGLASS_GOLD -> drawHourglassBadge(center, r)

        AchievementIconKey.GLOBE -> drawGlobeBadge(center, r)

        AchievementIconKey.STOPWATCH -> drawStopwatchBadge(center, r)

        AchievementIconKey.COMET_GREEN -> drawComet(center, r, cometGreen)

        AchievementIconKey.COMET_ORANGE -> drawComet(center, r, cometOrange)

        AchievementIconKey.COMET_PURPLE -> drawComet(center, r, cometPurple)

        AchievementIconKey.OWL -> drawOwlBadge(center, r)

        AchievementIconKey.SHIELD_CRACKED -> drawCrackedShieldBadge(center, r)
    }
}

private val ribbonBronzeLight = Color(0xFFE9A66B)
private val ribbonBronzeDark = Color(0xFF9A5A2A)
private val ribbonBlueLight = Color(0xFF6AB2FF)
private val ribbonBlueDark = Color(0xFF2467C4)
private val ribbonRedLight = Color(0xFFFF5A6E)
private val ribbonRedDark = Color(0xFFC81E3C)