package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import kotlinx.serialization.Serializable

@Serializable
data class CloudProgress(
    val schemaVersion: Int = SCHEMA_VERSION,
    val statistics: Map<Int, StatisticDto> = emptyMap(),
    val unlockedAchievements: List<UnlockedAchievementDto> = emptyList(),
    val dailyChallenges: List<DailyChallengeDto> = emptyList(),
    val savedGame: SavedGameDto? = null,
    val customThemes: List<CustomThemeDto> = emptyList(),
    val currentVisitStreak: Int = 0,
    val bestVisitStreak: Int = 0,
    val lastVisitDate: String? = null,
    val lastSyncTimestamp: Long = 0L,
) {
    companion object {
        const val SCHEMA_VERSION = 2
    }
}
