package com.bettertravel.ui.designsystem.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
object BtSpacing {
    val xxs: Dp = 2.dp
    val xs: Dp = 4.dp
    val s: Dp = 8.dp
    val m: Dp = 12.dp
    val l: Dp = 16.dp
    val xl: Dp = 20.dp
    val xxl: Dp = 24.dp
    val xxxl: Dp = 32.dp
    val tabBarInset: Dp = 80.dp
    val inputPillHeight: Dp = 56.dp
}

@Immutable
object BtShapes {
    val pill: Shape = CircleShape
    val chatTail: Dp = 2.dp
    val xs: Shape = RoundedCornerShape(6.dp)
    val sm: Shape = RoundedCornerShape(12.dp)
    val md: Shape = RoundedCornerShape(16.dp)
    val lg: Shape = RoundedCornerShape(20.dp)
    val xl: Shape = RoundedCornerShape(24.dp)
    val xxl: Shape = RoundedCornerShape(32.dp)
    val sheet: Shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    val heroBottom: Shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
    val userBubble: Shape = RoundedCornerShape(24.dp, 2.dp, 24.dp, 24.dp)
    val assistantBubble: Shape = RoundedCornerShape(2.dp, 24.dp, 24.dp, 24.dp)
}

@Immutable
object BtIconSize {
    val xs: Dp = 12.dp
    val s: Dp = 16.dp
    val m: Dp = 20.dp
    val l: Dp = 24.dp
    val xl: Dp = 28.dp
}
