package ru.shprot.sudokumobdevkz.core.base.domain.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.theme.AppColors
import ru.shprot.sudokumobdevkz.core.theme.toThemeColors

class ThemeColorsTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `decoding json without draftHighlight field falls back to text default`() {
        val fullJson = json.encodeToString(AppColors.LightColors.toThemeColors())
        val jsonWithoutDraftHighlight = fullJson.replace(Regex(""""draftHighlight":-?\d+,"""), "")

        val decoded = json.decodeFromString<ThemeColors>(jsonWithoutDraftHighlight)

        assertEquals(decoded.text, decoded.draftHighlight)
    }

    @Test
    fun `decoding full json preserves stored draftHighlight`() {
        val source = AppColors.LightColors.toThemeColors().copy(draftHighlight = 0xFFFF0000)
        val encoded = json.encodeToString(source)

        val decoded = json.decodeFromString<ThemeColors>(encoded)

        assertEquals(0xFFFF0000, decoded.draftHighlight)
    }
}
