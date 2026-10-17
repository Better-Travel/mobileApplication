package com.bettertravel.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.bettertravel.presentation.navigation.RootComponent
import com.bettertravel.ui.designsystem.layout.BtSafeArea
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.main.MainScreen
import com.bettertravel.ui.preview.PreviewRootComponent
import com.bettertravel.ui.stopdetails.StopDetailsScreen

@Composable
fun RootContent(root: RootComponent) {
    val focusManager = LocalFocusManager.current
    BetterTravelTheme {
        BtSafeArea(
            insets = WindowInsets(0, 0, 0, 0),
            modifier =
                Modifier.pointerInput(Unit) {
                    detectTapGestures { focusManager.clearFocus() }
                },
        ) {
            Children(stack = root.stack, animation = stackAnimation(slide())) { created ->
                when (val child = created.instance) {
                    is RootComponent.Child.Main -> MainScreen(child.component)
                    is RootComponent.Child.StopDetails -> StopDetailsScreen(child.viewModel)
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
