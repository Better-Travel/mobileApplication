package com.bettertravel.ui.designsystem.molecules

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtDot
import com.bettertravel.ui.designsystem.atoms.BtIconButton
import com.bettertravel.ui.designsystem.atoms.BtQuickReplyChip
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_close

@Composable
fun BtDateDivider(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = text.uppercase(),
            style = BetterTravelTheme.typography.eyebrow,
            color = colors.textTertiary,
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(colors.outline.copy(alpha = 0.5f))
                    .padding(horizontal = BtSpacing.m, vertical = BtSpacing.xs),
        )
    }
}

@Composable
fun BtQuickReplies(
    title: String,
    replies: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
        Text(
            text = title.uppercase(),
            style = BetterTravelTheme.typography.eyebrow,
            color = BetterTravelTheme.colors.textTertiary,
            modifier = Modifier.padding(start = BtSpacing.xs),
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
        ) {
            replies.forEach { reply ->
                BtQuickReplyChip(text = reply, onClick = { onSelect(reply) })
            }
        }
    }
}

@Composable
fun BtVoiceSuggestionChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Text(
        text = "“$text”",
        style = BetterTravelTheme.typography.label,
        color = colors.textSecondary,
        modifier =
            modifier
                .btShadow(BtElevation.Xs, CircleShape)
                .clip(CircleShape)
                .background(colors.glass)
                .border(1.dp, colors.glassBorder, CircleShape)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(horizontal = 15.dp, vertical = 7.dp),
    )
}

@Composable
fun BtLiveStatusBadge(
    text: String,
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    val transition = rememberInfiniteTransition(label = "live-dot")
    val halo by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing), RepeatMode.Restart),
        label = "live-halo",
    )
    Row(
        modifier =
            modifier
                .btShadow(BtElevation.Xs, CircleShape)
                .clip(CircleShape)
                .background(colors.glass)
                .border(1.dp, colors.glassBorder, CircleShape)
                .padding(horizontal = 17.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(10.dp)
                    .drawBehind {
                        if (isActive) {
                            drawCircle(
                                color = colors.online.copy(alpha = 0.6f * (1f - halo)),
                                radius = size.minDimension / 2f * (1f + 1.2f * halo),
                            )
                        }
                    },
            contentAlignment = Alignment.Center,
        ) {
            BtDot(colors.online, size = 10.dp)
        }
        Text(text = text, style = BetterTravelTheme.typography.label, color = colors.textSecondary)
    }
}

@Composable
fun BtVoiceTopBar(
    status: String,
    isActive: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = BtSpacing.xxl, vertical = BtSpacing.m),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BtIconButton(Res.drawable.ic_close, onClick = onClose, size = 44.dp)
        BtLiveStatusBadge(text = status, isActive = isActive)
        Spacer(modifier = Modifier.size(44.dp))
    }
}

@Preview
@Composable
private fun BtDateDividerPreview() {
    BtPreview {
        BtDateDivider("Today")
    }
}

@Preview
@Composable
private fun BtQuickRepliesPreview() {
    BtPreview {
        BtQuickReplies(
            title = "Suggested adjustments",
            replies = listOf("Make it shorter", "Add a café stop", "Avoid stairs"),
            onSelect = {},
        )
    }
}

@Preview
@Composable
private fun BtVoiceSuggestionChipPreview() {
    BtPreview {
        BtVoiceSuggestionChip(text = "How far is it?", onClick = {})
    }
}

@Preview
@Composable
private fun BtLiveStatusBadgePreview() {
    BtPreview {
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtLiveStatusBadge(text = "Betty is listening...", isActive = true)
            BtLiveStatusBadge(text = "Tap the mic to talk", isActive = false)
        }
    }
}

@Preview
@Composable
private fun BtVoiceTopBarPreview() {
    BtPreview {
        BtVoiceTopBar(status = "Betty is listening...", isActive = true, onClose = {})
    }
}
