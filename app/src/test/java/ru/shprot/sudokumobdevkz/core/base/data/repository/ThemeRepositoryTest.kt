package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.CustomThemeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.CustomThemeEntity
import ru.shprot.sudokumobdevkz.core.base.domain.model.CustomTheme
import ru.shprot.sudokumobdevkz.core.theme.AppColors
import ru.shprot.sudokumobdevkz.core.theme.BuiltInTheme

class ThemeRepositoryTest {

    private lateinit var dao: FakeCustomThemeDao
    private lateinit var repository: ThemeRepository

    @Before
    fun setUp() {
        dao = FakeCustomThemeDao()
        repository = ThemeRepository(dao)
    }

    @Test
    fun `getAll returns seeded themes`() = runTest {
        repository.seedBuiltIns()
        val themes = repository.getAll()
        assertEquals(BuiltInTheme.entries.size, themes.size)
    }

    @Test
    fun `save persists theme and getById returns it`() = runTest {
        val theme = CustomTheme(
            id = "test-id",
            name = "My Theme",
            isBuiltIn = false,
            colors = BuiltInTheme.LIGHT.colors,
            createdAt = 1000L,
        )
        repository.save(theme)
        val loaded = repository.getById("test-id")
        assertNotNull(loaded)
        assertEquals("My Theme", loaded?.name)
    }

    @Test
    fun `delete removes custom theme`() = runTest {
        val theme = CustomTheme(
            id = "delete-me",
            name = "Old Theme",
            isBuiltIn = false,
            colors = BuiltInTheme.DARK.colors,
            createdAt = 2000L,
        )
        repository.save(theme)
        repository.delete("delete-me")
        assertNull(dao.storage["delete-me"])
    }

    @Test
    fun `delete does not remove built-in theme`() = runTest {
        repository.seedBuiltIns()
        repository.delete(BuiltInTheme.LIGHT.id)
        assertNotNull(dao.storage[BuiltInTheme.LIGHT.id])
    }

    @Test
    fun `resolveColors returns LightColors for LIGHT id`() = runTest {
        val colors = repository.resolveColors(BuiltInTheme.LIGHT.id, isSystemDark = false).first()
        assertEquals(AppColors.LightColors, colors)
    }

    @Test
    fun `resolveColors returns DarkColors for DARK id`() = runTest {
        val colors = repository.resolveColors(BuiltInTheme.DARK.id, isSystemDark = false).first()
        assertEquals(AppColors.DarkColors, colors)
    }

    @Test
    fun `resolveColors respects isSystemDark for SYSTEM id`() = runTest {
        val dark = repository.resolveColors("SYSTEM", isSystemDark = true).first()
        assertEquals(AppColors.DarkColors, dark)

        val light = repository.resolveColors("SYSTEM", isSystemDark = false).first()
        assertEquals(AppColors.LightColors, light)
    }

    @Test
    fun `resolveColors falls back when id not found`() = runTest {
        val colors = repository.resolveColors("unknown-id", isSystemDark = false).first()
        assertEquals(AppColors.LightColors, colors)
    }

    @Test
    fun `seedBuiltIns does not re-seed already present themes`() = runTest {
        repository.seedBuiltIns()
        val countAfterFirst = dao.upsertCount
        repository.seedBuiltIns()
        val countAfterSecond = dao.upsertCount
        assertEquals(countAfterFirst, countAfterSecond)
    }
}

internal class FakeCustomThemeDao : CustomThemeDao {
    val storage = mutableMapOf<String, CustomThemeEntity>()
    var upsertCount = 0

    private val flow = MutableStateFlow<List<CustomThemeEntity>>(emptyList())

    override fun observeAll(): Flow<List<CustomThemeEntity>> = flow

    override suspend fun getAll(): List<CustomThemeEntity> = storage.values.toList()

    override suspend fun getById(id: String): CustomThemeEntity? = storage[id]

    override suspend fun upsert(entity: CustomThemeEntity) {
        upsertCount++
        storage[entity.id] = entity
        flow.value = storage.values.toList()
    }

    override suspend fun deleteById(id: String) {
        storage.remove(id)
        flow.value = storage.values.toList()
    }

    override suspend fun exists(id: String): Int = if (storage.containsKey(id)) 1 else 0
}