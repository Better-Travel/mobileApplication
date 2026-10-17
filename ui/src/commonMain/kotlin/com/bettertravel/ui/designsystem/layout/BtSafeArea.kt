package com.bettertravel.ui.designsystem.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme

@Composable
fun BtSafeArea(
    modifier: Modifier = Modifier,
    insets: WindowInsets = WindowInsets.safeDrawing,
    background: Color = BetterTravelTheme.colors.background,
    content: @Composable () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(background)
                .windowInsetsPadding(insets),
    ) {
        content()
    }
}

@Preview
@Composable
private fun BtSafeAreaPreview() {
    BtPreview {
        BtSafeArea {
            Text(text = "Content stays inside the system bars", style = BetterTravelTheme.typography.body)
        }
    }
}
