package com.bettertravel.ui.designsystem.organisms

import com.bettertravel.domain.map.GeoPoint
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.tan

internal const val MAP_TILE_DP = 256.0
internal const val MAP_MIN_ZOOM = 3.0
internal const val MAP_MAX_ZOOM = 18.0

internal data class MapCamera(
    val worldX: Double,
    val worldY: Double,
    val zoom: Double,
)

internal fun worldX(longitude: Double): Double {
    return (longitude + 180.0) / 360.0
}

internal fun worldY(latitude: Double): Double {
    val radians = latitude * PI / 180.0
    return (1.0 - ln(tan(radians) + 1.0 / cos(radians)) / PI) / 2.0
}

internal fun worldSizePx(
    zoom: Double,
    density: Float,
): Double {
    return MAP_TILE_DP * density * 2.0.pow(zoom)
}

internal fun fitCamera(
    points: List<GeoPoint>,
    widthPx: Double,
    heightPx: Double,
    density: Float,
): MapCamera {
    val xs = points.map { point -> worldX(point.longitude) }
    val ys = points.map { point -> worldY(point.latitude) }
    val minX = xs.min()
    val maxX = xs.max()
    val minY = ys.min()
    val maxY = ys.max()
    val spanX = (maxX - minX).coerceAtLeast(MIN_SPAN)
    val spanY = (maxY - minY).coerceAtLeast(MIN_SPAN)
    val zoomX = log2(widthPx / (spanX * MAP_TILE_DP * density))
    val zoomY = log2(heightPx / (spanY * MAP_TILE_DP * density))
    return MapCamera(
        worldX = (minX + maxX) / 2.0,
        worldY = (minY + maxY) / 2.0,
        zoom = minOf(zoomX, zoomY).coerceIn(MAP_MIN_ZOOM, MAP_MAX_ZOOM),
    )
}

internal fun log2(value: Double): Double {
    return ln(value) / ln(2.0)
}

private const val MIN_SPAN = 1e-5
