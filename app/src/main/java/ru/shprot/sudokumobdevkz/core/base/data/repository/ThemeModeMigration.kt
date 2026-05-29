package ru.shprot.sudokumobdevkz.core.base.data.repository

import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeMode

internal object ThemeModeMigration {
    fun resolve(storedId: String?, legacyDarkTheme: Boolean?): String {
        if (storedId != null) {
            return storedId
        }
        return if (legacyDarkTheme == true) ThemeMode.Dark.id else ThemeMode.System.id
    }
}