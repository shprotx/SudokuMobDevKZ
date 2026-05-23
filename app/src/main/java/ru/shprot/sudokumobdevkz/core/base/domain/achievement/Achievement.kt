package ru.shprot.sudokumobdevkz.core.base.domain.achievement

import androidx.annotation.StringRes

class Achievement(
    val id: String,
    val pgsId: String?,
    @StringRes val titleRes: Int,
    @StringRes val descRes: Int,
    val iconKey: AchievementIconKey,
    val category: AchievementCategory,
    val hidden: Boolean,
    val evaluate: (AchievementContext) -> AchievementProgress,
)