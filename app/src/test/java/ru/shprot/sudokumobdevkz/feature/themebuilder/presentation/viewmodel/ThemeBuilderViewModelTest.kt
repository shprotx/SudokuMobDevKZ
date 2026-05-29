package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.IThemeRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.AppSettings
import ru.shprot.sudokumobdevkz.core.base.domain.model.CustomTheme
import ru.shprot.sudokumobdevkz.core.theme.AppColors
import ru.shprot.sudokumobdevkz.core.theme.BuiltInThemes
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIEffect
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIEvent

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeBuilderViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var themeRepository: FakeThemeRepository
    private lateinit var settingsRepository: FakeSettingsRepository
    private lateinit var viewModel: ThemeBuilderViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        themeRepository = FakeThemeRepository()
        settingsRepository = FakeSettingsRepository()
        viewModel = buildViewModel(themeId = null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(themeId: String?): ThemeBuilderViewModel {
        val handle = SavedStateHandle(mapOf("themeId" to themeId))
        return ThemeBuilderViewModel(
            savedStateHandle = handle,
            themeRepository = themeRepository,
            settingsRepository = settingsRepository,
        )
    }

    @Test
    fun `initial state uses Light preset colors`() {
        assertEquals(BuiltInThemes.Light.colors, viewModel.uiState.value.colors)
    }

    @Test
    fun `SelectPreset Dark updates all colors to Dark palette`() = runTest {
        viewModel.setEvent(ThemeBuilderUIEvent.SelectPreset(BuiltInThemes.Dark.colors))
        advanceUntilIdle()
        assertEquals(BuiltInThemes.Dark.colors, viewModel.uiState.value.colors)
    }

    @Test
    fun `ColorChanged updates only the specified key`() = runTest {
        val originalColors = viewModel.uiState.value.colors
        val newBg = 0xFF000000L
        viewModel.setEvent(ThemeBuilderUIEvent.ColorChanged("background", newBg))
        advanceUntilIdle()
        assertEquals(newBg, viewModel.uiState.value.colors.background)
        assertEquals(originalColors.primary, viewModel.uiState.value.colors.primary)
        assertEquals(originalColors.text, viewModel.uiState.value.colors.text)
    }

    @Test
    fun `ConfirmSave with empty name shows name error`() = runTest {
        viewModel.setEvent(ThemeBuilderUIEvent.SaveClicked)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.showSaveDialog)

        viewModel.setEvent(ThemeBuilderUIEvent.ConfirmSave(""))
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.showNameError)
    }

    @Test
    fun `ConfirmSave with valid name saves theme and navigates back`() = runTest {
        val effects = mutableListOf<ThemeBuilderUIEffect>()
        val job = launch {
            viewModel.effect.collect { effects.add(it) }
        }

        viewModel.setEvent(ThemeBuilderUIEvent.ConfirmSave("My Theme"))
        advanceUntilIdle()

        assertTrue(themeRepository.savedThemes.isNotEmpty())
        assertEquals("My Theme", themeRepository.savedThemes.last().name)
        assertTrue(effects.any { it is ThemeBuilderUIEffect.ShowMessage })
        assertTrue(effects.any { it == ThemeBuilderUIEffect.NavigateBack })

        job.cancel()
    }

    @Test
    fun `BackClicked emits NavigateBack effect`() = runTest {
        val effects = mutableListOf<ThemeBuilderUIEffect>()
        val job = launch {
            viewModel.effect.collect { effects.add(it) }
        }

        viewModel.setEvent(ThemeBuilderUIEvent.BackClicked)
        advanceUntilIdle()

        assertTrue(effects.contains(ThemeBuilderUIEffect.NavigateBack))
        job.cancel()
    }

    @Test
    fun `SelectPreset Solarized updates colors to Solarized palette`() = runTest {
        viewModel.setEvent(ThemeBuilderUIEvent.SelectPreset(BuiltInThemes.Solarized.colors))
        advanceUntilIdle()
        assertEquals(BuiltInThemes.Solarized.colors, viewModel.uiState.value.colors)
    }

    @Test
    fun `OpenColorPicker sets showColorPicker true`() = runTest {
        viewModel.setEvent(ThemeBuilderUIEvent.OpenColorPicker("primary"))
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.showColorPicker)
        assertEquals("primary", viewModel.uiState.value.selectedColorKey)
    }

    @Test
    fun `DismissColorPicker clears color picker state`() = runTest {
        viewModel.setEvent(ThemeBuilderUIEvent.OpenColorPicker("text"))
        advanceUntilIdle()
        viewModel.setEvent(ThemeBuilderUIEvent.DismissColorPicker)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.showColorPicker)
    }

    @Test
    fun `edit mode loads existing theme on init`() = runTest {
        val existingTheme = CustomTheme(
            id = "theme-42",
            name = "Existing",
            isBuiltIn = false,
            colors = BuiltInThemes.Dark.colors,
            createdAt = 100L,
        )
        themeRepository.savedThemes.add(existingTheme)

        val editVm = buildViewModel(themeId = "theme-42")
        advanceUntilIdle()

        assertEquals(BuiltInThemes.Dark.colors, editVm.uiState.value.colors)
        assertEquals("Existing", editVm.uiState.value.themeName)
        assertTrue(editVm.uiState.value.isEditMode)
    }
}

private class FakeThemeRepository : IThemeRepository {
    val savedThemes = mutableListOf<CustomTheme>()

    override fun observeAll(): Flow<List<CustomTheme>> = MutableStateFlow(savedThemes.toList())

    override suspend fun getAll(): List<CustomTheme> = savedThemes.toList()

    override suspend fun getById(id: String): CustomTheme? = savedThemes.firstOrNull { it.id == id }

    override suspend fun save(theme: CustomTheme) {
        savedThemes.removeAll { it.id == theme.id }
        savedThemes.add(theme)
    }

    override suspend fun delete(id: String) {
        savedThemes.removeAll { it.id == id }
    }

    override suspend fun seedBuiltIns() = Unit

    override fun resolveColors(themeId: String, isSystemDark: Boolean): Flow<AppColors> =
        MutableStateFlow(AppColors.LightColors)
}

private class FakeSettingsRepository : ISettingsRepository {
    private var _settings = AppSettings()
    override val settings: Flow<AppSettings> = MutableStateFlow(_settings)
    override val currentSettings: AppSettings get() = _settings

    override fun update(transform: AppSettings.() -> AppSettings) {
        _settings = _settings.transform()
    }
}