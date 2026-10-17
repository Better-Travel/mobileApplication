package com.bettertravel.ui.aichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bettertravel.presentation.aichat.AIChatViewModel
import com.bettertravel.ui.designsystem.atoms.BtTypingIndicator
import com.bettertravel.ui.designsystem.molecules.BtAssistantMessage
import com.bettertravel.ui.designsystem.molecules.BtChatTopBar
import com.bettertravel.ui.designsystem.molecules.BtInputPill
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.preview.PreviewAIChat
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_back
import com.bettertravel.ui.rx.collectAsState

@Composable
fun AIChatContent(
    viewModel: AIChatViewModel,
    modifier: Modifier = Modifier,
    micModifier: Modifier = Modifier,
) {
    val items by viewModel.items.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val stub by viewModel.stub.collectAsState(initial = null)
    val inputText by viewModel.inputViewModel.text.collectAsState()
    val canSend by viewModel.inputViewModel.canSend.collectAsState(initial = false)
    val listState = rememberLazyListState()

    LaunchedEffect(items.size, loading) {
        val count = listState.layoutInfo.totalItemsCount
        if (count > 0) {
            listState.animateScrollToItem(count - 1)
        }
    }

    Column(modifier = modifier.fillMaxSize().background(BetterTravelTheme.colors.background)) {
        BtChatTopBar(
            title = viewModel.title,
            status = viewModel.status,
            onLeadingClick = { viewModel.back() },
            leadingIcon = Res.drawable.ic_back,
        )
        Box(modifier = Modifier.weight(1f).fillMaxSize().background(BtGradients.page)) {
            val currentStub = stub
            if (currentStub != null) {
                AIChatStub(currentStub, modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = BtSpacing.l, vertical = BtSpacing.xxl),
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
        }
        BtInputPill(
            value = inputText,
            onValueChange = { text -> viewModel.inputViewModel.onTextChanged(text) },
            placeholder = viewModel.inputViewModel.placeholder,
            onSend = { viewModel.inputViewModel.send() },
            onVoice = { viewModel.inputViewModel.startVoice() },
            canSend = canSend,
            voiceModifier = micModifier,
            modifier = Modifier.padding(horizontal = BtSpacing.l, vertical = BtSpacing.m),
        )
    }
}

@Preview
@Composable
private fun AIChatContentPreview() {
    BtPreview {
        AIChatContent(PreviewAIChat.viewModel(withMessages = true))
    }
}

@Preview
@Composable
private fun AIChatContentEmptyPreview() {
    BtPreview {
        AIChatContent(PreviewAIChat.viewModel(withMessages = false))
    }
}
