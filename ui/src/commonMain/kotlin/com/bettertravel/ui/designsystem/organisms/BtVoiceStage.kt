package com.bettertravel.ui.designsystem.organisms

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtBetty
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BtGradients

@Composable
fun BtVoiceStage(
    isListening: Boolean,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "stage-halo")
    val haloScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(1600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "stage-halo-scale",
    )
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier =
                Modifier
                    .size(300.dp)
                    .scale(if (isListening) haloScale else 1f)
                    .background(BtGradients.halo, CircleShape),
        )
        BtBetty(size = 220.dp)
    }
}

@Preview
@Composable
private fun BtVoiceStagePreview() {
    BtPreview {
        BtVoiceStage(isListening = true)
    }
}
