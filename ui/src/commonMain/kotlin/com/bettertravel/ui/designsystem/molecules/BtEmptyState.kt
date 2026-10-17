package com.bettertravel.ui.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtBetty
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtSpacing

@Composable
fun BtEmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier = modifier.padding(BtSpacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BtSpacing.m, Alignment.CenterVertically),
    ) {
        BtBetty(size = 120.dp)
        Text(text = title, style = BetterTravelTheme.typography.headlineLarge, color = colors.textPrimary)
        Text(
            text = message,
            style = BetterTravelTheme.typography.body,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun BtEmptyStatePreview() {
    BtPreview {
        BtEmptyState(title = "Memories", message = "Your saved walks will appear here.")
    }
}
