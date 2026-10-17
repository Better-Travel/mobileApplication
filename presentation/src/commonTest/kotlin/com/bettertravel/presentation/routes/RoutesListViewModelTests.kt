package com.bettertravel.presentation.routes

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.bettertravel.common.Deps
import com.bettertravel.domain.routes.Route
import com.bettertravel.domain.routes.RoutesRepository
import com.bettertravel.domainmock.routes.RoutesRepositoryMock
import com.bettertravel.localization.GeneratedStringRepository
import com.bettertravel.localization.StringRepository
import com.bettertravel.presentation.ViewModelContext
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RoutesListViewModelTests {
    private val repository = RoutesRepositoryMock()
    private val lifecycle = LifecycleRegistry()
    private val selected = mutableListOf<Route>()
    private val route = Route("1", "Old Town Loop", "Warsaw", 3.4, 112, 4)

    @BeforeTest
    fun setUp() {
        Deps.di =
            DI {
                bindSingleton<RoutesRepository> { repository }
                bindSingleton<StringRepository> { GeneratedStringRepository { "en" } }
            }
        lifecycle.resume()
    }

    private fun createViewModel(): RoutesListViewModelImpl {
        val parameters =
            RoutesListViewModel.Parameters(
                context = ViewModelContext(DefaultComponentContext(lifecycle)),
                onRouteSelected = { selected.add(it) },
            )
        return RoutesListViewModelImpl(parameters)
    }

    @Test
    fun testThat_repositoryReturnsRoutes_routesAreExposed() {
        repository.result = Result.success(listOf(route))

        val viewModel = createViewModel()

        assertEquals(listOf(route), viewModel.routes.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun testThat_repositoryFails_errorMessageIsExposed() {
        repository.result = Result.failure(IllegalStateException("boom"))

        val viewModel = createViewModel()

        assertEquals("Something went wrong while loading your routes", viewModel.errorMessage.value)
        assertEquals(emptyList(), viewModel.routes.value)
    }

    @Test
    fun testThat_reloadIsCalled_repositoryIsQueriedAgain() {
        val viewModel = createViewModel()

        viewModel.reload()

        assertEquals(2, repository.loadCount)
    }

    @Test
    fun testThat_routeIsSelected_parametersCallbackIsInvoked() {
        val viewModel = createViewModel()

        viewModel.select(route)

        assertEquals(listOf(route), selected)
        lifecycle.destroy()
    }
}
