package com.bettertravel.ui.memories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.presentation.memories.MemoriesViewModel
import com.bettertravel.presentation.memories.MemoryFilter
import com.bettertravel.presentation.memories.MemoryItem
import com.bettertravel.presentation.memories.MemoryNote
import com.bettertravel.presentation.memories.MemoryPhoto
import com.bettertravel.presentation.memories.MemorySummary
import com.bettertravel.ui.designsystem.atoms.BtAvatar
import com.bettertravel.ui.designsystem.atoms.BtAvatarRing
import com.bettertravel.ui.designsystem.atoms.BtDot
import com.bettertravel.ui.designsystem.atoms.BtFilterChip
import com.bettertravel.ui.designsystem.atoms.BtIcon
import com.bettertravel.ui.designsystem.atoms.BtPlaceImage
import com.bettertravel.ui.designsystem.molecules.BtEmptyState
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.preview.PreviewMemoriesViewModel
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.betty
import com.bettertravel.ui.resources.ic_bookmark
import com.bettertravel.ui.resources.ic_calendar
import com.bettertravel.ui.resources.ic_clock
import com.bettertravel.ui.resources.ic_heart
import com.bettertravel.ui.resources.ic_note_edit
import com.bettertravel.ui.resources.ic_photo_library
import com.bettertravel.ui.resources.ic_pin
import com.bettertravel.ui.resources.ic_play
import com.bettertravel.ui.resources.ic_rewalk_fork
import com.bettertravel.ui.resources.ic_route_winding
import com.bettertravel.ui.resources.ic_sparkles
import com.bettertravel.ui.rx.collectAsState
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun MemoriesScreen(viewModel: MemoriesViewModel) {
    val summary by viewModel.summary.collectAsState()
    val filters by viewModel.filters.collectAsState()
    val selectedFilterId by viewModel.selectedFilterId.collectAsState()
    val items by viewModel.items.collectAsState()
    MemoriesContent(
        viewModel = viewModel,
        summary = summary,
        filters = filters,
        selectedFilterId = selectedFilterId,
        items = items,
    )
}

@Composable
fun MemoriesContent(
    viewModel: MemoriesViewModel,
    summary: MemorySummary,
    filters: List<MemoryFilter>,
    selectedFilterId: String,
    items: List<MemoryItem>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .background(BtGradients.page),
        contentPadding =
            PaddingValues(top = BtSpacing.l, bottom = BtSpacing.tabBarInset + BtSpacing.xxl),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.l),
    ) {
        item(key = "header") {
            MemoriesHeader(
                title = viewModel.title,
                subtitle = viewModel.subtitle,
                badge = viewModel.levelBadge,
                modifier = Modifier.padding(horizontal = BtSpacing.l),
            )
        }
        item(key = "summary") {
            MemoriesSummaryCard(summary = summary, modifier = Modifier.padding(horizontal = BtSpacing.l))
        }
        item(key = "filters") {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                contentPadding = PaddingValues(horizontal = BtSpacing.l),
            ) {
                items(filters, key = { filter -> filter.id }) { filter ->
                    BtFilterChip(
                        text = filter.title,
                        count = filter.count,
                        selected = filter.id == selectedFilterId,
                        icon = if (filter.isFavorites) Res.drawable.ic_heart else null,
                        onClick = { viewModel.selectFilter(filter) },
                    )
                }
            }
        }
        if (items.isEmpty()) {
            item(key = "empty") {
                BtEmptyState(
                    title = viewModel.title,
                    message = viewModel.emptyMessage,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        items(items, key = { item -> item.id }) { item ->
            when (item) {
                is MemoryItem.Walk -> WalkMemoryCard(viewModel, item, Modifier.padding(horizontal = BtSpacing.l))
                is MemoryItem.SavedPlace -> SavedPlaceCard(viewModel, item, Modifier.padding(horizontal = BtSpacing.l))
            }
        }
    }
}

@Composable
private fun MemoriesHeader(
    title: String,
    subtitle: String,
    badge: String,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
            Text(text = title, style = BetterTravelTheme.typography.display, color = colors.textPrimary)
            Text(text = subtitle, style = BetterTravelTheme.typography.bodySmall, color = colors.textSecondary)
        }
        Row(
            modifier =
                Modifier
                    .padding(top = BtSpacing.s)
                    .clip(CircleShape)
                    .background(colors.coralLight)
                    .padding(horizontal = BtSpacing.m, vertical = BtSpacing.s),
            horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BtIcon(Res.drawable.ic_sparkles, tint = colors.coralText, size = 16.dp)
            Text(text = badge, style = BetterTravelTheme.typography.label, color = colors.coralText)
        }
    }
}

@Composable
private fun MemoriesSummaryCard(
    summary: MemorySummary,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Xs, BtShapes.xl)
                .clip(BtShapes.xl)
                .background(
                    Brush.horizontalGradient(listOf(colors.surfaceContainer, colors.coralLight.copy(alpha = 0.6f))),
                ).padding(BtSpacing.l),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(56.dp).clip(CircleShape).background(colors.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            BtIcon(Res.drawable.ic_photo_library, tint = colors.primary, size = 28.dp)
        }
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.xxs)) {
            Text(
                text = "${summary.moments} ${summary.momentsCaption}",
                style = BetterTravelTheme.typography.headline,
                color = colors.textPrimary,
            )
            Text(text = summary.details, style = BetterTravelTheme.typography.bodySmall, color = colors.textSecondary)
        }
    }
}

@Composable
private fun MemoryCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val clickable = if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Md, BtShapes.xxl)
                .clip(BtShapes.xxl)
                .background(BetterTravelTheme.colors.surface)
                .then(clickable)
                .padding(BtSpacing.l),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.m),
    ) {
        content()
    }
}

@Composable
private fun WalkMemoryCard(
    viewModel: MemoriesViewModel,
    walk: MemoryItem.Walk,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    MemoryCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BtIcon(Res.drawable.ic_calendar, tint = colors.textSecondary, size = 14.dp)
                    Text(
                        text = walk.date,
                        style = BetterTravelTheme.typography.labelMedium,
                        color = colors.textSecondary,
                    )
                    BtDot(colors.textTertiary)
                    Text(
                        text = walk.place,
                        style = BetterTravelTheme.typography.labelMedium,
                        color = colors.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = walk.title,
                    style = BetterTravelTheme.typography.headlineSemiBold,
                    color = colors.textPrimary,
                )
            }
            Row(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(colors.surfaceContainerHigh)
                        .clickable(role = Role.Button) { viewModel.rewalk(walk) }
                        .padding(horizontal = BtSpacing.m, vertical = BtSpacing.s),
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = viewModel.rewalkTitle, style = BetterTravelTheme.typography.label, color = colors.primary)
                BtIcon(Res.drawable.ic_rewalk_fork, tint = colors.primary, size = 14.dp)
            }
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(BtShapes.xs)
                    .background(colors.surfaceContainer)
                    .padding(horizontal = BtSpacing.m, vertical = BtSpacing.s),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WalkMetric(Res.drawable.ic_route_winding, walk.distance)
            BtDot(colors.outlineVariant)
            WalkMetric(Res.drawable.ic_pin, walk.stops)
            BtDot(colors.outlineVariant)
            WalkMetric(Res.drawable.ic_clock, walk.duration)
        }
        MemoryPhotos(photos = walk.photos)
        when (val note = walk.note) {
            is MemoryNote.Voice -> VoiceNote(note)
            is MemoryNote.Reflection -> ReflectionNote(note)
            null -> Unit
        }
    }
}

@Composable
private fun WalkMetric(
    icon: DrawableResource,
    text: String,
) {
    val colors = BetterTravelTheme.colors
    Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs), verticalAlignment = Alignment.CenterVertically) {
        BtIcon(icon, tint = colors.primary, size = 16.dp)
        Text(text = text, style = BetterTravelTheme.typography.label, color = colors.textPrimary)
    }
}

@Composable
private fun MemoryPhotos(
    photos: List<MemoryPhoto>,
    singleHeight: androidx.compose.ui.unit.Dp = PHOTO_HEIGHT_SINGLE,
) {
    // TODO: show real photos once memories can capture them
    Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
        photos.forEachIndexed { index, photo ->
            val colors = BetterTravelTheme.colors
            val tints = listOf(colors.primaryContainer, colors.coralLight, colors.gold)
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .height(if (photos.size == 1) singleHeight else PHOTO_HEIGHT)
                        .clip(BtShapes.xs)
                        .background(
                            Brush.verticalGradient(listOf(tints[index % tints.size], colors.surfaceContainerHigh)),
                        ),
            ) {
                BtPlaceImage(modifier = Modifier.align(Alignment.Center).fillMaxSize().padding(BtSpacing.m))
                Text(
                    text = photo.label,
                    style = BetterTravelTheme.typography.labelMedium,
                    color = colors.textOnPrimary,
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(BtSpacing.s)
                            .clip(CircleShape)
                            .background(colors.photoCaption)
                            .padding(horizontal = BtSpacing.s, vertical = BtSpacing.xxs),
                )
            }
        }
    }
}

@Composable
private fun VoiceNote(note: MemoryNote.Voice) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(BtShapes.xs)
                .background(colors.surfaceContainer)
                .padding(BtSpacing.m),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
    ) {
        BtAvatar(image = Res.drawable.betty, size = 40.dp, ring = BtAvatarRing.None)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = note.title, style = BetterTravelTheme.typography.label, color = colors.primary)
                Text(text = note.length, style = BetterTravelTheme.typography.mono, color = colors.textSecondary)
            }
            Text(
                text = note.quote,
                style = BetterTravelTheme.typography.bodySmall,
                color = colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(colors.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    BtIcon(Res.drawable.ic_play, tint = colors.textOnPrimary, size = 14.dp)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    WAVEFORM.forEachIndexed { index, height ->
                        Box(
                            modifier =
                                Modifier
                                    .size(width = 3.dp, height = height)
                                    .clip(CircleShape)
                                    .background(if (index < WAVEFORM_PLAYED) colors.brand else colors.outlineVariant),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReflectionNote(note: MemoryNote.Reflection) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(BtShapes.xs)
                .background(colors.surfaceContainer)
                .padding(BtSpacing.m),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(colors.coralLight),
            contentAlignment = Alignment.Center,
        ) {
            BtIcon(Res.drawable.ic_note_edit, tint = colors.coralText, size = 18.dp)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
            Text(text = note.title, style = BetterTravelTheme.typography.label, color = colors.textPrimary)
            Text(text = note.text, style = BetterTravelTheme.typography.bodySmall, color = colors.textSecondary)
        }
    }
}

@Composable
private fun SavedPlaceCard(
    viewModel: MemoriesViewModel,
    place: MemoryItem.SavedPlace,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    MemoryCard(modifier = modifier, onClick = { viewModel.open(place) }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BtIcon(Res.drawable.ic_heart, tint = colors.coralText, size = 14.dp)
                    Text(
                        text = place.date,
                        style = BetterTravelTheme.typography.labelMedium,
                        color = colors.textSecondary,
                    )
                    BtDot(colors.textTertiary)
                    Text(
                        text = viewModel.savedPlaceTitle,
                        style = BetterTravelTheme.typography.labelMedium,
                        color = colors.coralText,
                    )
                }
                Text(
                    text = place.name,
                    style = BetterTravelTheme.typography.headlineSemiBold,
                    color = colors.textPrimary,
                )
                Text(
                    text = place.routeTitle,
                    style = BetterTravelTheme.typography.bodySmall,
                    color = colors.textSecondary,
                )
            }
            Box(
                modifier =
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colors.coralLight)
                        .clickable(role = Role.Button) { viewModel.removeBookmark(place) },
                contentAlignment = Alignment.Center,
            ) {
                BtIcon(Res.drawable.ic_bookmark, tint = colors.coralText, size = 20.dp)
            }
        }
        MemoryPhotos(photos = listOf(MemoryPhoto(place.name)), singleHeight = PHOTO_HEIGHT)
    }
}

private val PHOTO_HEIGHT = 140.dp
private val PHOTO_HEIGHT_SINGLE = 200.dp
private val WAVEFORM = listOf(8.dp, 14.dp, 20.dp, 12.dp, 16.dp, 10.dp, 18.dp, 8.dp, 14.dp, 10.dp)
private const val WAVEFORM_PLAYED = 4

@Preview
@Composable
private fun MemoriesScreenPreview() {
    BtPreview {
        MemoriesScreen(PreviewMemoriesViewModel())
    }
}
