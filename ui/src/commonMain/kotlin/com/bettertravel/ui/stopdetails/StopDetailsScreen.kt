package com.bettertravel.ui.stopdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.presentation.details.StopDetailsEssential
import com.bettertravel.presentation.details.StopDetailsHighlight
import com.bettertravel.presentation.details.StopDetailsMetric
import com.bettertravel.presentation.details.StopDetailsViewModel
import com.bettertravel.ui.designsystem.atoms.BtBadge
import com.bettertravel.ui.designsystem.atoms.BtBadgeStyle
import com.bettertravel.ui.designsystem.atoms.BtButton
import com.bettertravel.ui.designsystem.atoms.BtButtonStyle
import com.bettertravel.ui.designsystem.atoms.BtIcon
import com.bettertravel.ui.designsystem.atoms.BtIconButton
import com.bettertravel.ui.designsystem.atoms.BtIconButtonStyle
import com.bettertravel.ui.designsystem.atoms.BtPlaceImage
import com.bettertravel.ui.designsystem.molecules.BtDetailTopBar
import com.bettertravel.ui.designsystem.molecules.BtInfoRow
import com.bettertravel.ui.designsystem.molecules.BtInsightCard
import com.bettertravel.ui.designsystem.molecules.BtSectionHeader
import com.bettertravel.ui.designsystem.organisms.BtMapPlaceholder
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.preview.PreviewStopDetailsViewModel
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_architecture
import com.bettertravel.ui.resources.ic_arrow_right
import com.bettertravel.ui.resources.ic_bookmark
import com.bettertravel.ui.resources.ic_check
import com.bettertravel.ui.resources.ic_chevron_right
import com.bettertravel.ui.resources.ic_clock
import com.bettertravel.ui.resources.ic_door_open_hours
import com.bettertravel.ui.resources.ic_flag
import com.bettertravel.ui.resources.ic_info_circle
import com.bettertravel.ui.resources.ic_medal_star
import com.bettertravel.ui.resources.ic_navigate
import com.bettertravel.ui.resources.ic_pin
import com.bettertravel.ui.resources.ic_sparkles
import com.bettertravel.ui.resources.ic_star
import com.bettertravel.ui.resources.ic_swap_horizontal
import com.bettertravel.ui.resources.ic_ticket
import com.bettertravel.ui.resources.ic_verified
import com.bettertravel.ui.rx.collectAsState
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun StopDetailsScreen(viewModel: StopDetailsViewModel) {
    val isBookmarked by viewModel.isBookmarked.collectAsState()
    StopDetailsContent(viewModel = viewModel, isBookmarked = isBookmarked)
}

@Composable
fun StopDetailsContent(
    viewModel: StopDetailsViewModel,
    isBookmarked: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(BtGradients.page)
                .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        BtDetailTopBar(eyebrow = viewModel.eyebrow, title = viewModel.screenTitle, onBackClick = { viewModel.back() })
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(BtSpacing.l),
        ) {
            StopHero(viewModel = viewModel, isBookmarked = isBookmarked)
            Column(
                modifier = Modifier.padding(horizontal = BtSpacing.l),
                verticalArrangement = Arrangement.spacedBy(BtSpacing.l),
            ) {
                StopTitle(viewModel = viewModel)
                Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
                    StopMetricTile(Res.drawable.ic_clock, colors.primary, viewModel.visitDuration, Modifier.weight(1f))
                    StopMetricTile(Res.drawable.ic_navigate, colors.coralText, viewModel.distance, Modifier.weight(1f))
                }
                BtInsightCard(
                    eyebrow = viewModel.secretEyebrow.uppercase(),
                    tag = viewModel.secretTag,
                    quote = viewModel.secretQuote,
                )
                BtSectionHeader(title = viewModel.essentialsTitle, icon = Res.drawable.ic_info_circle)
                viewModel.essentials.forEachIndexed { index, essential ->
                    StopEssentialRow(index = index, essential = essential)
                }
                BtSectionHeader(title = viewModel.highlightsTitle, icon = Res.drawable.ic_sparkles)
                viewModel.highlights.forEach { highlight ->
                    StopHighlightRow(highlight = highlight)
                }
                StopSurroundingCard(viewModel = viewModel)
                if (viewModel.isRouteStop) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
                    ) {
                        BtButton(
                            text = viewModel.keepTitle,
                            onClick = { viewModel.keep() },
                            leadingIcon = Res.drawable.ic_check,
                            modifier = Modifier.weight(1f),
                        )
                        BtButton(
                            text = viewModel.swapTitle,
                            onClick = { viewModel.swap() },
                            style = BtButtonStyle.Secondary,
                            leadingIcon = Res.drawable.ic_swap_horizontal,
                        )
                    }
                }
                Text(
                    text = viewModel.removeTitle,
                    style = BetterTravelTheme.typography.label,
                    color = colors.coralText,
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .clickable(role = Role.Button) { viewModel.remove() }
                            .padding(vertical = BtSpacing.m),
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StopHero(
    viewModel: StopDetailsViewModel,
    isBookmarked: Boolean,
) {
    val colors = BetterTravelTheme.colors
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(HERO_HEIGHT)
                .clip(BtShapes.heroBottom)
                .background(Brush.verticalGradient(listOf(colors.primaryContainer, colors.coralLight, colors.coral))),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(BtSpacing.l),
            horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier =
                    Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(colors.glassStrong)
                        .padding(horizontal = BtSpacing.l, vertical = BtSpacing.m),
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BtIcon(Res.drawable.ic_flag, tint = colors.primary, size = 16.dp)
                Text(
                    text = viewModel.stopBadge,
                    style = BetterTravelTheme.typography.label,
                    color = colors.textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            BtIconButton(
                icon = Res.drawable.ic_bookmark,
                onClick = { viewModel.toggleBookmark() },
                style = BtIconButtonStyle.Glass,
                tint = if (isBookmarked) colors.coralText else null,
            )
        }
        BtPlaceImage(modifier = Modifier.align(Alignment.TopCenter).padding(top = HERO_IMAGE_TOP).size(HERO_IMAGE_SIZE))
        FlowRow(
            modifier = Modifier.align(Alignment.BottomStart).padding(BtSpacing.l),
            horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
            verticalArrangement = Arrangement.spacedBy(BtSpacing.s),
        ) {
            viewModel.tags.forEachIndexed { index, tag ->
                val icon = HERO_TAG_ICONS[index % HERO_TAG_ICONS.size]
                val tint = listOf(colors.goldDark, colors.primary, colors.coralText)[index % HERO_TAG_ICONS.size]
                Row(
                    modifier =
                        Modifier
                            .clip(CircleShape)
                            .background(colors.glassStrong)
                            .padding(horizontal = BtSpacing.m, vertical = BtSpacing.s),
                    horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BtIcon(icon, tint = tint, size = 14.dp)
                    Text(text = tag, style = BetterTravelTheme.typography.label, color = colors.textPrimary)
                }
            }
        }
    }
}

@Composable
private fun StopTitle(viewModel: StopDetailsViewModel) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
            Text(text = viewModel.name, style = BetterTravelTheme.typography.headline, color = colors.textPrimary)
            Text(
                text = viewModel.nativeName,
                style = BetterTravelTheme.typography.bodySmallItalic,
                color = colors.textSecondary,
            )
        }
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
            Row(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(colors.surfaceContainer)
                        .padding(horizontal = BtSpacing.m, vertical = BtSpacing.xs),
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BtIcon(Res.drawable.ic_star, tint = colors.goldDark, size = 14.dp)
                Text(text = viewModel.rating, style = BetterTravelTheme.typography.title, color = colors.textPrimary)
            }
            Text(text = viewModel.reviews, style = BetterTravelTheme.typography.bodySmall, color = colors.textTertiary)
        }
    }
}

@Composable
private fun StopMetricTile(
    icon: DrawableResource,
    tint: Color,
    metric: StopDetailsMetric,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            modifier
                .clip(BtShapes.md)
                .background(colors.surfaceContainer)
                .padding(BtSpacing.l),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BtSpacing.xs),
    ) {
        BtIcon(icon, tint = tint, size = 20.dp)
        Text(text = metric.value, style = BetterTravelTheme.typography.title, color = colors.textPrimary)
        Text(
            text = metric.caption,
            style = BetterTravelTheme.typography.bodySmall,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun StopEssentialRow(
    index: Int,
    essential: StopDetailsEssential,
) {
    val colors = BetterTravelTheme.colors
    BtInfoRow(
        icon = ESSENTIAL_ICONS[index % ESSENTIAL_ICONS.size],
        title = essential.title,
        body = essential.body,
        note = essential.note,
        trailing =
            essential.trailing?.let { trailing ->
                {
                    when (index) {
                        0 -> {
                            BtBadge(trailing, style = BtBadgeStyle.Neutral)
                        }

                        1 -> {
                            Text(text = trailing, style = BetterTravelTheme.typography.label, color = colors.primary)
                        }

                        else -> {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = trailing,
                                    style = BetterTravelTheme.typography.label,
                                    color = colors.primary,
                                )
                                BtIcon(Res.drawable.ic_arrow_right, tint = colors.primary, size = 14.dp)
                            }
                        }
                    }
                }
            },
    )
}

@Composable
private fun StopHighlightRow(highlight: StopDetailsHighlight) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(BtShapes.md)
                .background(colors.surfaceContainer)
                .padding(BtSpacing.m),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BtPlaceImage(
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(56.dp).clip(CircleShape).background(colors.surfaceContainerMax),
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BtSpacing.xxs)) {
            Text(text = highlight.title, style = BetterTravelTheme.typography.title, color = colors.textPrimary)
            Text(
                text = highlight.description,
                style = BetterTravelTheme.typography.bodySmall,
                color = colors.textSecondary,
                maxLines = 2,
            )
        }
        BtIcon(Res.drawable.ic_chevron_right, tint = colors.textTertiary, size = 18.dp)
    }
}

@Composable
private fun StopSurroundingCard(viewModel: StopDetailsViewModel) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(BtShapes.md)
                .background(colors.surfaceContainer)
                .padding(BtSpacing.m),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.m),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = viewModel.surroundingTitle,
                style = BetterTravelTheme.typography.title,
                color = colors.textPrimary,
            )
            Text(
                text = viewModel.surroundingSubtitle,
                style = BetterTravelTheme.typography.bodySmall,
                color = colors.textSecondary,
            )
        }
        BtMapPlaceholder(
            modifier = Modifier.height(MAP_PREVIEW_HEIGHT).clip(BtShapes.md),
            overlay = {
                Box(modifier = Modifier.fillMaxSize().padding(BtSpacing.m), contentAlignment = Alignment.BottomStart) {
                    Text(
                        text = viewModel.nextStop,
                        style = BetterTravelTheme.typography.label,
                        color = colors.textPrimary,
                        modifier =
                            Modifier
                                .btShadow(BtElevation.Md, BtShapes.md)
                                .clip(BtShapes.md)
                                .background(colors.glassStrong)
                                .padding(horizontal = BtSpacing.m, vertical = BtSpacing.s),
                    )
                }
            },
        )
    }
}

private val HERO_HEIGHT = 320.dp
private val HERO_IMAGE_SIZE = 150.dp
private val HERO_IMAGE_TOP = 72.dp
private val MAP_PREVIEW_HEIGHT = 140.dp
private val HERO_TAG_ICONS = listOf(Res.drawable.ic_medal_star, Res.drawable.ic_verified, Res.drawable.ic_architecture)
private val ESSENTIAL_ICONS = listOf(Res.drawable.ic_door_open_hours, Res.drawable.ic_ticket, Res.drawable.ic_pin)

@Preview
@Composable
private fun StopDetailsContentPreview() {
    BtPreview {
        StopDetailsContent(viewModel = PreviewStopDetailsViewModel(), isBookmarked = false)
    }
}

@Preview
@Composable
private fun StopDetailsScreenPreview() {
    BtPreview {
        StopDetailsScreen(PreviewStopDetailsViewModel())
    }
}
