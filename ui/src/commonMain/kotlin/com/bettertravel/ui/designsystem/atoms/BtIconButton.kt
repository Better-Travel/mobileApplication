package com.bettertravel.ui.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtIconSize
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_arrow_right
import com.bettertravel.ui.resources.ic_back
import com.bettertravel.ui.resources.ic_locate_me
import com.bettertravel.ui.resources.ic_menu
import com.bettertravel.ui.resources.ic_mic
import org.jetbrains.compose.resources.DrawableResource

enum class BtIconButtonStyle {
    Glass,
    Floating,
    Filled,
    Brand,
    Tonal,
    Plain,
    Hero,
}

private const val DISABLED_ALPHA = 0.35f

@Composable
fun BtIconButton(
    icon: DrawableResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: BtIconButtonStyle = BtIconButtonStyle.Glass,
    size: Dp = 44.dp,
    iconSize: Dp = BtIconSize.m,
    contentDescription: String? = null,
    enabled: Boolean = true,
    tint: Color? = null,
) {
    val colors = BetterTravelTheme.colors
    val iconTint: Color =
        tint
            ?: when (style) {
                BtIconButtonStyle.Filled, BtIconButtonStyle.Brand, BtIconButtonStyle.Hero -> colors.textOnPrimary
                BtIconButtonStyle.Tonal -> colors.brand
                else -> colors.textPrimary
            }
    val shadow =
        when {
            !enabled -> Modifier
            style == BtIconButtonStyle.Glass -> Modifier.btShadow(BtElevation.Xs, CircleShape)
            style == BtIconButtonStyle.Floating -> Modifier.btShadow(BtElevation.Md, CircleShape)
            style == BtIconButtonStyle.Brand -> Modifier.btShadow(BtElevation.Brand, CircleShape)
            style == BtIconButtonStyle.Hero -> Modifier.btShadow(BtElevation.Brand, CircleShape)
            else -> Modifier
        }
    val background =
        when (style) {
            BtIconButtonStyle.Glass -> {
                Modifier
                    .background(colors.glass)
                    .border(1.dp, colors.glassBorder, CircleShape)
            }

            BtIconButtonStyle.Floating -> {
                Modifier
                    .background(colors.glassStrong)
                    .border(1.dp, colors.outline, CircleShape)
            }

            BtIconButtonStyle.Filled -> {
                Modifier.background(colors.primary)
            }

            BtIconButtonStyle.Brand -> {
                Modifier.background(if (enabled) colors.brand else colors.brand.copy(alpha = DISABLED_ALPHA))
            }

            BtIconButtonStyle.Tonal -> {
                Modifier.background(colors.brand.copy(alpha = 0.12f))
            }

            BtIconButtonStyle.Hero -> {
                Modifier.background(BtGradients.micButton)
            }

            BtIconButtonStyle.Plain -> {
                Modifier
            }
        }
    val dimmed = if (!enabled && style != BtIconButtonStyle.Brand) Modifier.alpha(DISABLED_ALPHA) else Modifier
    Box(
        modifier =
            modifier
                .then(dimmed)
                .then(shadow)
                .size(size)
                .clip(CircleShape)
                .then(background)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        BtIcon(icon, tint = iconTint, size = iconSize, contentDescription = contentDescription)
    }
}

@Preview
@Composable
private fun BtIconButtonPreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.m), verticalAlignment = Alignment.CenterVertically) {
            BtIconButton(Res.drawable.ic_menu, onClick = {}, style = BtIconButtonStyle.Plain, size = 40.dp)
            BtIconButton(Res.drawable.ic_back, onClick = {}, style = BtIconButtonStyle.Glass)
            BtIconButton(Res.drawable.ic_locate_me, onClick = {}, style = BtIconButtonStyle.Floating)
            BtIconButton(Res.drawable.ic_arrow_right, onClick = {}, style = BtIconButtonStyle.Brand, size = 40.dp)
            BtIconButton(
                Res.drawable.ic_arrow_right,
                onClick = {},
                style = BtIconButtonStyle.Brand,
                size = 40.dp,
                enabled = false,
            )
            BtIconButton(Res.drawable.ic_mic, onClick = {}, style = BtIconButtonStyle.Tonal, size = 40.dp)
            BtIconButton(
                Res.drawable.ic_mic,
                onClick = {},
                style = BtIconButtonStyle.Hero,
                size = 64.dp,
                iconSize = 28.dp,
            )
        }
    }
}
