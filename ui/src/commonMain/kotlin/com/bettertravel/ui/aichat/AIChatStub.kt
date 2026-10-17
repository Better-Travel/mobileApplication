package com.bettertravel.ui.aichat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.presentation.aichat.AIChatStubViewModel
import com.bettertravel.ui.designsystem.atoms.BtBetty
import com.bettertravel.ui.designsystem.atoms.BtButton
import com.bettertravel.ui.designsystem.atoms.BtQuickReplyChip
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.preview.PreviewAIChat

@Composable
fun AIChatStub(
    viewModel: AIChatStubViewModel,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier = modifier.padding(BtSpacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BtSpacing.m, Alignment.CenterVertically),
    ) {
        BtBetty(size = 120.dp)
        Text(text = viewModel.title, style = BetterTravelTheme.typography.headlineLarge, color = colors.textPrimary)
        Text(
            text = viewModel.message,
            style = BetterTravelTheme.typography.body,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )
        val actionTitle = viewModel.actionTitle
        if (actionTitle != null) {
            BtButton(text = actionTitle, onClick = { viewModel.performAction() })
        }
        if (viewModel.suggestions.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(BtSpacing.s),
            ) {
                viewModel.suggestions.forEach { suggestion ->
                    BtQuickReplyChip(text = suggestion, onClick = { viewModel.select(suggestion) })
                }
            }
        }
    }
}

@Preview
@Composable
private fun AIChatStubEmptyPreview() {
    BtPreview {
        AIChatStub(PreviewAIChat.emptyStub())
    }
}

@Preview
@Composable
private fun AIChatStubErrorPreview() {
    BtPreview {
        AIChatStub(PreviewAIChat.errorStub())
    }
}
