package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.SavedGameEntity
import ru.shprot.sudokumobdevkz.core.base.domain.model.GameSaveData

class SavedGameDailyFieldsMappingTest {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private fun makeDailyGameSaveData(): GameSaveData =
        GameSaveData(
            difficulty = 1,
            timeSeconds = 42,
            errors = 1,
            maxErrors = 3,
            hintsRemaining = 2,
            isNotesEnabled = false,
            cells = List(9) { List(9) { GameSaveData.CellSave() } },
            solution = List(9) { List(9) { 0 } },
            isStandardMode = true,
            isDailyChallenge = true,
            dailyDateKey = "2026-07-23",
        )

    private fun toEntity(data: GameSaveData): SavedGameEntity =
        SavedGameEntity(
            difficulty = data.difficulty,
            timeSeconds = data.timeSeconds,
            errors = data.errors,
            maxErrors = data.maxErrors,
            hintsRemaining = data.hintsRemaining,
            isNotesEnabled = data.isNotesEnabled,
            cellsJson = json.encodeToString(data.cells),
            solutionJson = json.encodeToString(data.solution),
            isStandardMode = data.isStandardMode,
            isDailyChallenge = data.isDailyChallenge,
            dailyDateKey = data.dailyDateKey,
        )

    private fun toDomain(entity: SavedGameEntity): GameSaveData =
        GameSaveData(
            difficulty = entity.difficulty,
            timeSeconds = entity.timeSeconds,
            errors = entity.errors,
            maxErrors = entity.maxErrors,
            hintsRemaining = entity.hintsRemaining,
            isNotesEnabled = entity.isNotesEnabled,
            cells = json.decodeFromString(entity.cellsJson),
            solution = json.decodeFromString(entity.solutionJson),
            isStandardMode = entity.isStandardMode,
            isDailyChallenge = entity.isDailyChallenge,
            dailyDateKey = entity.dailyDateKey,
        )

    @Test
    fun dailyChallengeSave_survivesEntityRoundTrip() {
        val original = makeDailyGameSaveData()
        val entity = toEntity(original)
        val restored = toDomain(entity)

        assertTrue(restored.isDailyChallenge)
        assertEquals("2026-07-23", restored.dailyDateKey)
        assertEquals(original, restored)
    }

    @Test
    fun regularGameSave_dailyFieldsDefaultToFalseAndEmpty() {
        val original = GameSaveData(
            difficulty = 0,
            timeSeconds = 10,
            errors = 0,
            maxErrors = 3,
            hintsRemaining = 3,
            isNotesEnabled = false,
            cells = List(9) { List(9) { GameSaveData.CellSave() } },
            solution = List(9) { List(9) { 0 } },
            isStandardMode = true,
        )
        val entity = toEntity(original)
        val restored = toDomain(entity)

        assertFalse(restored.isDailyChallenge)
        assertEquals("", restored.dailyDateKey)
    }

    @Test
    fun savedGameEntity_defaultConstructor_isNotDaily() {
        val entity = SavedGameEntity()
        assertFalse(entity.isDailyChallenge)
        assertEquals("", entity.dailyDateKey)
    }
}