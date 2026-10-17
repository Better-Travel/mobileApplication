package com.bettertravel.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.bettertravel.presentation.navigation.RootComponent
import com.bettertravel.ui.designsystem.layout.BtSafeArea
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.preview.PreviewRootComponent
import com.bettertravel.ui.routes.RoutesListScreen

@Composable
fun RootContent(root: RootComponent) {
    BetterTravelTheme {
        BtSafeArea {
            Children(stack = root.stack) { created ->
                when (val child = created.instance) {
                    is RootComponent.Child.RoutesList -> RoutesListScreen(child.viewModel)
                }
            }
        }
    }
}

@Preview
@Composable
private fun RootContentPreview() {
    RootContent(PreviewRootComponent())
}
