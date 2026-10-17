package com.bettertravel.ui.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalBtColors = staticCompositionLocalOf<BtColors> { BtLightColors }
private val LocalBtTypography =
    staticCompositionLocalOf<BtTypography> {
        error("BetterTravelTheme is not provided")
    }

object BetterTravelTheme {
    val colors: BtColors
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalBtColors.current
        }

    val typography: BtTypography
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalBtTypography.current
        }
}

@Composable
fun BetterTravelTheme(content: @Composable () -> Unit) {
    val colors = BtLightColors
    val typography = rememberBtTypography()
    val scheme =
        lightColorScheme(
            primary = colors.primary,
            onPrimary = colors.textOnPrimary,
            primaryContainer = colors.primaryContainer,
            onPrimaryContainer = colors.onPrimaryContainer,
            secondary = colors.coralText,
            secondaryContainer = colors.coralLight,
            onSecondaryContainer = colors.coralInk,
            tertiary = colors.goldDark,
            tertiaryContainer = colors.gold,
            background = colors.background,
            onBackground = colors.textPrimary,
            surface = colors.background,
            onSurface = colors.textPrimary,
            surfaceVariant = colors.surfaceContainerHigh,
            onSurfaceVariant = colors.textSecondary,
            surfaceContainer = colors.surfaceContainer,
            surfaceContainerHigh = colors.surfaceContainerHigh,
            surfaceContainerHighest = colors.surfaceContainerHighest,
            outline = colors.textTertiary,
            outlineVariant = colors.outlineVariant,
        )
    CompositionLocalProvider(
        LocalBtColors provides colors,
        LocalBtTypography provides typography,
    ) {
        MaterialTheme(colorScheme = scheme, content = content)
    }
}
