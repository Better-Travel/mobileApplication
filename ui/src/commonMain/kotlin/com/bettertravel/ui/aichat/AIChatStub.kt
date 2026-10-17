package com.bettertravel.ui.aichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.preview.PreviewAIChat

@Composable
fun AIChatStub(
    viewModel: AIChatStubViewModel,
    modifier: Modifier = Modifier,
) {
    val accentTitle = viewModel.accentTitle
    if (accentTitle != null) {
        AIChatLanding(
            title = viewModel.title,
            accentTitle = accentTitle,
            message = viewModel.message,
            modifier = modifier,
        )
    } else {
        AIChatProblem(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
private fun AIChatLanding(
    title: String,
    accentTitle: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    val typography = BetterTravelTheme.typography
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = BtSpacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(BtSpacing.xxxl))
        Text(text = title, style = typography.display, color = colors.textPrimary, textAlign = TextAlign.Center)
        Text(
            text = accentTitle,
            style = typography.display.copy(brush = BtGradients.heading),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BtSpacing.m))
        Text(text = message, style = typography.body, color = colors.textTertiary, textAlign = TextAlign.Center)
        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.size(256.dp).background(BtGradients.halo, CircleShape))
            BtBetty(size = 240.dp)
        }
    }
}

@Composable
private fun AIChatProblem(
    viewModel: AIChatStubViewModel,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier = modifier.fillMaxSize().padding(BtSpacing.xxl),
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

@Preview
@Composable
private fun AIChatLandingPreview() {
    BtPreview {
        AIChatLanding(
            title = "Let's plan your trip",
            accentTitle = "Together",
            message = "Meet Betty, your pocket-sized explorer ready to curate custom journeys in seconds.",
        )
    }
}

@Preview
@Composable
private fun AIChatProblemPreview() {
    BtPreview {
        AIChatProblem(PreviewAIChat.errorStub())
    }
}
