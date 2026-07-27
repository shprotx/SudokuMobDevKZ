package ru.shprot.sudokumobdevkz.core.base.data.cloud

import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CustomThemeDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.DailyChallengeDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SavedGameDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.StatisticDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.UnlockedAchievementDto
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.AchievementUnlockedEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.CustomThemeEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.DailyChallengeEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.SavedGameEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.StatisticEntity

object CloudProgressMappers {

    fun StatisticEntity.toDto(): StatisticDto = StatisticDto(
        allTime = allTime,
        bestTime = bestTime,
        averageTime = averageTime,
        gamesStarted = gamesStarted,
        gamesWon = gamesWon,
        percentOfWins = percentOfWins,
        winsWithoutErrors = winsWithoutErrors,
        bestWinsLine = bestWinsLine,
        currentWinsLine = currentWinsLine,
        casualGamesPlayed = casualGamesPlayed,
    )

    fun StatisticDto.toEntity(difficultyKey: Int): StatisticEntity = StatisticEntity(
        difficulty = difficultyKey,
        allTime = allTime,
        bestTime = bestTime,
        averageTime = averageTime,
        gamesStarted = gamesStarted,
        gamesWon = gamesWon,
        percentOfWins = percentOfWins,
        winsWithoutErrors = winsWithoutErrors,
        bestWinsLine = bestWinsLine,
        currentWinsLine = currentWinsLine,
        casualGamesPlayed = casualGamesPlayed,
    )

    fun AchievementUnlockedEntity.toDto(): UnlockedAchievementDto = UnlockedAchievementDto(
        id = id,
        unlockedAt = unlockedAt,
    )

    fun UnlockedAchievementDto.toEntity(): AchievementUnlockedEntity = AchievementUnlockedEntity(
        id = id,
        unlockedAt = unlockedAt,
    )

    fun DailyChallengeEntity.toDto(): DailyChallengeDto = DailyChallengeDto(
        dateKey = dateKey,
        difficultyOrdinal = difficultyOrdinal,
        isCompleted = isCompleted,
        completionTimeSeconds = completionTimeSeconds,
        errors = errors,
        completedAt = completedAt,
    )

    fun DailyChallengeDto.toEntity(): DailyChallengeEntity = DailyChallengeEntity(
        dateKey = dateKey,
        difficultyOrdinal = difficultyOrdinal,
        isCompleted = isCompleted,
        completionTimeSeconds = completionTimeSeconds,
        errors = errors,
        completedAt = completedAt,
    )

    fun SavedGameEntity.toDto(): SavedGameDto = SavedGameDto(
        difficulty = difficulty,
        timeSeconds = timeSeconds,
        errors = errors,
        maxErrors = maxErrors,
        hintsRemaining = hintsRemaining,
        isNotesEnabled = isNotesEnabled,
        cellsJson = cellsJson,
        solutionJson = solutionJson,
        isStandardMode = isStandardMode,
        timestamp = timestamp,
        isDailyChallenge = isDailyChallenge,
        dailyDateKey = dailyDateKey,
    )

    fun SavedGameDto.toEntity(): SavedGameEntity = SavedGameEntity(
        difficulty = difficulty,
        timeSeconds = timeSeconds,
        errors = errors,
        maxErrors = maxErrors,
        hintsRemaining = hintsRemaining,
        isNotesEnabled = isNotesEnabled,
        cellsJson = cellsJson,
        solutionJson = solutionJson,
        isStandardMode = isStandardMode,
        timestamp = timestamp,
        isDailyChallenge = isDailyChallenge,
        dailyDateKey = dailyDateKey,
    )

    fun CustomThemeEntity.toDto(): CustomThemeDto = CustomThemeDto(
        id = id,
        name = name,
        colorsJson = colorsJson,
        createdAt = createdAt,
    )

    fun CustomThemeDto.toEntity(): CustomThemeEntity = CustomThemeEntity(
        id = id,
        name = name,
        isBuiltIn = false,
        colorsJson = colorsJson,
        createdAt = createdAt,
    )
}
