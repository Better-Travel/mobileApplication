package com.bettertravel.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bettertravel.presentation.chat.ChatViewModel
import com.bettertravel.ui.designsystem.molecules.BtChatTopBar
import com.bettertravel.ui.designsystem.molecules.BtEmptyState
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.preview.PreviewChatViewModel
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_back

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    ChatContent(
        title = viewModel.title,
        status = viewModel.status,
        emptyMessage = viewModel.emptyMessage,
        onBack = { viewModel.back() },
    )
}

@Composable
fun ChatContent(
    title: String,
    status: String,
    emptyMessage: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(BetterTravelTheme.colors.background),
    ) {
        BtChatTopBar(
            title = title,
            status = status,
            onLeadingClick = onBack,
            leadingIcon = Res.drawable.ic_back,
        )
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(BtGradients.page),
            contentAlignment = Alignment.Center,
        ) {
            BtEmptyState(title = title, message = emptyMessage)
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    BtPreview {
        ChatScreen(PreviewChatViewModel())
    }
}

@Preview
@Composable
private fun ChatContentPreview() {
    BtPreview {
        ChatContent(
            title = "BetterTravel",
            status = "with Betty",
            emptyMessage = "Tell Betty where you are and how long you have. Chat is coming soon.",
            onBack = {},
        )
    }
}
