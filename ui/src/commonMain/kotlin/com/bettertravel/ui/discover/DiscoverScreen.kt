package com.bettertravel.ui.discover

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
import com.bettertravel.presentation.discover.DiscoverViewModel
import com.bettertravel.ui.designsystem.atoms.BtBetty
import com.bettertravel.ui.designsystem.molecules.BtPromptPill
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.preview.PreviewDiscoverViewModel

@Composable
fun DiscoverScreen(viewModel: DiscoverViewModel) {
    DiscoverContent(
        titlePrefix = viewModel.titlePrefix,
        titleAccent = viewModel.titleAccent,
        subtitle = viewModel.subtitle,
        inputPlaceholder = viewModel.inputPlaceholder,
        onInputClick = { viewModel.openChat() },
    )
}

@Composable
fun DiscoverContent(
    titlePrefix: String,
    titleAccent: String,
    subtitle: String,
    inputPlaceholder: String,
    onInputClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    val typography = BetterTravelTheme.typography
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = BtSpacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(BtSpacing.xxxl))
        Text(
            text = titlePrefix,
            style = typography.display,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = titleAccent,
            style = typography.display.copy(brush = BtGradients.heading),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BtSpacing.m))
        Text(
            text = subtitle,
            style = typography.body,
            color = colors.textTertiary,
            textAlign = TextAlign.Center,
        )
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(256.dp)
                        .background(BtGradients.halo, CircleShape),
            )
            BtBetty(size = 240.dp)
        }
        BtPromptPill(placeholder = inputPlaceholder, onClick = onInputClick)
        Spacer(modifier = Modifier.height(BtSpacing.tabBarInset))
    }
}

@Preview
@Composable
private fun DiscoverScreenPreview() {
    BtPreview {
        DiscoverScreen(PreviewDiscoverViewModel())
    }
}

@Preview
@Composable
private fun DiscoverContentPreview() {
    BtPreview {
        DiscoverContent(
            titlePrefix = "Let's plan your trip",
            titleAccent = "Together",
            subtitle = "Meet Betty, your pocket-sized explorer ready to curate custom journeys in seconds.",
            inputPlaceholder = "Tell BetterTravel where & how long...",
            onInputClick = {},
        )
    }
}
