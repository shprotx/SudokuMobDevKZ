package ru.shprot.sudokumobdevkz.core.base.data.cloud

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressMappers.toDto
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressMappers.toEntity
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.SavedGameEntity

class CloudProgressMappersDailyTest {

    @Test
    fun savedGameEntity_toDto_carriesDailyFields() {
        val entity = SavedGameEntity(
            difficulty = 2,
            isDailyChallenge = true,
            dailyDateKey = "2026-07-23",
        )
        val dto = entity.toDto()

        assertTrue(dto.isDailyChallenge)
        assertEquals("2026-07-23", dto.dailyDateKey)
    }

    @Test
    fun savedGameDto_toEntity_roundTrip_preservesDailyFields() {
        val entity = SavedGameEntity(
            difficulty = 2,
            isDailyChallenge = true,
            dailyDateKey = "2026-07-23",
        )
        val restored = entity.toDto().toEntity()

        assertEquals(entity, restored)
    }

    @Test
    fun savedGameEntity_toDto_regularGame_dailyFieldsAreFalseAndEmpty() {
        val entity = SavedGameEntity(difficulty = 0, isStandardMode = true)
        val dto = entity.toDto()

        assertFalse(dto.isDailyChallenge)
        assertEquals("", dto.dailyDateKey)
    }
}