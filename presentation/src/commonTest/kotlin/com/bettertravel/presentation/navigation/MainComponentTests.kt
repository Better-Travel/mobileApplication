package com.bettertravel.presentation.navigation

import com.bettertravel.presentation.aichat.AIChatMode
import com.bettertravel.presentation.createTestComponentContext
import com.bettertravel.presentation.installTestDeps
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MainComponentTests {
    @BeforeTest
    fun setUp() {
        installTestDeps()
    }

    private fun createComponent(): DefaultMainComponent {
        return DefaultMainComponent(
            componentContext = createTestComponentContext(),
            onPreviewRoute = { },
            onStartRoute = { },
        )
    }

    @Test
    fun testThat_componentIsCreated_discoverTabIsActive() {
        val component = createComponent()

        assertEquals(MainTab.Discover, component.children.value.active.instance.tab)
    }

    @Test
    fun testThat_tabIsSelected_selectedTabBecomesActive() {
        val component = createComponent()

        component.selectTab(MainTab.Memories)

        assertEquals(MainTab.Memories, component.children.value.active.instance.tab)
    }

    @Test
    fun testThat_tabsAreSwitchedBack_discoverIsActiveAgain() {
        val component = createComponent()

        component.selectTab(MainTab.Route)
        component.selectTab(MainTab.Discover)

        assertEquals(MainTab.Discover, component.children.value.active.instance.tab)
    }

    @Test
    fun testThat_drawerIsOpenedAndClosed_stateFollows() {
        val component = createComponent()
        assertFalse(component.isDrawerOpen.value)

        component.openDrawer()
        assertTrue(component.isDrawerOpen.value)

        component.closeDrawer()
        assertFalse(component.isDrawerOpen.value)
    }

    @Test
    fun testThat_tabTitleIsRequested_titleIsLocalized() {
        val component = createComponent()

        assertEquals("Discover", component.tabTitle(MainTab.Discover))
        assertEquals("Route", component.tabTitle(MainTab.Route))
        assertEquals("Memories", component.tabTitle(MainTab.Memories))
    }

    @Test
    fun testThat_discoverTabIsActive_itHostsTheChat() {
        val component = createComponent()

        val discover = component.children.value.active.instance as MainComponent.Child.Discover

        assertEquals(AIChatMode.Text, discover.viewModel.mode.value)
        assertTrue(
            discover.viewModel.items.value
                .isEmpty(),
        )
    }
}
