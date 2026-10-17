package com.bettertravel.ui.drawer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.presentation.drawer.DrawerProfile
import com.bettertravel.presentation.drawer.DrawerRoute
import com.bettertravel.presentation.drawer.DrawerRouteSketch
import com.bettertravel.presentation.drawer.DrawerStat
import com.bettertravel.presentation.drawer.DrawerViewModel
import com.bettertravel.ui.designsystem.atoms.BtAvatar
import com.bettertravel.ui.designsystem.atoms.BtAvatarRing
import com.bettertravel.ui.designsystem.atoms.BtHorizontalDivider
import com.bettertravel.ui.designsystem.atoms.BtIcon
import com.bettertravel.ui.designsystem.molecules.BtCityCard
import com.bettertravel.ui.designsystem.molecules.BtSettingRow
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.preview.PreviewDrawerViewModel
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.betty
import com.bettertravel.ui.resources.ic_audio_wave
import com.bettertravel.ui.resources.ic_check
import com.bettertravel.ui.resources.ic_chevron_down
import com.bettertravel.ui.resources.ic_chevron_right
import com.bettertravel.ui.resources.ic_compass
import com.bettertravel.ui.resources.ic_globe
import com.bettertravel.ui.resources.ic_help_box
import com.bettertravel.ui.resources.ic_offline_maps
import com.bettertravel.ui.rx.collectAsState

@Composable
fun DrawerScreen(viewModel: DrawerViewModel) {
    val selectedCityId by viewModel.selectedCityId.collectAsState()
    val isOfflineMapsEnabled by viewModel.isOfflineMapsEnabled.collectAsState()
    DrawerContent(
        viewModel = viewModel,
        selectedCityId = selectedCityId,
        isOfflineMapsEnabled = isOfflineMapsEnabled,
    )
}

@Composable
fun DrawerContent(
    viewModel: DrawerViewModel,
    selectedCityId: String,
    isOfflineMapsEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = BtSpacing.xl, vertical = BtSpacing.xxl),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.xl),
    ) {
        DrawerHeader(profile = viewModel.profile)
        DrawerStats(stats = viewModel.stats)
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
            DrawerSectionHeader(title = viewModel.exploredCitiesTitle, actionText = viewModel.mapViewTitle)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
                items(viewModel.cities, key = { city -> city.id }) { city ->
                    BtCityCard(
                        name = city.name,
                        routes = city.routes,
                        sights = city.sights,
                        selected = city.id == selectedCityId,
                        onClick = { viewModel.selectCity(city) },
                    )
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
            DrawerSectionHeader(title = viewModel.visitedRoutesTitle, actionText = viewModel.seeAllTitle)
            viewModel.routes.forEach { route ->
                DrawerRouteRow(route = route)
            }
        }
        BtHorizontalDivider()
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.s)) {
            BtSettingRow(
                icon = Res.drawable.ic_audio_wave,
                title = viewModel.voiceGuideTitle,
                trailing = { DrawerValuePill(text = viewModel.voiceGuideValue) },
            )
            BtSettingRow(
                icon = Res.drawable.ic_offline_maps,
                iconTint = colors.coralText,
                title = viewModel.offlineMapsTitle,
                subtitle = viewModel.offlineMapsSubtitle,
                checked = isOfflineMapsEnabled,
                onCheckedChange = { enabled -> viewModel.setOfflineMapsEnabled(enabled) },
            )
            BtSettingRow(
                icon = Res.drawable.ic_globe,
                title = viewModel.languageUnitsTitle,
                value = viewModel.languageUnitsValue,
            )
            DrawerSupportRow(title = viewModel.supportTitle, version = viewModel.appVersion)
        }
    }
}

@Composable
private fun DrawerHeader(profile: DrawerProfile) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BtAvatar(image = Res.drawable.betty, size = 44.dp, ring = BtAvatarRing.None)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
            Text(text = profile.name, style = BetterTravelTheme.typography.headline, color = colors.textPrimary)
            Row(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(colors.coralLight)
                        .padding(horizontal = BtSpacing.s, vertical = BtSpacing.xs),
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BtIcon(Res.drawable.ic_compass, tint = colors.coralText, size = 12.dp)
                Text(text = profile.level, style = BetterTravelTheme.typography.labelMedium, color = colors.coralText)
            }
        }
        BtAvatar(image = Res.drawable.betty, size = 40.dp, ring = BtAvatarRing.Soft)
    }
}

@Composable
private fun DrawerStats(stats: List<DrawerStat>) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(BtShapes.sm)
                .background(colors.surfaceContainer)
                .padding(vertical = BtSpacing.l),
    ) {
        stats.forEach { stat ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(BtSpacing.xxs),
            ) {
                Text(text = stat.value, style = BetterTravelTheme.typography.headline, color = colors.textPrimary)
                Text(
                    text = stat.caption,
                    style = BetterTravelTheme.typography.labelMedium,
                    color = colors.textSecondary,
                )
            }
        }
    }
}

@Composable
private fun DrawerSectionHeader(
    title: String,
    actionText: String,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title.uppercase(), style = BetterTravelTheme.typography.labelCaps, color = colors.textSecondary)
        Text(text = actionText, style = BetterTravelTheme.typography.label, color = colors.primary)
    }
}

@Composable
private fun DrawerRouteRow(route: DrawerRoute) {
    val colors = BetterTravelTheme.colors
    val accent = if (route.isCompleted) colors.primary else colors.coralText
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(BtShapes.sm)
                .background(colors.surfaceContainer)
                .padding(BtSpacing.m),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DrawerRouteSketchImage(sketch = route.sketch, modifier = Modifier.size(56.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BtSpacing.xxs)) {
            Text(text = route.title, style = BetterTravelTheme.typography.title, color = colors.textPrimary)
            Text(text = route.subtitle, style = BetterTravelTheme.typography.body, color = colors.textSecondary)
            Row(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = route.distance, style = BetterTravelTheme.typography.label, color = accent)
                Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(colors.textTertiary))
                Text(text = route.sights, style = BetterTravelTheme.typography.label, color = colors.textSecondary)
            }
        }
        if (route.isCompleted) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(colors.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                BtIcon(Res.drawable.ic_check, tint = colors.primary, size = 18.dp)
            }
        } else {
            BtIcon(Res.drawable.ic_chevron_right, tint = colors.textTertiary, size = 18.dp)
        }
    }
}

@Composable
private fun DrawerRouteSketchImage(
    sketch: DrawerRouteSketch,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Canvas(
        modifier =
            modifier
                .clip(BtShapes.xs)
                .background(colors.surfaceContainerHighest),
    ) {
        val width = size.width
        val height = size.height
        val strokeWidth = 2.dp.toPx()
        val path = Path()
        when (sketch) {
            DrawerRouteSketch.Trail -> {
                path.moveTo(width * 0.15f, height * 0.78f)
                path.cubicTo(width * 0.3f, height * 0.7f, width * 0.45f, height * 0.4f, width * 0.85f, height * 0.25f)
                drawRoute(path, colors.primary, Stroke(strokeWidth, cap = StrokeCap.Round, pathEffect = DASHED))
                drawEndpoints(
                    Offset(width * 0.15f, height * 0.78f),
                    Offset(width * 0.85f, height * 0.25f),
                    colors.coralText,
                    colors.primary,
                )
            }

            DrawerRouteSketch.River -> {
                path.moveTo(width * 0.25f, height * 0.2f)
                path.cubicTo(width * 0.2f, height * 0.55f, width * 0.6f, height * 0.5f, width * 0.75f, height * 0.8f)
                drawRoute(path, colors.coral, Stroke(strokeWidth, cap = StrokeCap.Round))
                drawEndpoints(
                    Offset(width * 0.25f, height * 0.2f),
                    Offset(width * 0.75f, height * 0.8f),
                    colors.primary,
                    colors.coralText,
                )
            }

            DrawerRouteSketch.Loop -> {
                path.moveTo(width * 0.15f, height * 0.65f)
                path.cubicTo(width * 0.3f, height * 0.25f, width * 0.5f, height * 0.25f, width * 0.55f, height * 0.55f)
                path.cubicTo(width * 0.6f, height * 0.85f, width * 0.8f, height * 0.85f, width * 0.88f, height * 0.6f)
                drawRoute(path, colors.brand, Stroke(strokeWidth, cap = StrokeCap.Round))
                drawEndpoints(
                    Offset(width * 0.15f, height * 0.65f),
                    Offset(width * 0.88f, height * 0.6f),
                    colors.primary,
                    colors.primary,
                )
            }
        }
    }
}

private val DASHED: PathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 8f))

private fun DrawScope.drawRoute(
    path: Path,
    color: Color,
    stroke: Stroke,
) {
    drawPath(path = path, color = color, style = stroke)
}

private fun DrawScope.drawEndpoints(
    start: Offset,
    end: Offset,
    startColor: Color,
    endColor: Color,
) {
    drawCircle(color = startColor, radius = 3.5.dp.toPx(), center = start)
    drawCircle(color = endColor, radius = 3.5.dp.toPx(), center = end)
}

@Composable
private fun DrawerValuePill(text: String) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            Modifier
                .clip(CircleShape)
                .background(colors.surfaceContainer)
                .padding(horizontal = BtSpacing.m, vertical = BtSpacing.s),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = text, style = BetterTravelTheme.typography.label, color = colors.textPrimary)
        BtIcon(Res.drawable.ic_chevron_down, tint = colors.textPrimary, size = 14.dp)
    }
}

@Composable
private fun DrawerSupportRow(
    title: String,
    version: String,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(BtShapes.xs)
                .background(colors.surfaceContainer)
                .padding(horizontal = BtSpacing.l, vertical = BtSpacing.m),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BtIcon(Res.drawable.ic_help_box, tint = colors.textTertiary, size = 18.dp)
        Text(
            text = title,
            style = BetterTravelTheme.typography.label,
            color = colors.textSecondary,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = version,
            style = BetterTravelTheme.typography.label,
            color = colors.primary,
            textAlign = TextAlign.End,
        )
    }
}

@Preview
@Composable
private fun DrawerContentPreview() {
    BtPreview {
        DrawerContent(
            viewModel = PreviewDrawerViewModel(),
            selectedCityId = "warsaw",
            isOfflineMapsEnabled = true,
        )
    }
}

@Preview
@Composable
private fun DrawerScreenPreview() {
    BtPreview {
        DrawerScreen(PreviewDrawerViewModel())
    }
}
