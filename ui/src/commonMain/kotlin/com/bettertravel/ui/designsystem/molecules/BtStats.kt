package com.bettertravel.ui.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtIcon
import com.bettertravel.ui.designsystem.atoms.BtVerticalDivider
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_clock
import com.bettertravel.ui.resources.ic_flag
import com.bettertravel.ui.resources.ic_walk
import org.jetbrains.compose.resources.DrawableResource

data class BtStat(
    val value: String,
    val caption: String,
    val icon: DrawableResource,
    val tint: Color,
)

@Composable
fun BtStatTile(
    stat: BtStat,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            modifier
                .btShadow(BtElevation.Xs, BtShapes.md)
                .clip(BtShapes.md)
                .background(colors.surfaceContainer)
                .padding(horizontal = BtSpacing.xs, vertical = BtSpacing.l),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BtSpacing.xs),
    ) {
        Box(
            modifier =
                Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceContainerHigh),
            contentAlignment = Alignment.Center,
        ) {
            BtIcon(stat.icon, tint = stat.tint, size = 16.dp)
        }
        Text(text = stat.value, style = BetterTravelTheme.typography.headline, color = colors.textPrimary)
        Text(
            text = stat.caption.uppercase(),
            style = BetterTravelTheme.typography.labelMedium,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun BtStatsStrip(
    stats: List<BtStat>,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clip(BtShapes.md)
                .background(colors.surfaceContainer.copy(alpha = 0.8f))
                .padding(BtSpacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        stats.forEachIndexed { index, stat ->
            if (index > 0) {
                BtVerticalDivider(modifier = Modifier.height(24.dp))
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(BtSpacing.xs),
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(stat.tint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                    BtIcon(stat.icon, tint = stat.tint, size = 14.dp)
                }
                Text(text = stat.value, style = BetterTravelTheme.typography.metric, color = colors.textPrimary)
                Text(text = stat.caption, style = BetterTravelTheme.typography.labelSmall, color = colors.textSecondary)
            }
        }
    }
}

@Composable
private fun previewStats(): List<BtStat> {
    val colors = BetterTravelTheme.colors
    return listOf(
        BtStat("2h 15m", "Total time", Res.drawable.ic_clock, colors.primary),
        BtStat("3.4 km", "Walking", Res.drawable.ic_walk, colors.coralText),
        BtStat("4 stops", "Curated", Res.drawable.ic_flag, colors.goldDark),
    )
}

@Preview
@Composable
private fun BtStatTilePreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            previewStats().forEach { stat ->
                BtStatTile(stat, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
private fun BtStatsStripPreview() {
    BtPreview {
        BtStatsStrip(previewStats())
    }
}
