package com.bettertravel.ui.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettertravel.ui.designsystem.atoms.BtBadge
import com.bettertravel.ui.designsystem.atoms.BtBadgeStyle
import com.bettertravel.ui.designsystem.atoms.BtButton
import com.bettertravel.ui.designsystem.atoms.BtButtonStyle
import com.bettertravel.ui.designsystem.atoms.BtDragHandle
import com.bettertravel.ui.designsystem.atoms.BtMarkerStyle
import com.bettertravel.ui.designsystem.atoms.BtProgressBar
import com.bettertravel.ui.designsystem.molecules.BtStat
import com.bettertravel.ui.designsystem.molecules.BtStatsStrip
import com.bettertravel.ui.designsystem.molecules.BtWaypointCard
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.betty
import com.bettertravel.ui.resources.ic_clock
import com.bettertravel.ui.resources.ic_flag
import com.bettertravel.ui.resources.ic_navigation
import com.bettertravel.ui.resources.ic_pause_circle
import com.bettertravel.ui.resources.ic_walk
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun BtBottomSheet(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Lg, BtShapes.sheet)
                .clip(BtShapes.sheet)
                .background(BetterTravelTheme.colors.glassStrong)
                .padding(start = BtSpacing.l, end = BtSpacing.l, top = BtSpacing.m, bottom = BtSpacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BtSpacing.l),
    ) {
        BtDragHandle()
        content()
    }
}

data class BtWaypoint(
    val order: Int,
    val markerStyle: BtMarkerStyle,
    val title: String,
    val description: String,
    val duration: String,
    val rating: String,
    val reviews: String,
    val departure: String?,
    val thumbnail: DrawableResource,
    val warm: Boolean = false,
)

@Composable
fun BtItinerarySheet(
    stats: List<BtStat>,
    sectionTitle: String,
    walkableLabel: String,
    waypoints: List<BtWaypoint>,
    startLabel: String,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BtBottomSheet(modifier = modifier) {
        BtStatsStrip(stats)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = sectionTitle,
                style = BetterTravelTheme.typography.headline,
                color = BetterTravelTheme.colors.textPrimary,
            )
            BtBadge(walkableLabel, style = BtBadgeStyle.PrimaryContainer)
        }
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
            waypoints.forEach { waypoint ->
                BtWaypointCard(
                    order = waypoint.order,
                    markerStyle = waypoint.markerStyle,
                    title = waypoint.title,
                    description = waypoint.description,
                    duration = waypoint.duration,
                    rating = waypoint.rating,
                    reviews = waypoint.reviews,
                    departure = waypoint.departure,
                    thumbnail = waypoint.thumbnail,
                    warm = waypoint.warm,
                )
            }
        }
        BtButton(
            text = startLabel,
            onClick = onStart,
            style = BtButtonStyle.Brand,
            leadingIcon = Res.drawable.ic_navigation,
        )
    }
}

@Composable
fun BtDestinationCard(
    arrival: String,
    step: String,
    title: String,
    remaining: String,
    progress: Float,
    progressStart: String,
    progressEnd: String,
    pauseLabel: String,
    guideLabel: String,
    onPause: () -> Unit,
    onGuide: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Xxl, BtShapes.xxl)
                .clip(BtShapes.xxl)
                .background(colors.glassStrong)
                .padding(BtSpacing.l),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.l),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BtBadge(arrival, style = BtBadgeStyle.CoralLight)
            Text(text = step, style = BetterTravelTheme.typography.bodySmallMedium, color = colors.textSecondary)
        }
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
            Text(text = title, style = BetterTravelTheme.typography.headlineLarge, color = colors.textPrimary)
            Text(text = remaining, style = BetterTravelTheme.typography.bodySmall, color = colors.textSecondary)
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = progressStart,
                    style = BetterTravelTheme.typography.labelMedium.copy(letterSpacing = 0.44.sp),
                    color = colors.textSecondary,
                )
                Text(text = progressEnd, style = BetterTravelTheme.typography.labelMedium, color = colors.primary)
            }
            BtProgressBar(progress = progress)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtButton(
                text = pauseLabel,
                onClick = onPause,
                style = BtButtonStyle.Secondary,
                leadingIcon = Res.drawable.ic_pause_circle,
                modifier = Modifier.weight(1f),
            )
            BtButton(
                text = guideLabel,
                onClick = onGuide,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
private fun BtItinerarySheetPreview() {
    BtPreview {
        BtItinerarySheet(
            stats =
                listOf(
                    BtStat("2h 15m", "Total time", Res.drawable.ic_clock, BetterTravelTheme.colors.primary),
                    BtStat("3.4 km", "Walking", Res.drawable.ic_walk, BetterTravelTheme.colors.coralText),
                    BtStat("4 stops", "Curated", Res.drawable.ic_flag, BetterTravelTheme.colors.goldDark),
                ),
            sectionTitle = "Today's sequence",
            walkableLabel = "Walkable",
            waypoints =
                listOf(
                    BtWaypoint(
                        1,
                        BtMarkerStyle.Primary,
                        "Old Town Market Square",
                        "Historic landmark, vibrant cafés",
                        "30 min",
                        "4.9",
                        "1.2k",
                        "Departure 10:00 AM",
                        Res.drawable.betty,
                    ),
                    BtWaypoint(
                        2,
                        BtMarkerStyle.Primary,
                        "St. John's Archcathedral",
                        "Gothic cathedral with a crypt",
                        "25 min",
                        "4.8",
                        "1.4k",
                        null,
                        Res.drawable.betty,
                    ),
                ),
            startLabel = "Start route navigation",
            onStart = {},
        )
    }
}

@Preview
@Composable
private fun BtBottomSheetPreview() {
    BtPreview {
        BtBottomSheet {
            Text(text = "Sheet content", style = BetterTravelTheme.typography.body)
        }
    }
}

@Preview
@Composable
private fun BtDestinationCardPreview() {
    BtPreview {
        BtDestinationCard(
            arrival = "Arriving in 2 min",
            step = "Step 3 of 6",
            title = "St. John's Archcathedral",
            remaining = "3 min • 180 m remaining",
            progress = 0.85f,
            progressStart = "Old Town Market Square",
            progressEnd = "85% complete",
            pauseLabel = "Pause walk",
            guideLabel = "View guide",
            onPause = {},
            onGuide = {},
        )
    }
}
