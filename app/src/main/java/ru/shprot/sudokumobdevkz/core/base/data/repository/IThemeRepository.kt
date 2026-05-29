package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.flow.Flow
import ru.shprot.sudokumobdevkz.core.base.domain.model.CustomTheme
import ru.shprot.sudokumobdevkz.core.theme.AppColors

interface IThemeRepository {
    fun observeAll(): Flow<List<CustomTheme>>
    suspend fun getAll(): List<CustomTheme>
    suspend fun getById(id: String): CustomTheme?
    suspend fun save(theme: CustomTheme)
    suspend fun delete(id: String)
    suspend fun seedBuiltIns()
    fun resolveColors(themeId: String, isSystemDark: Boolean): Flow<AppColors>
}