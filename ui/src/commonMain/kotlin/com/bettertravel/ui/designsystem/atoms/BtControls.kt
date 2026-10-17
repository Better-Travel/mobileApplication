package com.bettertravel.ui.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow

@Composable
fun BtHorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = BetterTravelTheme.colors.surfaceContainerMax,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color),
    )
}

@Composable
fun BtVerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = BetterTravelTheme.colors.surfaceContainerMax,
) {
    Box(
        modifier =
            modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(color),
    )
}

@Composable
fun BtProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(colors.surfaceContainerHighest),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .clip(CircleShape)
                    .background(BtGradients.progress),
        )
    }
}

@Composable
fun BtSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Box(
        modifier =
            modifier
                .size(width = 44.dp, height = 24.dp)
                .clip(CircleShape)
                .background(if (checked) colors.primary else colors.surfaceContainerMax)
                .clickable(role = Role.Switch) { onCheckedChange(!checked) }
                .padding(2.dp),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White),
        )
    }
}

@Composable
fun BtDragHandle(
    modifier: Modifier = Modifier,
    width: Dp = 48.dp,
) {
    Box(
        modifier =
            modifier
                .size(width = width, height = 6.dp)
                .clip(CircleShape)
                .background(BetterTravelTheme.colors.surfaceContainerMax),
    )
}

enum class BtMarkerStyle {
    Primary,
    Coral,
    Deep,
    Outlined,
}

@Composable
fun BtNumberMarker(
    number: Int,
    modifier: Modifier = Modifier,
    style: BtMarkerStyle = BtMarkerStyle.Primary,
    size: Dp = 28.dp,
) {
    val colors = BetterTravelTheme.colors
    val container =
        when (style) {
            BtMarkerStyle.Primary -> colors.primary
            BtMarkerStyle.Coral -> colors.coral
            BtMarkerStyle.Deep -> colors.coralText
            BtMarkerStyle.Outlined -> colors.surface
        }
    val content =
        when (style) {
            BtMarkerStyle.Coral -> colors.coralDeep
            BtMarkerStyle.Outlined -> colors.brand
            else -> colors.textOnPrimary
        }
    val outline =
        if (style == BtMarkerStyle.Outlined) {
            Modifier.border(1.dp, colors.skyTint, CircleShape)
        } else {
            Modifier.btShadow(BtElevation.Xs, CircleShape)
        }
    Box(
        modifier =
            modifier
                .then(outline)
                .size(size)
                .clip(CircleShape)
                .background(container),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = number.toString(), style = BetterTravelTheme.typography.labelMedium, color = content)
    }
}

@Preview
@Composable
private fun BtDividerPreview() {
    BtPreview {
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
            BtHorizontalDivider()
            Box(modifier = Modifier.height(24.dp)) { BtVerticalDivider() }
        }
    }
}

@Preview
@Composable
private fun BtProgressBarPreview() {
    BtPreview {
        BtProgressBar(progress = 0.78f)
    }
}

@Preview
@Composable
private fun BtSwitchPreview() {
    BtPreview {
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtSwitch(checked = true, onCheckedChange = {})
            BtSwitch(checked = false, onCheckedChange = {})
        }
    }
}

@Preview
@Composable
private fun BtDragHandlePreview() {
    BtPreview {
        BtDragHandle()
    }
}

@Preview
@Composable
private fun BtNumberMarkerPreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtNumberMarker(1)
            BtNumberMarker(2)
            BtNumberMarker(3, style = BtMarkerStyle.Coral)
            BtNumberMarker(4, style = BtMarkerStyle.Deep)
            BtNumberMarker(1, style = BtMarkerStyle.Outlined, size = 20.dp)
        }
    }
}
