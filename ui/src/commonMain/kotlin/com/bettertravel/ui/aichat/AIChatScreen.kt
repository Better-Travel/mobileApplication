package com.bettertravel.ui.aichat

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bettertravel.presentation.aichat.AIChatMode
import com.bettertravel.presentation.aichat.AIChatViewModel
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.preview.PreviewAIChat
import com.bettertravel.ui.rx.collectAsState

private const val MIC_SHARED_KEY = "mic"
private const val MODE_TRANSITION_MILLIS = 450

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AIChatScreen(viewModel: AIChatViewModel) {
    val mode by viewModel.mode.collectAsState()

    SharedTransitionLayout {
        AnimatedContent(
            targetState = mode,
            transitionSpec = {
                fadeIn(tween(MODE_TRANSITION_MILLIS)) togetherWith fadeOut(tween(MODE_TRANSITION_MILLIS))
            },
            label = "ai-chat-mode",
        ) { target ->
            val micModifier: Modifier =
                with(this@SharedTransitionLayout) {
                    Modifier.sharedElement(
                        sharedContentState = rememberSharedContentState(key = MIC_SHARED_KEY),
                        animatedVisibilityScope = this@AnimatedContent,
                    )
                }
            when (target) {
                AIChatMode.Text -> AIChatContent(viewModel = viewModel, micModifier = micModifier)
                AIChatMode.Voice -> AIVoiceContent(viewModel = viewModel.voiceViewModel, micModifier = micModifier)
            }
        }
    }
}

@Preview
@Composable
private fun AIChatScreenPreview() {
    BtPreview {
        AIChatScreen(PreviewAIChat.viewModel(withMessages = true))
    }
}

@Preview
@Composable
private fun AIChatScreenVoicePreview() {
    BtPreview {
        AIChatScreen(PreviewAIChat.viewModel(withMessages = true, mode = AIChatMode.Voice))
    }
}
