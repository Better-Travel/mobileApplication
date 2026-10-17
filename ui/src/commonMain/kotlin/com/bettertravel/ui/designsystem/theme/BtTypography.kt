package com.bettertravel.ui.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.inter
import com.bettertravel.ui.resources.liberation_sans_bold
import com.bettertravel.ui.resources.plus_jakarta_sans
import org.jetbrains.compose.resources.Font

@Immutable
data class BtTypography(
    val display: TextStyle,
    val headlineLarge: TextStyle,
    val headline: TextStyle,
    val headlineSemiBold: TextStyle,
    val appTitle: TextStyle,
    val title: TextStyle,
    val titleSmall: TextStyle,
    val button: TextStyle,
    val bodyLarge: TextStyle,
    val body: TextStyle,
    val bodyStrong: TextStyle,
    val bodySmall: TextStyle,
    val bodySmallMedium: TextStyle,
    val bodySmallItalic: TextStyle,
    val label: TextStyle,
    val labelCaps: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
    val eyebrow: TextStyle,
    val metric: TextStyle,
    val mono: TextStyle,
)

@Composable
fun rememberBtTypography(): BtTypography {
    val jakarta =
        FontFamily(
            Font(Res.font.plus_jakarta_sans, FontWeight.Normal, variationSettings = weightVariation(400)),
            Font(Res.font.plus_jakarta_sans, FontWeight.Medium, variationSettings = weightVariation(500)),
            Font(Res.font.plus_jakarta_sans, FontWeight.SemiBold, variationSettings = weightVariation(600)),
            Font(Res.font.plus_jakarta_sans, FontWeight.Bold, variationSettings = weightVariation(700)),
            Font(Res.font.plus_jakarta_sans, FontWeight.ExtraBold, variationSettings = weightVariation(800)),
        )
    val inter =
        FontFamily(
            Font(Res.font.inter, FontWeight.Normal, variationSettings = weightVariation(400)),
            Font(Res.font.inter, FontWeight.Medium, variationSettings = weightVariation(500)),
            Font(Res.font.inter, FontWeight.SemiBold, variationSettings = weightVariation(600)),
            Font(Res.font.inter, FontWeight.Bold, variationSettings = weightVariation(700)),
            Font(Res.font.inter, FontWeight.Normal, FontStyle.Italic, weightVariation(400)),
        )
    val liberation = FontFamily(Font(Res.font.liberation_sans_bold, FontWeight.Bold))
    return remember(jakarta, inter, liberation) {
        createTypography(jakarta, inter, liberation)
    }
}

private fun weightVariation(weight: Int): FontVariation.Settings {
    return FontVariation.Settings(FontVariation.weight(weight))
}

private fun createTypography(
    jakarta: FontFamily,
    inter: FontFamily,
    liberation: FontFamily,
): BtTypography {
    return BtTypography(
        display =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 34.sp,
                lineHeight = 38.sp,
                letterSpacing = (-0.85).sp,
                fontWeight = FontWeight.Bold,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = (-0.33).sp,
                fontWeight = FontWeight.Bold,
            ),
        headline =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = (-0.18).sp,
                fontWeight = FontWeight.Bold,
            ),
        headlineSemiBold =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = (-0.18).sp,
                fontWeight = FontWeight.SemiBold,
            ),
        appTitle =
            TextStyle(
                fontFamily = liberation,
                fontSize = 20.sp,
                lineHeight = 30.sp,
                letterSpacing = (-0.5).sp,
                fontWeight = FontWeight.Bold,
            ),
        title =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = (-0.4).sp,
                fontWeight = FontWeight.Bold,
            ),
        titleSmall =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
            ),
        button =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.14.sp,
                fontWeight = FontWeight.Bold,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = inter,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Normal,
            ),
        body =
            TextStyle(
                fontFamily = inter,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Normal,
            ),
        bodyStrong =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 14.sp,
                lineHeight = 22.75.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        bodySmall =
            TextStyle(
                fontFamily = inter,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.12.sp,
                fontWeight = FontWeight.Normal,
            ),
        bodySmallMedium =
            TextStyle(
                fontFamily = inter,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.12.sp,
                fontWeight = FontWeight.Medium,
            ),
        bodySmallItalic =
            TextStyle(
                fontFamily = inter,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.12.sp,
                fontStyle = FontStyle.Italic,
            ),
        label =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.24.sp,
                fontWeight = FontWeight.Bold,
            ),
        labelCaps =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.6.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        labelMedium =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                letterSpacing = 0.44.sp,
                fontWeight = FontWeight.Bold,
            ),
        labelSmall =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 10.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        eyebrow =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                letterSpacing = 0.55.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        metric =
            TextStyle(
                fontFamily = jakarta,
                fontSize = 18.sp,
                lineHeight = 18.sp,
                letterSpacing = (-0.18).sp,
                fontWeight = FontWeight.Bold,
            ),
        mono =
            TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.12.sp,
            ),
    )
}
