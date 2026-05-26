package ru.shprot.sudokumobdevkz.core.base.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeModeTest {

    @Test
    fun `fromId returns System for SYSTEM`() {
        assertEquals(ThemeMode.System, ThemeMode.fromId("SYSTEM"))
    }

    @Test
    fun `fromId returns Light for LIGHT`() {
        assertEquals(ThemeMode.Light, ThemeMode.fromId("LIGHT"))
    }

    @Test
    fun `fromId returns Dark for DARK`() {
        assertEquals(ThemeMode.Dark, ThemeMode.fromId("DARK"))
    }

    @Test
    fun `fromId returns System for unknown id`() {
        assertEquals(ThemeMode.System, ThemeMode.fromId("CUSTOM_42"))
    }

    @Test
    fun `fromId returns System for null id`() {
        assertEquals(ThemeMode.System, ThemeMode.fromId(null))
    }

    @Test
    fun `fromId returns System for empty id`() {
        assertEquals(ThemeMode.System, ThemeMode.fromId(""))
    }

    @Test
    fun `builtIn returns System Light Dark in order`() {
        val items = ThemeMode.builtIn()
        assertEquals(3, items.size)
        assertEquals(ThemeMode.System, items[0])
        assertEquals(ThemeMode.Light, items[1])
        assertEquals(ThemeMode.Dark, items[2])
    }

    @Test
    fun `resolveDark for System follows isSystemDark`() {
        assertTrue(ThemeMode.System.resolveDark(isSystemDark = true))
        assertFalse(ThemeMode.System.resolveDark(isSystemDark = false))
    }

    @Test
    fun `resolveDark for Light is always false`() {
        assertFalse(ThemeMode.Light.resolveDark(isSystemDark = true))
        assertFalse(ThemeMode.Light.resolveDark(isSystemDark = false))
    }

    @Test
    fun `resolveDark for Dark is always true`() {
        assertTrue(ThemeMode.Dark.resolveDark(isSystemDark = true))
        assertTrue(ThemeMode.Dark.resolveDark(isSystemDark = false))
    }

    @Test
    fun `resolveDark for Custom uses isDark flag`() {
        val customDark = ThemeMode.Custom(id = "RED", title = "Red", isDark = true)
        val customLight = ThemeMode.Custom(id = "BEIGE", title = "Beige", isDark = false)

        assertTrue(customDark.resolveDark(isSystemDark = false))
        assertTrue(customDark.resolveDark(isSystemDark = true))
        assertFalse(customLight.resolveDark(isSystemDark = false))
        assertFalse(customLight.resolveDark(isSystemDark = true))
    }
}