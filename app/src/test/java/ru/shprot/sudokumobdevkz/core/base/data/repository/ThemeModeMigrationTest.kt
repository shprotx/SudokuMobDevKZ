package ru.shprot.sudokumobdevkz.core.base.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeMode

class ThemeModeMigrationTest {

    @Test
    fun `resolve returns stored id when present`() {
        assertEquals(
            ThemeMode.Dark.id,
            ThemeModeMigration.resolve(storedId = "DARK", legacyDarkTheme = null),
        )
    }

    @Test
    fun `resolve maps unknown stored id to System`() {
        assertEquals(
            ThemeMode.System.id,
            ThemeModeMigration.resolve(storedId = "UNKNOWN", legacyDarkTheme = true),
        )
    }

    @Test
    fun `resolve returns Dark when legacy dark_theme was true`() {
        assertEquals(
            ThemeMode.Dark.id,
            ThemeModeMigration.resolve(storedId = null, legacyDarkTheme = true),
        )
    }

    @Test
    fun `resolve returns System when legacy dark_theme was false`() {
        assertEquals(
            ThemeMode.System.id,
            ThemeModeMigration.resolve(storedId = null, legacyDarkTheme = false),
        )
    }

    @Test
    fun `resolve returns System when no values are present`() {
        assertEquals(
            ThemeMode.System.id,
            ThemeModeMigration.resolve(storedId = null, legacyDarkTheme = null),
        )
    }

    @Test
    fun `stored id wins over legacy dark_theme`() {
        assertEquals(
            ThemeMode.Light.id,
            ThemeModeMigration.resolve(storedId = "LIGHT", legacyDarkTheme = true),
        )
    }
}