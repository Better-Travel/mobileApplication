package com.bettertravel.presentation.drawer

import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.createTestComponentContext
import com.bettertravel.presentation.installTestDeps
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DrawerViewModelTests {
    @BeforeTest
    fun setUp() {
        installTestDeps()
    }

    private fun createViewModel(): DrawerViewModelImpl {
        val parameters = DrawerViewModel.Parameters(ViewModelContext(createTestComponentContext()))
        return DrawerViewModelImpl(parameters)
    }

    @Test
    fun testThat_viewModelIsCreated_firstCityIsSelected() {
        val viewModel = createViewModel()

        assertEquals(viewModel.cities.first().id, viewModel.selectedCityId.value)
    }

    @Test
    fun testThat_cityIsSelected_selectionFollows() {
        val viewModel = createViewModel()
        val prague = viewModel.cities.first { city -> city.name == "Prague" }

        viewModel.selectCity(prague)

        assertEquals(prague.id, viewModel.selectedCityId.value)
    }

    @Test
    fun testThat_offlineMapsIsToggled_stateFollows() {
        val viewModel = createViewModel()
        assertTrue(viewModel.isOfflineMapsEnabled.value)

        viewModel.setOfflineMapsEnabled(false)

        assertFalse(viewModel.isOfflineMapsEnabled.value)
    }

    @Test
    fun testThat_titlesAreRequested_theyAreLocalized() {
        val viewModel = createViewModel()

        assertEquals("Explored cities", viewModel.exploredCitiesTitle)
        assertEquals(listOf("Cities", "Routes", "Walked"), viewModel.stats.map { stat -> stat.caption })
    }
}
