package com.bettertravel.ui.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtBadge
import com.bettertravel.ui.designsystem.atoms.BtBadgeStyle
import com.bettertravel.ui.designsystem.atoms.BtButton
import com.bettertravel.ui.designsystem.atoms.BtButtonStyle
import com.bettertravel.ui.designsystem.atoms.BtDot
import com.bettertravel.ui.designsystem.atoms.BtIcon
import com.bettertravel.ui.designsystem.atoms.BtIconButton
import com.bettertravel.ui.designsystem.atoms.BtIconButtonStyle
import com.bettertravel.ui.designsystem.atoms.BtMarkerStyle
import com.bettertravel.ui.designsystem.atoms.BtNumberMarker
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_book_open
import com.bettertravel.ui.resources.ic_calendar
import com.bettertravel.ui.resources.ic_end_session
import com.bettertravel.ui.resources.ic_mic
import com.bettertravel.ui.resources.ic_navigation
import com.bettertravel.ui.resources.ic_note_edit
import com.bettertravel.ui.resources.ic_rewalk_fork
import com.bettertravel.ui.resources.ic_turn_right

@Composable
fun BtEmbeddedRouteCard(
    title: String,
    duration: String,
    stops: List<String>,
    previewLabel: String,
    startLabel: String,
    onPreview: () -> Unit,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(BtShapes.md)
                .background(colors.skyTint.copy(alpha = 0.35f))
                .border(1.dp, colors.skyTint, BtShapes.md)
                .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.m),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = title, style = BetterTravelTheme.typography.label, color = colors.textPrimary)
            BtBadge(duration, style = BtBadgeStyle.Sky)
        }
        stops.forEachIndexed { index, stop ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BtNumberMarker(index + 1, style = BtMarkerStyle.Outlined, size = 20.dp)
                Text(text = stop, style = BetterTravelTheme.typography.label.copy(fontWeight = FontWeight.Medium))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtButton(
                text = previewLabel,
                onClick = onPreview,
                style = BtButtonStyle.Outlined,
                modifier = Modifier.weight(1f),
            )
            BtButton(
                text = startLabel,
                onClick = onStart,
                style = BtButtonStyle.Brand,
                leadingIcon = Res.drawable.ic_navigation,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun BtTurnGuidanceCard(
    distance: String,
    mode: String,
    instruction: String,
    next: String,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Xl, BtShapes.xxl)
                .clip(BtShapes.xxl)
                .background(colors.glassStrong)
                .padding(BtSpacing.l),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.l),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(56.dp)
                    .btShadow(BtElevation.Md, CircleShape)
                    .clip(CircleShape)
                    .background(colors.primary),
            contentAlignment = Alignment.Center,
        ) {
            BtIcon(Res.drawable.ic_turn_right, tint = colors.textOnPrimary, size = 24.dp)
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = distance, style = BetterTravelTheme.typography.eyebrow, color = colors.coralText)
                BtDot(colors.coral)
                Text(text = mode, style = BetterTravelTheme.typography.labelMedium, color = colors.textSecondary)
            }
            Text(
                text = instruction,
                style = BetterTravelTheme.typography.headline,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(text = next, style = BetterTravelTheme.typography.bodySmall, color = colors.textSecondary)
        }
    }
}

@Composable
fun BtMemoryCard(
    date: String,
    location: String,
    title: String,
    metrics: List<String>,
    rewalkLabel: String,
    onRewalk: () -> Unit,
    modifier: Modifier = Modifier,
    media: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Md, BtShapes.xxl)
                .clip(BtShapes.xxl)
                .background(colors.surface)
                .padding(BtSpacing.l),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.l),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BtIcon(Res.drawable.ic_calendar, tint = colors.textSecondary, size = 10.dp)
                    Text(text = date, style = BetterTravelTheme.typography.labelMedium, color = colors.textSecondary)
                    Text(text = "• $location", style = BetterTravelTheme.typography.labelMedium, color = colors.primary)
                }
                Text(text = title, style = BetterTravelTheme.typography.headlineSemiBold, color = colors.textPrimary)
            }
            BtButton(
                text = rewalkLabel,
                onClick = onRewalk,
                style = BtButtonStyle.Secondary,
                leadingIcon = Res.drawable.ic_rewalk_fork,
            )
        }
        BtMetricsStrip(metrics)
        media?.invoke()
        footer?.invoke()
    }
}

@Composable
fun BtMetricsStrip(
    metrics: List<String>,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(BtShapes.sm)
                .background(colors.surfaceContainer)
                .padding(horizontal = BtSpacing.m, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.l),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        metrics.forEachIndexed { index, metric ->
            if (index > 0) {
                Text(text = "•", color = colors.textSecondary.copy(alpha = 0.3f))
            }
            Text(text = metric, style = BetterTravelTheme.typography.label, color = colors.textSecondary)
        }
    }
}

@Composable
fun BtJournalNote(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(BtShapes.sm)
                .background(colors.surfaceContainer)
                .padding(BtSpacing.s),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
    ) {
        Box(
            modifier =
                Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(colors.coral.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center,
        ) {
            BtIcon(Res.drawable.ic_note_edit, tint = colors.coralDeep, size = 14.dp)
        }
        Column {
            Text(text = title, style = BetterTravelTheme.typography.labelMedium, color = colors.textPrimary)
            Text(text = body, style = BetterTravelTheme.typography.bodySmall, color = colors.textSecondary)
        }
    }
}

@Composable
fun BtTranscriptCard(
    header: String,
    liveLabel: String,
    entries: List<BtTranscriptEntry>,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            modifier
                .widthIn(max = 384.dp)
                .fillMaxWidth()
                .btShadow(BtElevation.Md, BtShapes.xl)
                .clip(BtShapes.xl)
                .background(colors.glass)
                .border(1.dp, colors.glassBorder, BtShapes.xl)
                .padding(21.dp),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.s),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = header, style = BetterTravelTheme.typography.labelCaps, color = colors.textTertiary)
            BtBadge(liveLabel, style = BtBadgeStyle.Sky, shape = BtShapes.xs)
        }
        entries.forEach { entry ->
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(
                    text = entry.speaker,
                    style = BetterTravelTheme.typography.label,
                    color = if (entry.fromUser) colors.textTertiary else colors.brand,
                )
                Text(
                    text = entry.text,
                    style = BetterTravelTheme.typography.bodyStrong,
                    color = if (entry.fromUser) colors.textTertiary else colors.textPrimary,
                )
            }
        }
    }
}

data class BtTranscriptEntry(
    val speaker: String,
    val text: String,
    val fromUser: Boolean,
)

@Composable
fun BtVoiceControlDock(
    onKeyboard: () -> Unit,
    onMic: () -> Unit,
    onEnd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .widthIn(max = 384.dp)
                .fillMaxWidth()
                .btShadow(BtElevation.Lg, CircleShape)
                .clip(CircleShape)
                .background(colors.glass)
                .border(1.dp, colors.glassBorder, CircleShape)
                .padding(horizontal = 21.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BtIconButton(Res.drawable.ic_book_open, onClick = onKeyboard, size = 48.dp)
        BtIconButton(
            Res.drawable.ic_mic,
            onClick = onMic,
            style = BtIconButtonStyle.Hero,
            size = 64.dp,
            iconSize = 28.dp,
        )
        BtIconButton(Res.drawable.ic_end_session, onClick = onEnd, size = 48.dp)
    }
}

@Preview
@Composable
private fun BtEmbeddedRouteCardPreview() {
    BtPreview {
        BtEmbeddedRouteCard(
            title = "Old Town & Courtyards",
            duration = "2h 45m • 3.2 km",
            stops = listOf("Old Town Market Square", "St. John's Vaults & Bell View", "Freta Artisan Café"),
            previewLabel = "Preview on map",
            startLabel = "Start walk",
            onPreview = {},
            onStart = {},
        )
    }
}

@Preview
@Composable
private fun BtTurnGuidanceCardPreview() {
    BtPreview {
        BtTurnGuidanceCard(
            distance = "IN 80 METERS",
            mode = "Walking mode",
            instruction = "Turn right onto Royal Way",
            next = "Next: St. John's Archcathedral",
        )
    }
}

@Preview
@Composable
private fun BtMemoryCardPreview() {
    BtPreview {
        BtMemoryCard(
            date = "Feb 12, 2026",
            location = "Warsaw, Poland",
            title = "Warsaw Old Town historic loop",
            metrics = listOf("3.4 km", "4 stops", "1h 52m"),
            rewalkLabel = "Re-walk",
            onRewalk = {},
            footer = {
                BtJournalNote(title = "Traveler reflection", body = "The cathedral crypts were unforgettable.")
            },
        )
    }
}

@Preview
@Composable
private fun BtMetricsStripPreview() {
    BtPreview {
        BtMetricsStrip(listOf("3.4 km", "4 stops", "1h 52m"))
    }
}

@Preview
@Composable
private fun BtJournalNotePreview() {
    BtPreview {
        BtJournalNote(title = "Traveler reflection", body = "Loved the courtyard café.")
    }
}

@Preview
@Composable
private fun BtTranscriptCardPreview() {
    BtPreview {
        BtTranscriptCard(
            header = "REAL-TIME TRANSCRIPT",
            liveLabel = "Live",
            entries =
                listOf(
                    BtTranscriptEntry("You", "Find me a quiet café near the Royal Castle.", fromUser = true),
                    BtTranscriptEntry(
                        "Betty",
                        "There's an artisan bakery tucked in a courtyard 4 minutes away.",
                        false,
                    ),
                ),
        )
    }
}

@Preview
@Composable
private fun BtVoiceControlDockPreview() {
    BtPreview {
        BtVoiceControlDock(onKeyboard = {}, onMic = {}, onEnd = {})
    }
}
