package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.CustomThemeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.CustomThemeEntity
import ru.shprot.sudokumobdevkz.core.base.domain.model.CustomTheme
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.base.domain.model.toAppColors
import ru.shprot.sudokumobdevkz.core.theme.AppColors
import ru.shprot.sudokumobdevkz.core.theme.BuiltInThemes
import ru.shprot.sudokumobdevkz.core.theme.toThemeColors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepository @Inject constructor(
    private val customThemeDao: CustomThemeDao,
) : IThemeRepository {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override fun observeAll(): Flow<List<CustomTheme>> =
        customThemeDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getAll(): List<CustomTheme> =
        customThemeDao.getAll().map { it.toDomain() }

    override suspend fun getById(id: String): CustomTheme? =
        customThemeDao.getById(id)?.toDomain()

    override suspend fun save(theme: CustomTheme) {
        customThemeDao.upsert(theme.toEntity())
    }

    override suspend fun delete(id: String) {
        val entity = customThemeDao.getById(id) ?: return
        if (!entity.isBuiltIn) {
            customThemeDao.deleteById(id)
        }
    }

    override suspend fun seedBuiltIns() {
        BuiltInThemes.all.forEach { theme ->
            if (customThemeDao.exists(theme.id) == 0) {
                customThemeDao.upsert(theme.toEntity())
            }
        }
    }

    override fun resolveColors(themeId: String, isSystemDark: Boolean): Flow<AppColors> =
        observeAll().map { themes ->
            when (themeId) {
                BuiltInThemes.ID_LIGHT -> AppColors.LightColors
                BuiltInThemes.ID_DARK -> AppColors.DarkColors
                "LIGHT" -> AppColors.LightColors
                "DARK" -> AppColors.DarkColors
                "SYSTEM" -> if (isSystemDark) AppColors.DarkColors else AppColors.LightColors
                else -> themes.firstOrNull { it.id == themeId }?.colors?.toAppColors()
                    ?: (if (isSystemDark) AppColors.DarkColors else AppColors.LightColors)
            }
        }

    private fun CustomThemeEntity.toDomain(): CustomTheme {
        val colors = runCatching { json.decodeFromString<ThemeColors>(colorsJson) }
            .getOrDefault(AppColors.LightColors.toThemeColors())
        return CustomTheme(
            id = id,
            name = name,
            isBuiltIn = isBuiltIn,
            colors = colors,
            createdAt = createdAt,
        )
    }

    private fun CustomTheme.toEntity(): CustomThemeEntity = CustomThemeEntity(
        id = id,
        name = name,
        isBuiltIn = isBuiltIn,
        colorsJson = json.encodeToString(colors),
        createdAt = createdAt,
    )
}