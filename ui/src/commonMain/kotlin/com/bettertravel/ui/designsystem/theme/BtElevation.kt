package com.bettertravel.ui.designsystem.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class BtElevation(
    val elevation: Dp,
    val tint: Color,
) {
    Xs(1.dp, Color(0x0D000000)),
    Md(4.dp, Color(0x1A000000)),
    Lg(8.dp, Color(0x1A000000)),
    Xl(14.dp, Color(0x1A000000)),
    Xxl(20.dp, Color(0x40000000)),
    Brand(6.dp, Color(0x734EA8DE)),
    Primary(6.dp, Color(0x40006590)),
}

fun Modifier.btShadow(
    level: BtElevation,
    shape: Shape,
): Modifier {
    return shadow(
        elevation = level.elevation,
        shape = shape,
        ambientColor = level.tint,
        spotColor = level.tint,
    )
}
