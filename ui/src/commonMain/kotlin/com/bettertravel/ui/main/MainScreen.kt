package com.bettertravel.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.bettertravel.presentation.navigation.MainComponent
import com.bettertravel.presentation.navigation.MainTab
import com.bettertravel.ui.designsystem.molecules.BtHomeTopBar
import com.bettertravel.ui.designsystem.molecules.BtTab
import com.bettertravel.ui.designsystem.molecules.BtTabBar
import com.bettertravel.ui.designsystem.organisms.BtDrawer
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.discover.DiscoverScreen
import com.bettertravel.ui.memories.MemoriesScreen
import com.bettertravel.ui.preview.PreviewMainComponent
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_tab_discover
import com.bettertravel.ui.resources.ic_tab_memories
import com.bettertravel.ui.resources.ic_tab_route_map
import com.bettertravel.ui.route.RouteScreen
import com.bettertravel.ui.rx.collectAsState
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun MainScreen(component: MainComponent) {
    val stack by component.children.subscribeAsState()
    val isDrawerOpen by component.isDrawerOpen.collectAsState()
    val tabs = component.tabs.map { tab -> BtTab(label = component.tabTitle(tab), icon = tab.icon()) }

    MainContent(
        appTitle = component.appTitle,
        tabs = tabs,
        selectedIndex = component.tabs.indexOf(stack.active.instance.tab),
        onTabSelected = { index -> component.selectTab(component.tabs[index]) },
        onMenuClick = { component.openDrawer() },
        isDrawerOpen = isDrawerOpen,
        drawerTitle = component.drawerTitle,
        drawerMessage = component.drawerMessage,
        onDrawerDismiss = { component.closeDrawer() },
    ) {
        Children(stack = component.children) { created ->
            when (val child = created.instance) {
                is MainComponent.Child.Discover -> DiscoverScreen(child.viewModel)
                is MainComponent.Child.Route -> RouteScreen(child.viewModel)
                is MainComponent.Child.Memories -> MemoriesScreen(child.viewModel)
            }
        }
    }
}

@Composable
fun MainContent(
    appTitle: String,
    tabs: List<BtTab>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    onMenuClick: () -> Unit,
    isDrawerOpen: Boolean,
    drawerTitle: String,
    drawerMessage: String,
    onDrawerDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            BtHomeTopBar(title = appTitle, onMenuClick = onMenuClick, onProfileClick = {})
            Box(modifier = Modifier.weight(1f)) {
                content()
            }
        }
        BtTabBar(
            tabs = tabs,
            selectedIndex = selectedIndex,
            onTabSelected = onTabSelected,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = BtSpacing.l, vertical = BtSpacing.s),
        )
        if (isDrawerOpen) {
            BtDrawer(title = drawerTitle, message = drawerMessage, onDismiss = onDrawerDismiss)
        }
    }
}

private fun MainTab.icon(): DrawableResource {
    return when (this) {
        MainTab.Discover -> Res.drawable.ic_tab_discover
        MainTab.Route -> Res.drawable.ic_tab_route_map
        MainTab.Memories -> Res.drawable.ic_tab_memories
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    BtPreview {
        MainScreen(PreviewMainComponent())
    }
}

@Preview
@Composable
private fun MainContentPreview() {
    BtPreview {
        MainContent(
            appTitle = "BetterTravel",
            tabs =
                listOf(
                    BtTab("Discover", Res.drawable.ic_tab_discover),
                    BtTab("Route", Res.drawable.ic_tab_route_map),
                    BtTab("Memories", Res.drawable.ic_tab_memories),
                ),
            selectedIndex = 0,
            onTabSelected = {},
            onMenuClick = {},
            isDrawerOpen = false,
            drawerTitle = "Menu",
            drawerMessage = "",
            onDrawerDismiss = {},
        ) {
            Text(text = "Tab content", style = BetterTravelTheme.typography.body, modifier = Modifier.padding(24.dp))
        }
    }
}
