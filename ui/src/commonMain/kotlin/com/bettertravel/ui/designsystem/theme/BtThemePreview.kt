package com.bettertravel.ui.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.preview.BtPreview

@Composable
private fun Swatch(
    name: String,
    color: Color,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Column(
            modifier =
                Modifier
                    .size(56.dp)
                    .clip(BtShapes.sm)
                    .background(color)
                    .border(1.dp, BetterTravelTheme.colors.outline, BtShapes.sm),
        ) {}
        Text(
            text = name,
            style = BetterTravelTheme.typography.labelSmall,
            color = BetterTravelTheme.colors.textSecondary,
        )
    }
}

@Preview
@Composable
private fun BtColorsPreview() {
    BtPreview {
        val c = BetterTravelTheme.colors
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Swatch("background", c.background)
            Swatch("container", c.surfaceContainer)
            Swatch("high", c.surfaceContainerHigh)
            Swatch("highest", c.surfaceContainerHighest)
            Swatch("text", c.textPrimary)
            Swatch("text 2", c.textSecondary)
            Swatch("text 3", c.textTertiary)
            Swatch("primary", c.primary)
            Swatch("brand", c.brand)
            Swatch("coral", c.coral)
            Swatch("coralText", c.coralText)
            Swatch("gold", c.gold)
            Swatch("online", c.online)
        }
    }
}

@Preview
@Composable
private fun BtTypographyPreview() {
    BtPreview {
        val t = BetterTravelTheme.typography
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Let's plan your trip", style = t.display)
            Text("Headline large 22", style = t.headlineLarge)
            Text("Headline 18", style = t.headline)
            Text("BetterTravel app title", style = t.appTitle)
            Text("Title 16", style = t.title)
            Text("Button 14", style = t.button)
            Text("Body large 16 Inter", style = t.bodyLarge)
            Text("Body 14 Inter", style = t.body)
            Text("Body small 12 Inter", style = t.bodySmall)
            Text("Label 12", style = t.label)
            Text("EYEBROW 11", style = t.eyebrow)
            Text("Label small 10", style = t.labelSmall)
        }
    }
}

@Preview
@Composable
private fun BtSpacingAndShapesPreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
            listOf(BtShapes.xs, BtShapes.sm, BtShapes.md, BtShapes.xl, BtShapes.xxl, BtShapes.pill).forEach { shape ->
                Column(
                    modifier =
                        Modifier
                            .size(44.dp)
                            .clip(shape)
                            .background(BetterTravelTheme.colors.primaryContainer),
                ) {}
            }
        }
    }
}
