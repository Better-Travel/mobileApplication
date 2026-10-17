package com.bettertravel.ui.route

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.routes.RouteStop
import com.bettertravel.presentation.route.RouteSelectedStop
import com.bettertravel.presentation.route.RouteStopDetails
import com.bettertravel.presentation.route.RouteSummary
import com.bettertravel.presentation.route.RouteViewModel
import com.bettertravel.ui.designsystem.atoms.BtAvatar
import com.bettertravel.ui.designsystem.atoms.BtAvatarRing
import com.bettertravel.ui.designsystem.atoms.BtBadge
import com.bettertravel.ui.designsystem.atoms.BtBadgeStyle
import com.bettertravel.ui.designsystem.atoms.BtButton
import com.bettertravel.ui.designsystem.atoms.BtButtonStyle
import com.bettertravel.ui.designsystem.atoms.BtDot
import com.bettertravel.ui.designsystem.atoms.BtHorizontalDivider
import com.bettertravel.ui.designsystem.atoms.BtIcon
import com.bettertravel.ui.designsystem.atoms.BtIconButton
import com.bettertravel.ui.designsystem.atoms.BtMarkerStyle
import com.bettertravel.ui.designsystem.molecules.BtCompanionPrompt
import com.bettertravel.ui.designsystem.molecules.BtEmptyState
import com.bettertravel.ui.designsystem.molecules.BtWaypointCard
import com.bettertravel.ui.designsystem.organisms.BtExpandableSheet
import com.bettertravel.ui.designsystem.organisms.BtMap
import com.bettertravel.ui.designsystem.organisms.BtRouteLoading
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.preview.PreviewRouteViewModel
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.attraction_placeholder
import com.bettertravel.ui.resources.betty
import com.bettertravel.ui.resources.ic_clock
import com.bettertravel.ui.resources.ic_expand_chevrons
import com.bettertravel.ui.resources.ic_flag
import com.bettertravel.ui.resources.ic_layers
import com.bettertravel.ui.resources.ic_locate_me
import com.bettertravel.ui.resources.ic_navigation
import com.bettertravel.ui.resources.ic_regenerate
import com.bettertravel.ui.resources.ic_walk
import com.bettertravel.ui.rx.collectAsState
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun RouteScreen(viewModel: RouteViewModel) {
    val routeTitle by viewModel.routeTitle.collectAsState()
    val summary by viewModel.summary.collectAsState()
    val stops by viewModel.stops.collectAsState()
    val stopDetails by viewModel.stopDetails.collectAsState()
    val path by viewModel.path.collectAsState()
    val selectedStop by viewModel.selectedStop.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(initial = false)
    val hasError by viewModel.hasError.collectAsState()
    RouteContent(
        viewModel = viewModel,
        routeTitle = routeTitle,
        summary = summary,
        stops = stops,
        stopDetails = stopDetails,
        path = path,
        selectedStop = selectedStop,
        isLoading = isLoading,
        hasError = hasError,
    )
}

@Composable
fun RouteContent(
    viewModel: RouteViewModel,
    routeTitle: String?,
    summary: RouteSummary?,
    stops: List<RouteStop>,
    stopDetails: List<RouteStopDetails>,
    path: List<GeoPoint>,
    selectedStop: RouteSelectedStop?,
    isLoading: Boolean,
    hasError: Boolean,
    modifier: Modifier = Modifier,
) {
    if (routeTitle == null || summary == null) {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(BtGradients.page),
            contentAlignment = Alignment.Center,
        ) {
            BtEmptyState(title = viewModel.title, message = viewModel.emptyMessage)
        }
        return
    }
    var expanded by remember { mutableStateOf(false) }
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val containerHeight = maxHeight
        BtMap(
            markers = stops.map { stop -> stop.location },
            path = path,
            loadTile = { tile -> viewModel.loadTile(tile) },
            fitPadding = PaddingValues(top = MAP_TOP_PADDING, bottom = COLLAPSED_SHEET_HEIGHT + MAP_CONTROLS_HEIGHT),
            labels = stopDetails.map { details -> details.name },
            selectedIndex = selectedStop?.let { selected -> selected.order - 1 },
            onMarkerClick = { index -> viewModel.selectStop(index + 1) },
            onBackgroundTap = { viewModel.clearSelection() },
        )
        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(horizontal = BtSpacing.l, vertical = BtSpacing.m),
            verticalArrangement = Arrangement.spacedBy(BtSpacing.m),
        ) {
            RouteTopBar(viewModel = viewModel, distance = summary.distance, isLoading = isLoading)
            if (selectedStop != null) {
                RouteSelectedCard(viewModel = viewModel, selected = selectedStop)
            }
        }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            if (hasError) {
                RouteErrorBanner(
                    viewModel = viewModel,
                    modifier = Modifier.padding(horizontal = BtSpacing.l, vertical = BtSpacing.s),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = BtSpacing.l, vertical = BtSpacing.m),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
                    // TODO: center the map on the user and switch the map layers
                    BtIconButton(icon = Res.drawable.ic_locate_me, onClick = {})
                    BtIconButton(icon = Res.drawable.ic_layers, onClick = {})
                }
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
                    if (!expanded) {
                        BtAvatar(
                            image = Res.drawable.betty,
                            size = FLOATING_BETTY_SIZE,
                            ring = BtAvatarRing.Companion,
                            modifier =
                                Modifier
                                    .btShadow(BtElevation.Lg, CircleShape)
                                    .clip(CircleShape)
                                    .clickable(role = Role.Button) { viewModel.askBetty() },
                        )
                    }
                    Text(
                        text = viewModel.attribution,
                        style = BetterTravelTheme.typography.labelSmall,
                        color = BetterTravelTheme.colors.textSecondary,
                        modifier =
                            Modifier
                                .clip(CircleShape)
                                .background(BetterTravelTheme.colors.glass)
                                .padding(horizontal = BtSpacing.s, vertical = BtSpacing.xxs),
                    )
                }
            }
            BtExpandableSheet(
                expanded = expanded,
                onExpandedChange = { value -> expanded = value },
                collapsedHeight = COLLAPSED_SHEET_HEIGHT,
                expandedHeight = containerHeight * EXPANDED_SHEET_FRACTION,
                bottomInset = BtSpacing.tabBarInset,
                collapsedContent = {
                    RouteCollapsedContent(viewModel = viewModel, title = routeTitle, summary = summary)
                },
                expandedContent = {
                    RouteExpandedContent(viewModel = viewModel, summary = summary, stopDetails = stopDetails)
                },
            )
        }
        if (isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(BetterTravelTheme.colors.background.copy(alpha = LOADING_SCRIM_ALPHA))
                        .pointerInput(Unit) { detectTapGestures { } },
                contentAlignment = Alignment.Center,
            ) {
                BtRouteLoading(message = viewModel.loadingMessage)
            }
        }
    }
}

@Composable
private fun RouteTopBar(
    viewModel: RouteViewModel,
    distance: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier =
                Modifier
                    .btShadow(BtElevation.Md, CircleShape)
                    .clip(CircleShape)
                    .background(colors.glassStrong)
                    .padding(horizontal = BtSpacing.l, vertical = BtSpacing.m),
            horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 2.dp, color = colors.brand)
            } else {
                BtDot(colors.brand, size = 12.dp)
            }
            Text(text = viewModel.pillTitle, style = BetterTravelTheme.typography.label, color = colors.textPrimary)
            BtDot(colors.textTertiary, size = 3.dp)
            Text(text = distance, style = BetterTravelTheme.typography.label, color = colors.primary)
        }
        Row(
            modifier =
                Modifier
                    .btShadow(BtElevation.Md, CircleShape)
                    .clip(CircleShape)
                    .background(colors.glassStrong)
                    .clickable(role = Role.Button) { viewModel.regenerate() }
                    .padding(horizontal = BtSpacing.l, vertical = BtSpacing.m),
            horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BtIcon(Res.drawable.ic_regenerate, tint = colors.primary, size = 18.dp)
            Text(
                text = viewModel.regenerateTitle,
                style = BetterTravelTheme.typography.label,
                color = colors.textPrimary,
            )
        }
    }
}

@Composable
private fun RouteSelectedCard(
    viewModel: RouteViewModel,
    selected: RouteSelectedStop,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Md, BtShapes.lg)
                .clip(BtShapes.lg)
                .background(colors.glassStrong)
                .padding(BtSpacing.m),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.m),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(colors.skyTint),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = selected.order.toString(),
                    style = BetterTravelTheme.typography.label,
                    color = colors.primary,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = selected.name,
                    style = BetterTravelTheme.typography.title,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = selected.subtitle,
                    style = BetterTravelTheme.typography.bodySmall,
                    color = colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            BtBadge(viewModel.selectedTag.uppercase(), style = BtBadgeStyle.Sky)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtButton(
                text = viewModel.showDetailsTitle,
                onClick = { viewModel.showDetails(selected.order) },
                style = BtButtonStyle.Outlined,
                modifier = Modifier.weight(1f),
            )
            BtButton(
                text = viewModel.changeTitle,
                onClick = { viewModel.askBetty() },
                style = BtButtonStyle.Outlined,
                modifier = Modifier.weight(1f),
            )
            BtButton(
                text = viewModel.deleteTitle,
                onClick = { viewModel.deleteSelectedStop() },
                style = BtButtonStyle.Outlined,
                enabled = selected.canDelete,
                modifier = Modifier.weight(1f).alpha(if (selected.canDelete) 1f else DISABLED_ALPHA),
            )
        }
    }
}

@Composable
private fun RouteErrorBanner(
    viewModel: RouteViewModel,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .clip(CircleShape)
                .background(colors.glassStrong)
                .padding(start = BtSpacing.l, end = BtSpacing.s, top = BtSpacing.xs, bottom = BtSpacing.xs),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = viewModel.errorMessage, style = BetterTravelTheme.typography.label, color = colors.coralText)
        BtButton(text = viewModel.retryTitle, onClick = { viewModel.regenerate() })
    }
}

@Composable
private fun RouteCollapsedContent(
    viewModel: RouteViewModel,
    title: String,
    summary: RouteSummary,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = BtSpacing.xl),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.m),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title,
                        style = BetterTravelTheme.typography.headlineSemiBold,
                        color = colors.textPrimary,
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    BtDot(colors.goldDark, size = 8.dp)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = summary.distance, style = BetterTravelTheme.typography.label, color = colors.primary)
                    BtDot(colors.textTertiary, size = 3.dp)
                    Text(
                        text = "${summary.duration} ${viewModel.walkSuffix}",
                        style = BetterTravelTheme.typography.labelMedium,
                        color = colors.textSecondary,
                    )
                    BtDot(colors.textTertiary, size = 3.dp)
                    Text(
                        text = "${summary.stopsCount} ${viewModel.stopsSuffix}",
                        style = BetterTravelTheme.typography.labelMedium,
                        color = colors.textSecondary,
                    )
                }
            }
            BtButton(
                text = viewModel.startTitle.uppercase(),
                onClick = { viewModel.start() },
                leadingIcon = Res.drawable.ic_navigation,
            )
        }
        BtHorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BtIcon(Res.drawable.ic_expand_chevrons, tint = colors.primary, size = 16.dp)
                Text(
                    text = viewModel.sheetHint,
                    style = BetterTravelTheme.typography.bodySmall,
                    color = colors.textSecondary,
                    maxLines = 1,
                )
            }
            BtBadge(viewModel.pillTitle.uppercase(), style = BtBadgeStyle.Neutral)
        }
    }
}

@Composable
private fun RouteExpandedContent(
    viewModel: RouteViewModel,
    summary: RouteSummary,
    stopDetails: List<RouteStopDetails>,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BtSpacing.l),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.l),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(BtShapes.md)
                    .background(colors.surfaceContainer)
                    .padding(vertical = BtSpacing.m, horizontal = BtSpacing.s),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            RouteMetric(Res.drawable.ic_clock, colors.primary, summary.duration, viewModel.totalTimeCaption)
            RouteMetric(Res.drawable.ic_walk, colors.coralText, summary.distance, viewModel.walkingCaption)
            RouteMetric(
                Res.drawable.ic_flag,
                colors.goldDark,
                "${summary.stopsCount} ${viewModel.stopsCaption}",
                viewModel.curatedCaption,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = viewModel.sequenceTitle,
                    style = BetterTravelTheme.typography.headline,
                    color = colors.textPrimary,
                )
                BtBadge(viewModel.walkableTitle, style = BtBadgeStyle.PrimaryContainer)
            }
            Text(text = viewModel.editOrderTitle, style = BetterTravelTheme.typography.label, color = colors.primary)
        }
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
            stopDetails.forEachIndexed { index, details ->
                val isLast = index == stopDetails.lastIndex && stopDetails.size > 1
                val isWarm = index >= 2 || isLast
                BtWaypointCard(
                    order = details.order,
                    markerStyle =
                        when {
                            isLast -> BtMarkerStyle.Deep
                            isWarm -> BtMarkerStyle.Coral
                            else -> BtMarkerStyle.Primary
                        },
                    title = details.name,
                    description = details.description,
                    duration = details.duration,
                    rating = details.rating,
                    reviews = details.reviews,
                    departure = details.note,
                    thumbnail = Res.drawable.attraction_placeholder,
                    warm = isWarm,
                    onClick = { viewModel.showDetails(details.order) },
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BtButton(
                text = viewModel.startNavigationTitle,
                onClick = { viewModel.start() },
                style = BtButtonStyle.Brand,
                leadingIcon = Res.drawable.ic_navigation,
            )
            BtCompanionPrompt(
                title = viewModel.askTitle,
                subtitle = viewModel.askActionTitle,
                onClick = { viewModel.askBetty() },
            )
        }
        Box(modifier = Modifier.size(BtSpacing.s))
    }
}

@Composable
private fun RouteMetric(
    icon: DrawableResource,
    tint: androidx.compose.ui.graphics.Color,
    value: String,
    caption: String,
) {
    val colors = BetterTravelTheme.colors
    Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(tint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            BtIcon(icon, tint = tint, size = 16.dp)
        }
        Column {
            Text(text = value, style = BetterTravelTheme.typography.title, color = colors.textPrimary, maxLines = 1)
            Text(
                text = caption,
                style = BetterTravelTheme.typography.labelSmall,
                color = colors.textSecondary,
                maxLines = 1,
            )
        }
    }
}

private val MAP_TOP_PADDING: Dp = 64.dp
private val FLOATING_BETTY_SIZE: Dp = 56.dp
private val MAP_CONTROLS_HEIGHT: Dp = 84.dp
private val COLLAPSED_SHEET_HEIGHT: Dp = 244.dp
private const val EXPANDED_SHEET_FRACTION = 0.68f
private const val DISABLED_ALPHA = 0.4f
private const val LOADING_SCRIM_ALPHA = 1f

@Preview
@Composable
private fun RouteScreenPreview() {
    BtPreview {
        RouteScreen(PreviewRouteViewModel(withRoute = true))
    }
}

@Preview
@Composable
private fun RouteScreenEmptyPreview() {
    BtPreview {
        RouteScreen(PreviewRouteViewModel(withRoute = false))
    }
}
