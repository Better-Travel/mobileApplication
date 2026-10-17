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

    @Test
    fun testThat_rootIsCreated_mainIsTheOnlyDestination() {
        val root = DefaultRootComponent(createTestComponentContext())

        assertTrue(root.stack.value.active.instance is RootComponent.Child.Main)
        assertEquals(1, root.stack.value.items.size)
    }

    @Test
    fun testThat_rootIsCreated_discoverTabIsActive() {
        val root = DefaultRootComponent(createTestComponentContext())
        val main = root.stack.value.active.instance as RootComponent.Child.Main

        assertEquals(MainTab.Discover, main.component.children.value.active.instance.tab)
    }
}
