package com.bettertravel.ui.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtBadge
import com.bettertravel.ui.designsystem.atoms.BtBadgeStyle
import com.bettertravel.ui.designsystem.atoms.BtIcon
import com.bettertravel.ui.designsystem.atoms.BtSwitch
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_chevron_right
import com.bettertravel.ui.resources.ic_clock
import com.bettertravel.ui.resources.ic_globe
import com.bettertravel.ui.resources.ic_ticket
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun BtSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s), verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                BtIcon(icon, tint = colors.primary, size = 18.dp)
            }
            Text(text = title, style = BetterTravelTheme.typography.title, color = colors.textPrimary)
        }
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = BetterTravelTheme.typography.label,
                color = colors.primary,
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .clickable(role = Role.Button, onClick = onActionClick)
                        .padding(vertical = BtSpacing.xs),
            )
        }
    }
}

@Composable
fun BtInfoRow(
    icon: DrawableResource,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    note: String? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(BtShapes.md)
                .background(colors.surfaceContainer)
                .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
    ) {
        Box(
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceContainerHighest),
            contentAlignment = Alignment.Center,
        ) {
            BtIcon(icon, tint = colors.primary, size = 18.dp)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = title, style = BetterTravelTheme.typography.title, color = colors.textPrimary)
                trailing?.invoke()
            }
            Text(text = body, style = BetterTravelTheme.typography.bodyLarge, color = colors.textSecondary)
            if (note != null) {
                Text(text = note, style = BetterTravelTheme.typography.bodySmall, color = colors.textTertiary)
            }
        }
    }
}

@Composable
fun BtSettingRow(
    icon: DrawableResource,
    title: String,
    modifier: Modifier = Modifier,
    value: String? = null,
    subtitle: String? = null,
    iconTint: Color = BetterTravelTheme.colors.primary,
    checked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val colors = BetterTravelTheme.colors
    val clickable = if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .then(clickable)
                .padding(vertical = BtSpacing.xs),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceContainerHighest),
            contentAlignment = Alignment.Center,
        ) {
            BtIcon(icon, tint = iconTint, size = 16.dp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = BetterTravelTheme.typography.label, color = colors.textPrimary)
            if (subtitle != null) {
                Text(text = subtitle, style = BetterTravelTheme.typography.labelMedium, color = colors.textSecondary)
            }
        }
        if (value != null) {
            Text(text = value, style = BetterTravelTheme.typography.labelMedium, color = colors.textTertiary)
        }
        if (checked != null && onCheckedChange != null) {
            BtSwitch(checked = checked, onCheckedChange = onCheckedChange)
        }
        trailing?.invoke()
    }
}

@Preview
@Composable
private fun BtSectionHeaderPreview() {
    BtPreview {
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
            BtSectionHeader(title = "Visiting essentials", icon = Res.drawable.ic_clock)
            BtSectionHeader(title = "VISITED ROUTES", actionText = "See all", onActionClick = {})
        }
    }
}

@Preview
@Composable
private fun BtInfoRowPreview() {
    BtPreview {
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtInfoRow(
                icon = Res.drawable.ic_clock,
                title = "Opening hours",
                body = "Mon to Sat 10:00 to 17:00",
                note = "Closed during services",
                trailing = { BtBadge("Open now", style = BtBadgeStyle.Neutral) },
            )
            BtInfoRow(icon = Res.drawable.ic_ticket, title = "Admission", body = "Free entry to the nave")
        }
    }
}

@Preview
@Composable
private fun BtSettingRowPreview() {
    BtPreview {
        Column {
            BtSettingRow(icon = Res.drawable.ic_globe, title = "Language", value = "EN", onClick = {})
            BtSettingRow(
                icon = Res.drawable.ic_chevron_right,
                title = "Audio guide",
                checked = true,
                onCheckedChange = {},
            )
        }
    }
}
