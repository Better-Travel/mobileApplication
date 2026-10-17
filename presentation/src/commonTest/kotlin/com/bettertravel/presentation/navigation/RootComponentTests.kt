package com.bettertravel.presentation.navigation

import com.bettertravel.presentation.createTestComponentContext
import com.bettertravel.presentation.installTestDeps
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RootComponentTests {
    @BeforeTest
    fun setUp() {
        installTestDeps()
    }

    private fun activeChild(root: DefaultRootComponent): RootComponent.Child {
        return root.stack.value.active.instance
    }

    @Test
    fun testThat_rootIsCreated_mainIsActive() {
        val root = DefaultRootComponent(createTestComponentContext())

        assertTrue(activeChild(root) is RootComponent.Child.Main)
    }

    @Test
    fun testThat_discoverOpensChat_chatIsPushedOnTopOfMain() {
        val root = DefaultRootComponent(createTestComponentContext())
        val main = activeChild(root) as RootComponent.Child.Main
        val discover = main.component.children.value.active.instance as MainComponent.Child.Discover

        discover.viewModel.openChat()

        assertTrue(activeChild(root) is RootComponent.Child.Chat)
        assertEquals(2, root.stack.value.items.size)
    }

    @Test
    fun testThat_chatGoesBack_mainIsActiveAgain() {
        val root = DefaultRootComponent(createTestComponentContext())
        val main = activeChild(root) as RootComponent.Child.Main
        val discover = main.component.children.value.active.instance as MainComponent.Child.Discover
        discover.viewModel.openChat()
        val chat = activeChild(root) as RootComponent.Child.Chat

        chat.viewModel.back()

        assertTrue(activeChild(root) is RootComponent.Child.Main)
    }
}
