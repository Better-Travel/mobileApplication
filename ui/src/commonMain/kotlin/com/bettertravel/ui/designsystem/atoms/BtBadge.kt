package com.bettertravel.ui.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtIconSize
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_star
import org.jetbrains.compose.resources.DrawableResource

enum class BtBadgeStyle {
    Primary,
    PrimaryContainer,
    Coral,
    CoralLight,
    Sky,
    Neutral,
    Gold,
}

@Composable
fun BtBadge(
    text: String,
    modifier: Modifier = Modifier,
    style: BtBadgeStyle = BtBadgeStyle.Neutral,
    shape: Shape = CircleShape,
    icon: DrawableResource? = null,
    textStyle: TextStyle = BetterTravelTheme.typography.labelMedium,
) {
    val colors = BetterTravelTheme.colors
    val container: Color
    val content: Color
    when (style) {
        BtBadgeStyle.Primary -> {
            container = colors.primary.copy(alpha = 0.1f)
            content = colors.primary
        }

        BtBadgeStyle.PrimaryContainer -> {
            container = colors.primaryContainer
            content = colors.onPrimaryContainer
        }

        BtBadgeStyle.Coral -> {
            container = colors.coral.copy(alpha = 0.2f)
            content = colors.coralText
        }

        BtBadgeStyle.CoralLight -> {
            container = colors.coralLight
            content = colors.coralInk
        }

        BtBadgeStyle.Sky -> {
            container = colors.skyTint
            content = colors.primary
        }

        BtBadgeStyle.Neutral -> {
            container = colors.surfaceContainerHighest
            content = colors.textSecondary
        }

        BtBadgeStyle.Gold -> {
            container = colors.gold.copy(alpha = 0.6f)
            content = colors.goldDark
        }
    }
    Row(
        modifier =
            modifier
                .clip(shape)
                .background(container)
                .padding(horizontal = BtSpacing.s, vertical = BtSpacing.xxs),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            BtIcon(icon, tint = content, size = BtIconSize.xs)
        }
        Text(text = text, style = textStyle, color = content)
    }
}

@Composable
fun BtDurationTag(
    text: String,
    modifier: Modifier = Modifier,
    warm: Boolean = false,
) {
    BtBadge(
        text = text,
        modifier = modifier,
        style = if (warm) BtBadgeStyle.Coral else BtBadgeStyle.Primary,
        shape = BtShapes.xs,
    )
}

@Composable
fun BtRating(
    rating: String,
    modifier: Modifier = Modifier,
    reviews: String? = null,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BtIcon(Res.drawable.ic_star, tint = colors.goldDark, size = 10.dp)
        Text(
            text = rating,
            style = BetterTravelTheme.typography.labelMedium.copy(letterSpacing = 0.sp),
            color = colors.goldDark,
        )
        if (reviews != null) {
            Text(text = "($reviews)", style = BetterTravelTheme.typography.labelMedium, color = colors.textTertiary)
        }
    }
}

@Preview
@Composable
private fun BtBadgePreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtBadge("Walkable", style = BtBadgeStyle.PrimaryContainer)
            BtBadge("Arriving", style = BtBadgeStyle.CoralLight)
            BtBadge("Level 4", style = BtBadgeStyle.Gold)
            BtBadge("Open now")
            BtBadge("4.8", style = BtBadgeStyle.Neutral, icon = Res.drawable.ic_star)
        }
    }
}

@Preview
@Composable
private fun BtDurationTagPreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtDurationTag("30 min")
            BtDurationTag("25 min", warm = true)
        }
    }
}

@Preview
@Composable
private fun BtRatingPreview() {
    BtPreview {
        BtRating(rating = "4.9", reviews = "1.2k")
    }
}
