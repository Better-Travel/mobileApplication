package com.bettertravel.ui.designsystem.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtIconSize
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_navigation
import com.bettertravel.ui.resources.ic_pause_circle
import org.jetbrains.compose.resources.DrawableResource

enum class BtButtonStyle {
    Primary,
    Brand,
    Secondary,
    Outlined,
    Text,
    Destructive,
}

@Composable
fun BtButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: BtButtonStyle = BtButtonStyle.Primary,
    leadingIcon: DrawableResource? = null,
    enabled: Boolean = true,
) {
    val colors = BetterTravelTheme.colors
    val container =
        when (style) {
            BtButtonStyle.Primary -> colors.primary
            BtButtonStyle.Brand -> colors.brand
            BtButtonStyle.Secondary -> colors.surfaceContainerHigh
            BtButtonStyle.Outlined -> colors.surface
            BtButtonStyle.Text -> Color.Transparent
            BtButtonStyle.Destructive -> Color.Transparent
        }
    val content =
        when (style) {
            BtButtonStyle.Primary -> colors.textOnPrimary
            BtButtonStyle.Brand -> colors.textOnPrimary
            BtButtonStyle.Secondary -> colors.textPrimary
            BtButtonStyle.Outlined -> colors.textPrimary
            BtButtonStyle.Text -> colors.textSecondary
            BtButtonStyle.Destructive -> colors.coralText
        }
    val shadow =
        when (style) {
            BtButtonStyle.Primary -> Modifier.btShadow(BtElevation.Primary, CircleShape)
            BtButtonStyle.Brand -> Modifier.btShadow(BtElevation.Brand, CircleShape)
            else -> Modifier
        }
    val outline =
        if (style == BtButtonStyle.Outlined) {
            Modifier.border(BorderStroke(1.dp, colors.outline), CircleShape)
        } else {
            Modifier
        }
    Row(
        modifier =
            modifier
                .then(shadow)
                .clip(BtShapes.pill)
                .background(container)
                .then(outline)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
                .heightIn(min = 48.dp)
                .padding(horizontal = BtSpacing.xxl),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.s, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            BtIcon(leadingIcon, tint = content, size = BtIconSize.s)
        }
        Text(text = text, style = BetterTravelTheme.typography.button, color = content)
    }
}

@Preview
@Composable
private fun BtButtonPreview() {
    BtPreview {
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtButton("Keep in route", onClick = {}, modifier = Modifier.fillMaxWidth())
            BtButton(
                "Start route navigation",
                onClick = {},
                style = BtButtonStyle.Brand,
                leadingIcon = Res.drawable.ic_navigation,
            )
            BtButton(
                "Pause walk",
                onClick = {},
                style = BtButtonStyle.Secondary,
                leadingIcon = Res.drawable.ic_pause_circle,
            )
            BtButton("Show details", onClick = {}, style = BtButtonStyle.Outlined)
            BtButton("Back to home", onClick = {}, style = BtButtonStyle.Text)
            BtButton("Remove from this walk", onClick = {}, style = BtButtonStyle.Destructive)
        }
    }
}
