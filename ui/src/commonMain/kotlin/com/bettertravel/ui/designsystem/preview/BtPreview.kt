package com.bettertravel.ui.designsystem.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme

@Composable
fun BtPreview(content: @Composable () -> Unit) {
    BetterTravelTheme {
        Box(
            modifier =
                Modifier
                    .background(BetterTravelTheme.colors.background)
                    .padding(16.dp),
        ) {
            content()
        }
    }
}
