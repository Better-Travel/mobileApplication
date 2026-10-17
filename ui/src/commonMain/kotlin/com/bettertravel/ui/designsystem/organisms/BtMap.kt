package com.bettertravel.ui.designsystem.organisms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.plusAssign
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.subscribe
import com.bettertravel.domain.map.GeoPoint
import com.bettertravel.domain.map.MapTile
import com.bettertravel.ui.designsystem.atoms.BtMarkerStyle
import com.bettertravel.ui.designsystem.atoms.BtNumberMarker
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtSpacing
import org.jetbrains.compose.resources.decodeToImageBitmap
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun BtMap(
    markers: List<GeoPoint>,
    path: List<GeoPoint>,
    loadTile: (MapTile) -> Single<ByteArray>,
    modifier: Modifier = Modifier,
    fitPadding: PaddingValues = PaddingValues(),
    labels: List<String> = emptyList(),
    selectedIndex: Int? = null,
    onMarkerClick: (Int) -> Unit = {},
    onBackgroundTap: () -> Unit = {},
) {
    val colors = BetterTravelTheme.colors
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val currentLoadTile by rememberUpdatedState(loadTile)
    var size by remember { mutableStateOf(IntSize.Zero) }
    var camera by remember { mutableStateOf<MapCamera?>(null) }
    val bitmaps = remember { mutableStateMapOf<MapTile, ImageBitmap>() }
    val requested = remember { mutableSetOf<MapTile>() }
    val disposables = remember { CompositeDisposable() }
    DisposableEffect(Unit) {
        onDispose { disposables.dispose() }
    }

    val fitPoints = remember(markers, path) { markers + path }
    LaunchedEffect(fitPoints, size, fitPadding) {
        if (fitPoints.isNotEmpty() && size.width > 0 && size.height > 0) {
            val left = with(density) { fitPadding.calculateStartPadding(layoutDirection).toPx() }
            val right = with(density) { fitPadding.calculateEndPadding(layoutDirection).toPx() }
            val top = with(density) { fitPadding.calculateTopPadding().toPx() }
            val bottom = with(density) { fitPadding.calculateBottomPadding().toPx() }
            val fitted =
                fitCamera(
                    points = fitPoints,
                    widthPx = (size.width - left - right - FIT_MARGIN_PX).coerceAtLeast(1f).toDouble(),
                    heightPx = (size.height - top - bottom - FIT_MARGIN_PX).coerceAtLeast(1f).toDouble(),
                    density = density.density,
                )
            val worldPx = worldSizePx(fitted.zoom, density.density)
            val shiftX = (left - right) / 2.0
            val shiftY = (top - bottom) / 2.0
            camera = fitted.copy(worldX = fitted.worldX - shiftX / worldPx, worldY = fitted.worldY - shiftY / worldPx)
        }
    }

    val current = camera
    val visibleTiles =
        if (current != null && size.width > 0) visibleTiles(current, size, density.density) else emptyList()
    LaunchedEffect(visibleTiles) {
        visibleTiles.forEach { tile ->
            if (requested.add(tile)) {
                disposables +=
                    currentLoadTile(tile)
                        .observeOn(mainScheduler)
                        .subscribe(
                            onSuccess = { bytes ->
                                runCatching { bytes.decodeToImageBitmap() }
                                    .onSuccess { bitmap -> bitmaps[tile] = bitmap }
                                    .onFailure { requested.remove(tile) }
                            },
                            onError = { requested.remove(tile) },
                        )
            }
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .clipToBounds()
                .background(colors.surfaceContainer)
                .onSizeChanged { newSize -> size = newSize }
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, _ ->
                        camera = camera?.transformed(centroid, pan, zoom, size, density.density)
                    }
                }.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onBackgroundTap() },
                        onDoubleTap = { position ->
                            camera = camera?.transformed(position, Offset.Zero, 2f, size, density.density)
                        },
                    )
                },
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cam = camera ?: return@Canvas
            drawTiles(cam, bitmaps)
            val screenPath = path.map { point -> point.toScreen(cam, size, density.density) }
            if (screenPath.size > 1) {
                val line =
                    Path().apply {
                        moveTo(screenPath.first().x, screenPath.first().y)
                        screenPath.drop(1).forEach { point -> lineTo(point.x, point.y) }
                    }
                val gradient =
                    Brush.linearGradient(
                        listOf(colors.brand, colors.coral),
                        start = screenPath.first(),
                        end = screenPath.last(),
                    )
                val round = { width: Float -> Stroke(width = width, cap = StrokeCap.Round, join = StrokeJoin.Round) }
                drawPath(line, gradient, alpha = HALO_ALPHA, style = round(HALO_WIDTH.toPx()))
                drawPath(line, gradient, style = round(LINE_WIDTH.toPx()))
                drawPath(
                    line,
                    Color.White,
                    alpha = DASH_ALPHA,
                    style =
                        Stroke(
                            width = DASH_WIDTH.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 12f)),
                        ),
                )
            }
        }
        val markerRadius = with(density) { MARKER_SIZE.toPx() / 2 }
        markers.forEachIndexed { index, point ->
            val isLast = index == markers.lastIndex && markers.size > 1
            val isSelected = index == selectedIndex
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .offset {
                            val position = camera?.let { cam -> point.toScreen(cam, size, density.density) }
                            if (position == null) {
                                IntOffset(-OFF_SCREEN_PX, -OFF_SCREEN_PX)
                            } else {
                                IntOffset(
                                    (position.x - markerRadius).roundToInt(),
                                    (position.y - markerRadius).roundToInt(),
                                )
                            }
                        }.clickable(role = Role.Button) { onMarkerClick(index) },
            ) {
                BtNumberMarker(
                    number = index + 1,
                    style =
                        when {
                            isSelected -> BtMarkerStyle.Primary
                            isLast -> BtMarkerStyle.Coral
                            else -> BtMarkerStyle.Light
                        },
                    size = MARKER_SIZE,
                )
                labels.getOrNull(index)?.let { label ->
                    Text(
                        text = label,
                        style = BetterTravelTheme.typography.labelMedium,
                        color = colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier =
                            Modifier
                                .padding(start = BtSpacing.xs)
                                .widthIn(max = LABEL_MAX_WIDTH)
                                .clip(CircleShape)
                                .background(colors.glassStrong)
                                .padding(horizontal = BtSpacing.m, vertical = BtSpacing.xs),
                    )
                }
            }
        }
    }
}

private val MARKER_SIZE = 36.dp
private val LABEL_MAX_WIDTH = 160.dp
private val HALO_WIDTH = 14.dp
private val LINE_WIDTH = 6.dp
private val DASH_WIDTH = 1.5.dp
private const val HALO_ALPHA = 0.25f
private const val DASH_ALPHA = 0.85f
private const val FIT_MARGIN_PX = 96f
private const val OFF_SCREEN_PX = 10_000

private fun GeoPoint.toScreen(
    camera: MapCamera,
    size: IntSize,
    density: Float,
): Offset {
    val worldPx = worldSizePx(camera.zoom, density)
    return Offset(
        x = (size.width / 2.0 + (worldX(longitude) - camera.worldX) * worldPx).toFloat(),
        y = (size.height / 2.0 + (worldY(latitude) - camera.worldY) * worldPx).toFloat(),
    )
}

private fun MapCamera.transformed(
    centroid: Offset,
    pan: Offset,
    zoomFactor: Float,
    size: IntSize,
    density: Float,
): MapCamera {
    val newZoom = (zoom + log2(zoomFactor.toDouble())).coerceIn(MAP_MIN_ZOOM, MAP_MAX_ZOOM)
    val oldWorldPx = worldSizePx(zoom, density)
    val newWorldPx = worldSizePx(newZoom, density)
    val underX = worldX + (centroid.x - size.width / 2.0) / oldWorldPx
    val underY = worldY + (centroid.y - size.height / 2.0) / oldWorldPx
    return MapCamera(
        worldX = underX - (centroid.x + pan.x - size.width / 2.0) / newWorldPx,
        worldY = (underY - (centroid.y + pan.y - size.height / 2.0) / newWorldPx).coerceIn(0.0, 1.0),
        zoom = newZoom,
    )
}

private fun tileZoom(camera: MapCamera): Int {
    return floor(camera.zoom).toInt()
}

private fun visibleTiles(
    camera: MapCamera,
    size: IntSize,
    density: Float,
): List<MapTile> {
    val zoom = tileZoom(camera)
    val count = 2.0.pow(zoom).toInt()
    val tilePx = worldSizePx(camera.zoom, density) / count
    val centerX = camera.worldX * count
    val centerY = camera.worldY * count
    val halfX = size.width / 2.0 / tilePx
    val halfY = size.height / 2.0 / tilePx
    val tiles = mutableListOf<MapTile>()
    for (y in floor(centerY - halfY).toInt()..floor(centerY + halfY).toInt()) {
        if (y in 0 until count) {
            for (x in floor(centerX - halfX).toInt()..floor(centerX + halfX).toInt()) {
                tiles.add(MapTile(zoom = zoom, x = ((x % count) + count) % count, y = y))
            }
        }
    }
    return tiles
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTiles(
    camera: MapCamera,
    bitmaps: SnapshotStateMap<MapTile, ImageBitmap>,
) {
    val zoom = tileZoom(camera)
    val count = 2.0.pow(zoom).toInt()
    val tilePx = worldSizePx(camera.zoom, density) / count
    val centerX = camera.worldX * count
    val centerY = camera.worldY * count
    val halfX = size.width / 2.0 / tilePx
    val halfY = size.height / 2.0 / tilePx
    for (y in floor(centerY - halfY).toInt()..floor(centerY + halfY).toInt()) {
        if (y !in 0 until count) {
            continue
        }
        for (x in floor(centerX - halfX).toInt()..floor(centerX + halfX).toInt()) {
            val wrappedX = ((x % count) + count) % count
            val bitmap = bitmaps[MapTile(zoom, wrappedX, y)] ?: continue
            val left = (size.width / 2.0 + (x - centerX) * tilePx).roundToInt()
            val top = (size.height / 2.0 + (y - centerY) * tilePx).roundToInt()
            val right = (size.width / 2.0 + (x + 1 - centerX) * tilePx).roundToInt()
            val bottom = (size.height / 2.0 + (y + 1 - centerY) * tilePx).roundToInt()
            drawImage(
                image = bitmap,
                dstOffset = IntOffset(left, top),
                dstSize = IntSize(right - left, bottom - top),
            )
        }
    }
}

@Preview
@Composable
private fun BtMapPreview() {
    val stops =
        listOf(GeoPoint(52.24975, 21.01218), GeoPoint(52.24902, 21.01368), GeoPoint(52.25315, 21.01044))
    BtPreview {
        BtMap(
            markers = stops,
            path = stops,
            loadTile = { singleOf(ByteArray(0)) },
            modifier = Modifier.height(320.dp),
        )
    }
}
