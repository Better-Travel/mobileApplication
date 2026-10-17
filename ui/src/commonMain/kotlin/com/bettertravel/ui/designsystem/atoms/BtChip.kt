package com.bettertravel.ui.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtIconSize
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_heart
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun BtFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    count: Int? = null,
    icon: DrawableResource? = null,
) {
    val colors = BetterTravelTheme.colors
    val container = if (selected) colors.brand else colors.surfaceContainerHigh
    val content = if (selected) colors.textOnPrimary else colors.textSecondary
    Row(
        modifier =
            modifier
                .clip(CircleShape)
                .background(container)
                .clickable(role = Role.Tab, onClick = onClick)
                .padding(horizontal = BtSpacing.l, vertical = BtSpacing.s),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            BtIcon(icon, tint = content, size = BtIconSize.xs)
        }
        Text(text = text, style = BetterTravelTheme.typography.label.copy(letterSpacing = 0.24.sp), color = content)
        if (count != null) {
            Text(text = "($count)", style = BetterTravelTheme.typography.bodySmall, color = content.copy(alpha = 0.8f))
        }
    }
}

@Composable
fun BtQuickReplyChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Text(
        text = text,
        style = BetterTravelTheme.typography.label.copy(fontWeight = FontWeight.Medium),
        color = colors.textSecondary,
        modifier =
            modifier
                .clip(CircleShape)
                .background(colors.glassStrong)
                .border(1.dp, colors.outline, CircleShape)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(horizontal = 15.dp, vertical = 7.dp),
    )
}

@Preview
@Composable
private fun BtFilterChipPreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
            BtFilterChip("All", selected = true, onClick = {}, count = 6)
            BtFilterChip("Warsaw", selected = false, onClick = {}, count = 4)
            BtFilterChip("Favorites", selected = false, onClick = {}, icon = Res.drawable.ic_heart)
        }
    }
}

@Preview
@Composable
private fun BtQuickReplyChipPreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtQuickReplyChip("Make it shorter", onClick = {})
            BtQuickReplyChip("Add a café stop", onClick = {})
        }
    }
}
