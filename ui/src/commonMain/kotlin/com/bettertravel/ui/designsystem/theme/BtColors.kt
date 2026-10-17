package com.bettertravel.ui.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable
data class BtColors(
    val background: Color,
    val surface: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceContainerMax: Color,
    val glass: Color,
    val glassStrong: Color,
    val glassBorder: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textOnPrimary: Color,
    val placeholder: Color,
    val outline: Color,
    val outlineVariant: Color,
    val primary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val brand: Color,
    val brandLight: Color,
    val brandDeep: Color,
    val skyTint: Color,
    val coral: Color,
    val coralLight: Color,
    val coralSoft: Color,
    val coralText: Color,
    val coralDeep: Color,
    val coralInk: Color,
    val gold: Color,
    val goldDark: Color,
    val online: Color,
    val scrim: Color,
    val photoCaption: Color,
)

val BtLightColors: BtColors =
    BtColors(
        background = Color(0xFFFAF8FF),
        surface = Color(0xFFFFFFFF),
        surfaceContainer = Color(0xFFF2F3FF),
        surfaceContainerHigh = Color(0xFFEAEDFF),
        surfaceContainerHighest = Color(0xFFE2E7FF),
        surfaceContainerMax = Color(0xFFDAE2FD),
        glass = Color(0xE6FFFFFF),
        glassStrong = Color(0xF2FFFFFF),
        glassBorder = Color(0xCCFFFFFF),
        textPrimary = Color(0xFF131B2E),
        textSecondary = Color(0xFF3F484F),
        textTertiary = Color(0xFF6F7880),
        textOnPrimary = Color(0xFFFFFFFF),
        placeholder = Color(0xFF94A3B8),
        outline = Color(0xFFE2E8F0),
        outlineVariant = Color(0xFFBFC7D0),
        primary = Color(0xFF006590),
        primaryContainer = Color(0xFFC8E6FF),
        onPrimaryContainer = Color(0xFF004C6D),
        brand = Color(0xFF4EA8DE),
        brandLight = Color(0xFF65BAED),
        brandDeep = Color(0xFF3A86FF),
        skyTint = Color(0xFFE0F2FE),
        coral = Color(0xFFFE9D7C),
        coralLight = Color(0xFFFFDBD0),
        coralSoft = Color(0xFFFFB59C),
        coralText = Color(0xFF94492E),
        coralDeep = Color(0xFF773219),
        coralInk = Color(0xFF390C00),
        gold = Color(0xFFFFDF9B),
        goldDark = Color(0xFF785A00),
        online = Color(0xFF10B981),
        scrim = Color(0x66131B2E),
        photoCaption = Color(0x99131B2E),
    )

object BtGradients {
    val progress: Brush = Brush.horizontalGradient(listOf(Color(0xFF006590), Color(0xFFFE9D7C)))
    val routeComplete: Brush =
        Brush.horizontalGradient(
            listOf(Color(0xFFFE9D7C), Color(0xFFFFB59C), Color(0xFFFFDF9B)),
        )
    val xpBadge: Brush = Brush.linearGradient(listOf(Color(0xFF785A00), Color(0xFFFFDF9B)))
    val companionRing: Brush =
        Brush.linearGradient(
            listOf(Color(0xFFFE9D7C), Color(0xFF4EA8DE), Color(0xFFFAF8FF)),
        )
    val miniRing: Brush = Brush.linearGradient(listOf(Color(0xFFFFDBD0), Color(0xFFC8E6FF)))
    val mapRing: Brush = Brush.linearGradient(listOf(Color(0xFF4EA8DE), Color(0xFFFE9D7C)))
    val micButton: Brush = Brush.linearGradient(listOf(Color(0xFF4EA8DE), Color(0xFF65BAED)))
    val userBubble: Brush = Brush.linearGradient(listOf(Color(0xFF4EA8DE), Color(0xFF3A86FF)))
    val heading: Brush =
        Brush.linearGradient(
            listOf(Color(0xFF3EA5E6), Color(0xFF60A5FA), Color(0xFF94A3B8)),
        )
    val avatarRing: Brush =
        Brush.linearGradient(
            listOf(Color(0xFF7DD3FC), Color(0xFFFBCFE8), Color(0xFFFDE68A)),
        )
    val page: Brush =
        Brush.verticalGradient(
            listOf(Color(0xFFFAF8FF), Color(0xFFF2F3FF), Color(0xFFEAEDFF)),
        )
    val voice: Brush =
        Brush.verticalGradient(
            listOf(Color(0xFFF2F8FD), Color(0xFFF8FAFC), Color(0xFFEFF6FB)),
        )
    val halo: Brush =
        Brush.radialGradient(
            listOf(Color(0x80BADDFF), Color(0x40FED7E2), Color(0x00FED7E2)),
        )
    val warmAura: Brush =
        Brush.radialGradient(
            listOf(Color(0x66FE9D7C), Color(0x66FFDF9B), Color(0x004EA8DE)),
        )
}
