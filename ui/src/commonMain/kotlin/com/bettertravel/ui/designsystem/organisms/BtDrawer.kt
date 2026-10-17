package com.bettertravel.ui.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtIconButton
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_chevron_left

@Composable
fun BtDrawer(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val colors = BetterTravelTheme.colors
    val panelShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(colors.scrim)
                    .clickable(role = Role.Button, onClick = onDismiss),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.82f)
                    .btShadow(BtElevation.Xxl, panelShape)
                    .clip(panelShape)
                    .background(colors.glassStrong)
                    .clickable(enabled = false) {},
        ) {
            content()
        }
        BtIconButton(
            icon = Res.drawable.ic_chevron_left,
            onClick = onDismiss,
            size = 36.dp,
            modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = BtSpacing.m),
        )
    }
}

@Preview
@Composable
private fun BtDrawerPreview() {
    BtPreview {
        BtDrawer(onDismiss = {}) {
            Text(
                text = "Menu",
                style = BetterTravelTheme.typography.headlineLarge,
                modifier = Modifier.padding(BtSpacing.l),
            )
        }
    }
}
