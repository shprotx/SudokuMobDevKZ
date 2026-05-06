package ru.shprot.sudokumobdevkz.core.base.domain.achievement

import androidx.annotation.StringRes

class Achievement(
    val id: String,
    @StringRes val titleRes: Int,
    @StringRes val descRes: Int,
    val iconKey: String,
    val category: AchievementCategory,
    val hidden: Boolean,
    val evaluate: (AchievementContext) -> AchievementProgress,
)