package com.bettertravel.ui.aichat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.bettertravel.presentation.aichat.AIAssistantMessageViewModel
import com.bettertravel.presentation.aichat.AIChatViewModel
import com.bettertravel.presentation.aichat.AIQuickRepliesViewModel
import com.bettertravel.presentation.aichat.AIRouteCardViewModel
import com.bettertravel.presentation.aichat.AIUserMessageViewModel
import com.bettertravel.ui.designsystem.molecules.BtAssistantMessage
import com.bettertravel.ui.designsystem.molecules.BtDateDivider
import com.bettertravel.ui.designsystem.molecules.BtQuickReplies
import com.bettertravel.ui.designsystem.molecules.BtUserMessage
import com.bettertravel.ui.designsystem.organisms.BtEmbeddedRouteCard
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.preview.PreviewAIChat

@Composable
fun AIChatItem(
    item: AIChatViewModel.Item,
    modifier: Modifier = Modifier,
) {
    when (item) {
        is AIChatViewModel.Item.DateDivider -> BtDateDivider(text = item.title, modifier = modifier)
        is AIChatViewModel.Item.UserMessage -> AIUserMessage(item.viewModel, modifier)
        is AIChatViewModel.Item.AssistantMessage -> AIAssistantMessage(item.viewModel, modifier)
    }
}

@Composable
fun AIUserMessage(
    viewModel: AIUserMessageViewModel,
    modifier: Modifier = Modifier,
) {
    BtUserMessage(text = viewModel.text, timestamp = viewModel.time, modifier = modifier)
}

@Composable
fun AIAssistantMessage(
    viewModel: AIAssistantMessageViewModel,
    modifier: Modifier = Modifier,
) {
    BtAssistantMessage(timestamp = viewModel.time, modifier = modifier) {
        Text(
            text = viewModel.text,
            style = BetterTravelTheme.typography.bodyStrong.copy(fontWeight = FontWeight.Normal),
            color = BetterTravelTheme.colors.textSecondary,
        )
        viewModel.route?.let { route -> AIRouteCard(route) }
    }
}

@Composable
fun AIRouteCard(
    viewModel: AIRouteCardViewModel,
    modifier: Modifier = Modifier,
) {
    BtEmbeddedRouteCard(
        title = viewModel.title,
        duration = viewModel.duration,
        stops = viewModel.stops,
        previewLabel = viewModel.previewTitle,
        startLabel = viewModel.startTitle,
        onPreview = { viewModel.preview() },
        onStart = { viewModel.start() },
        modifier = modifier,
    )
}

@Composable
fun AIQuickReplies(
    viewModel: AIQuickRepliesViewModel,
    modifier: Modifier = Modifier,
) {
    BtQuickReplies(
        title = viewModel.title,
        replies = viewModel.replies,
        onSelect = { reply -> viewModel.select(reply) },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun AIChatItemsPreview() {
    BtPreview {
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.xl)) {
            PreviewAIChat.items().forEach { item ->
                AIChatItem(item)
            }
        }
    }
}

@Preview
@Composable
private fun AIUserMessagePreview() {
    BtPreview {
        AIUserMessage(PreviewAIChat.userMessage())
    }
}

@Preview
@Composable
private fun AIAssistantMessagePreview() {
    BtPreview {
        AIAssistantMessage(PreviewAIChat.assistantMessage(withRoute = true))
    }
}

@Preview
@Composable
private fun AIRouteCardPreview() {
    BtPreview {
        AIRouteCard(PreviewAIChat.routeCard())
    }
}

@Preview
@Composable
private fun AIQuickRepliesViewModelPreview() {
    BtPreview {
        AIQuickReplies(PreviewAIChat.quickReplies())
    }
}
