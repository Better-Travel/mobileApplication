package com.bettertravel.ui.designsystem.layout

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

@Composable
fun isKeyboardVisible(): Boolean {
    return WindowInsets.ime.getBottom(LocalDensity.current) > 0
}
