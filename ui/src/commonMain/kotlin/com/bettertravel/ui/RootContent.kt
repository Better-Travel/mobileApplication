package com.bettertravel.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.bettertravel.presentation.navigation.RootComponent
import com.bettertravel.ui.chat.ChatScreen
import com.bettertravel.ui.designsystem.layout.BtSafeArea
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.main.MainScreen
import com.bettertravel.ui.preview.PreviewRootComponent

@Composable
fun RootContent(root: RootComponent) {
    BetterTravelTheme {
        BtSafeArea {
            Children(stack = root.stack, animation = stackAnimation(slide())) { created ->
                when (val child = created.instance) {
                    is RootComponent.Child.Main -> MainScreen(child.component)
                    is RootComponent.Child.Chat -> ChatScreen(child.viewModel)
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
