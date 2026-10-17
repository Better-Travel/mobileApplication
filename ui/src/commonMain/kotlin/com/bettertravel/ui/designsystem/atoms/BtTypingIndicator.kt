package com.bettertravel.ui.designsystem.atoms

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme

private const val DOT_COUNT = 3
private const val DOT_CYCLE_MILLIS = 1200
private const val DOT_STAGGER_MILLIS = 200

@Composable
fun BtTypingIndicator(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "typing")
    Row(
        modifier = modifier.padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(DOT_COUNT) { index ->
            val dotAlpha by transition.animateFloat(
                initialValue = 0.3f,
                targetValue = 0.3f,
                animationSpec =
                    infiniteRepeatable(
                        animation =
                            keyframes {
                                durationMillis = DOT_CYCLE_MILLIS
                                0.3f at 0
                                1f at DOT_CYCLE_MILLIS / 4
                                0.3f at DOT_CYCLE_MILLIS / 2
                            },
                        repeatMode = RepeatMode.Restart,
                        initialStartOffset = StartOffset(index * DOT_STAGGER_MILLIS),
                    ),
                label = "typing-dot-$index",
            )
            BtDot(
                color = BetterTravelTheme.colors.brand,
                size = 8.dp,
                modifier = Modifier.alpha(dotAlpha),
            )
        }
    }
}

@Preview
@Composable
private fun BtTypingIndicatorPreview() {
    BtPreview {
        BtTypingIndicator()
    }
}
