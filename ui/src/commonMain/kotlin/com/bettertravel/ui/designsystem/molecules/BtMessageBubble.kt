package com.bettertravel.ui.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtAvatar
import com.bettertravel.ui.designsystem.atoms.BtAvatarRing
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.betty

@Composable
fun BtUserMessage(
    text: String,
    timestamp: String,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(BtSpacing.xs),
    ) {
        Text(
            text = text,
            style = BetterTravelTheme.typography.bodyStrong.copy(fontWeight = FontWeight.Normal),
            color = colors.textOnPrimary,
            modifier =
                Modifier
                    .widthIn(max = 322.dp)
                    .btShadow(BtElevation.Brand, BtShapes.userBubble)
                    .clip(BtShapes.userBubble)
                    .background(BtGradients.userBubble)
                    .padding(BtSpacing.l),
        )
        Text(
            text = timestamp,
            style = BetterTravelTheme.typography.labelSmall,
            color = colors.textTertiary,
            modifier = Modifier.padding(end = BtSpacing.s),
        )
    }
}

@Composable
fun BtAssistantMessage(
    timestamp: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top,
    ) {
        BtAvatar(image = Res.drawable.betty, size = 32.dp, ring = BtAvatarRing.Border)
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
            Column(
                modifier =
                    Modifier
                        .btShadow(BtElevation.Xs, BtShapes.assistantBubble)
                        .clip(BtShapes.assistantBubble)
                        .background(colors.glass)
                        .border(1.dp, colors.glassBorder, BtShapes.assistantBubble)
                        .padding(17.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                content()
            }
            Text(
                text = timestamp,
                style = BetterTravelTheme.typography.labelSmall,
                color = colors.textTertiary,
                modifier = Modifier.padding(start = BtSpacing.s),
            )
        }
    }
}

@Preview
@Composable
private fun BtUserMessagePreview() {
    BtPreview {
        BtUserMessage(
            text = "Hey Betty! I'm in Warsaw for the afternoon. I want a scenic walk with great coffee.",
            timestamp = "2:14 PM",
        )
    }
}

@Preview
@Composable
private fun BtAssistantMessagePreview() {
    BtPreview {
        BtAssistantMessage(timestamp = "2:14 PM") {
            Text(
                text = "I love Warsaw! I've sketched a gentle 3.2 km loop through the Old Town.",
                style = BetterTravelTheme.typography.bodyStrong.copy(fontWeight = FontWeight.Normal),
                color = BetterTravelTheme.colors.textSecondary,
            )
        }
    }
}
