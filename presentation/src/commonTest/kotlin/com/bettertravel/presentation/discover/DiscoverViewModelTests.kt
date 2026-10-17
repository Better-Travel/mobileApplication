package com.bettertravel.presentation.discover

import com.bettertravel.presentation.ViewModelContext
import com.bettertravel.presentation.createTestComponentContext
import com.bettertravel.presentation.installTestDeps
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DiscoverViewModelTests {
    private var openChatCount = 0

    @BeforeTest
    fun setUp() {
        installTestDeps()
    }

    private fun createViewModel(): DiscoverViewModelImpl {
        val parameters =
            DiscoverViewModel.Parameters(
                context = ViewModelContext(createTestComponentContext()),
                onOpenChat = { openChatCount += 1 },
            )
        return DiscoverViewModelImpl(parameters)
    }

    @Test
    fun testThat_openChatIsCalled_parametersCallbackIsInvoked() {
        val viewModel = createViewModel()

        viewModel.openChat()

        assertEquals(1, openChatCount)
    }

    @Test
    fun testThat_languageIsEnglish_titleIsEnglish() {
        val viewModel = createViewModel()

        assertEquals("Let's plan your trip", viewModel.titlePrefix)
        assertEquals("Together", viewModel.titleAccent)
    }

    @Test
    fun testThat_languageIsFrench_titleIsFrench() {
        installTestDeps(language = "fr")

        val viewModel = createViewModel()

        assertEquals("Planifions votre voyage", viewModel.titlePrefix)
    }
}
