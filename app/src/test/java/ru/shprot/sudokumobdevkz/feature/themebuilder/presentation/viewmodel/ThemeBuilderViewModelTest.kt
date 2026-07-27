package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.IThemeRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.AppSettings
import ru.shprot.sudokumobdevkz.core.base.domain.model.CustomTheme
import ru.shprot.sudokumobdevkz.core.theme.AppColors
import ru.shprot.sudokumobdevkz.core.theme.BuiltInTheme
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIEffect
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIEvent
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.model.ThemeColorKey

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
    fun `initial state without themeId uses active theme colors`() = runTest {
        advanceUntilIdle()
        assertEquals(BuiltInTheme.LIGHT.colors, viewModel.uiState.value.colors)
    }

    @Test
    fun `initial state with Dark active theme uses Dark colors`() = runTest {
        settingsRepository.update { copy(themeModeId = "DARK") }
        val vm = buildViewModel(themeId = null)
        advanceUntilIdle()
        assertEquals(BuiltInTheme.DARK.colors, vm.uiState.value.colors)
    }

    @Test
    fun `ColorChanged updates only the specified key`() = runTest {
        advanceUntilIdle()
        val originalColors = viewModel.uiState.value.colors
        val newBg = 0xFF000000L
        viewModel.setEvent(ThemeBuilderUIEvent.ColorChanged(ThemeColorKey.BACKGROUND, newBg))
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
    fun `OpenColorPicker sets picker state and snapshots original color`() = runTest {
        advanceUntilIdle()
        val original = viewModel.uiState.value.colors.primary
        viewModel.setEvent(ThemeBuilderUIEvent.OpenColorPicker(ThemeColorKey.PRIMARY))
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.showColorPicker)
        assertEquals(ThemeColorKey.PRIMARY, viewModel.uiState.value.selectedColorKey)
        assertEquals(original, viewModel.uiState.value.pickerOriginalColor)
    }

    @Test
    fun `ApplyColorPicker keeps changed color and closes picker`() = runTest {
        advanceUntilIdle()
        val newColor = 0xFF123456L
        viewModel.setEvent(ThemeBuilderUIEvent.OpenColorPicker(ThemeColorKey.PRIMARY))
        viewModel.setEvent(ThemeBuilderUIEvent.ColorChanged(ThemeColorKey.PRIMARY, newColor))
        viewModel.setEvent(ThemeBuilderUIEvent.ApplyColorPicker)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.showColorPicker)
        assertEquals(newColor, viewModel.uiState.value.colors.primary)
    }

    @Test
    fun `CancelColorPicker reverts changed color and closes picker`() = runTest {
        advanceUntilIdle()
        val original = viewModel.uiState.value.colors.primary
        viewModel.setEvent(ThemeBuilderUIEvent.OpenColorPicker(ThemeColorKey.PRIMARY))
        viewModel.setEvent(ThemeBuilderUIEvent.ColorChanged(ThemeColorKey.PRIMARY, 0xFF123456L))
        viewModel.setEvent(ThemeBuilderUIEvent.CancelColorPicker)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.showColorPicker)
        assertEquals(original, viewModel.uiState.value.colors.primary)
    }

    @Test
    fun `edit mode loads existing theme on init`() = runTest {
        val existingTheme = CustomTheme(
            id = "theme-42",
            name = "Existing",
            isBuiltIn = false,
            colors = BuiltInTheme.DARK.colors,
            createdAt = 100L,
        )
        themeRepository.savedThemes.add(existingTheme)

        val editVm = buildViewModel(themeId = "theme-42")
        advanceUntilIdle()

        assertEquals(BuiltInTheme.DARK.colors, editVm.uiState.value.colors)
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

    override fun isLeaderboardNamePromptShown(): Flow<Boolean> = flowOf(true)

    override fun markLeaderboardNamePromptShown() = Unit
}