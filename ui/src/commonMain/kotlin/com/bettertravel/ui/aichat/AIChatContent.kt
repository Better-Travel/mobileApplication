package com.bettertravel.ui.aichat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bettertravel.presentation.aichat.AIChatViewModel
import com.bettertravel.ui.designsystem.atoms.BtTypingIndicator
import com.bettertravel.ui.designsystem.layout.isKeyboardVisible
import com.bettertravel.ui.designsystem.molecules.BtAssistantMessage
import com.bettertravel.ui.designsystem.molecules.BtInputPill
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.preview.PreviewAIChat
import com.bettertravel.ui.rx.collectAsState
import com.bettertravel.ui.rx.collectAsStateImmediately

val LocalMicModifier = compositionLocalOf<Modifier> { Modifier }

@Composable
fun AIChatContent(
    viewModel: AIChatViewModel,
    modifier: Modifier = Modifier,
) {
    val items by viewModel.items.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val stub by viewModel.stub.collectAsState(initial = null)
    val inputText by viewModel.inputViewModel.text.collectAsStateImmediately()
    val canSend by viewModel.inputViewModel.canSend.collectAsState(initial = false)
    val placeholder by viewModel.inputViewModel.placeholder.collectAsState(initial = "")
    val listState = rememberLazyListState()
    val keyboardVisible = isKeyboardVisible()

    LaunchedEffect(items.size, loading) {
        val count = listState.layoutInfo.totalItemsCount
        if (count > 0) {
            listState.animateScrollToItem(count - 1)
        }
    }

    val pillBottom = if (keyboardVisible) BtSpacing.m else BtSpacing.tabBarInset
    val contentBottom = pillBottom + BtSpacing.inputPillHeight + BtSpacing.m

    Box(modifier = modifier.fillMaxSize()) {
        val currentStub = stub
        if (currentStub != null) {
            AIChatStub(currentStub, modifier = Modifier.fillMaxSize().padding(bottom = contentBottom))
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding =
                    PaddingValues(
                        start = BtSpacing.l,
                        end = BtSpacing.l,
                        top = BtSpacing.xxl,
                        bottom = contentBottom,
                    ),
                verticalArrangement = Arrangement.spacedBy(BtSpacing.xl),
            ) {
                items(items, key = { it.id }) { item ->
                    AIChatItem(item)
                }
                if (loading) {
                    item(key = "typing") {
                        BtAssistantMessage(timestamp = null) {
                            BtTypingIndicator()
                        }
                    }
                }
            }
        }
        BtInputPill(
            value = inputText,
            onValueChange = { text -> viewModel.inputViewModel.onTextChanged(text) },
            placeholder = placeholder,
            onSend = { viewModel.inputViewModel.send() },
            onVoice = { viewModel.inputViewModel.startVoice() },
            // TODO: attachments are not supported yet
            onAdd = { },
            canSend = canSend,
            voiceModifier = LocalMicModifier.current,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = BtSpacing.l, end = BtSpacing.l, bottom = pillBottom),
        )
    }
}

@Preview
@Composable
private fun AIChatContentPreview() {
    BtPreview {
        CompositionLocalProvider(LocalMicModifier provides Modifier) {
            AIChatContent(PreviewAIChat.viewModel(withMessages = true))
        }
    }
}

@Preview
@Composable
private fun AIChatContentEmptyPreview() {
    BtPreview {
        AIChatContent(PreviewAIChat.viewModel(withMessages = false))
    }
}
