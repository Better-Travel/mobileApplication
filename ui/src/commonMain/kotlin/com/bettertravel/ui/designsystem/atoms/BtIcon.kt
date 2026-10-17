package com.bettertravel.ui.designsystem.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtIconSize
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_compass
import com.bettertravel.ui.resources.ic_heart
import com.bettertravel.ui.resources.ic_microphone
import com.bettertravel.ui.resources.ic_star
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun BtIcon(
    icon: DrawableResource,
    modifier: Modifier = Modifier,
    tint: Color = BetterTravelTheme.colors.textPrimary,
    size: Dp = BtIconSize.m,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.size(size),
    )
}

@Preview
@Composable
private fun BtIconPreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.l)) {
            BtIcon(Res.drawable.ic_compass)
            BtIcon(Res.drawable.ic_microphone, tint = BetterTravelTheme.colors.primary)
            BtIcon(Res.drawable.ic_heart, tint = BetterTravelTheme.colors.coralText, size = BtIconSize.xl)
            BtIcon(Res.drawable.ic_star, tint = BetterTravelTheme.colors.goldDark, size = BtIconSize.s)
        }
    }
}
