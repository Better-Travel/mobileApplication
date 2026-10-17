package com.bettertravel.ui.designsystem.organisms

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtDragHandle
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import kotlinx.coroutines.launch

@Composable
fun BtExpandableSheet(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    collapsedHeight: Dp,
    expandedHeight: Dp,
    bottomInset: Dp,
    modifier: Modifier = Modifier,
    collapsedContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val minPx = with(density) { collapsedHeight.toPx() }
    val maxPx = with(density) { expandedHeight.toPx() }
    val height = remember { Animatable(if (expanded) maxPx else minPx) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(expanded, minPx, maxPx) {
        height.animateTo(if (expanded) maxPx else minPx)
    }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .height(with(density) { height.value.toDp() })
                .btShadow(BtElevation.Lg, BtShapes.sheet)
                .clip(BtShapes.sheet)
                .background(BetterTravelTheme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .pointerInput(minPx, maxPx) {
                        var travelled = 0f
                        detectVerticalDragGestures(
                            onDragStart = { travelled = 0f },
                            onDragEnd = {
                                val target =
                                    expanded.let { current ->
                                        if (travelled ==
                                            0f
                                        ) {
                                            current
                                        } else {
                                            travelled < 0f
                                        }
                                    }
                                scope.launch { height.animateTo(if (target) maxPx else minPx) }
                                onExpandedChange(target)
                            },
                            onVerticalDrag = { change, amount ->
                                change.consume()
                                travelled += amount
                                scope.launch { height.snapTo((height.value - amount).coerceIn(minPx, maxPx)) }
                            },
                        )
                    }.clickable(role = Role.Button) { onExpandedChange(!expanded) }
                    .padding(top = BtSpacing.m, bottom = BtSpacing.s),
            contentAlignment = Alignment.Center,
        ) {
            BtDragHandle()
        }
        Crossfade(
            targetState = expanded,
            modifier = Modifier.weight(1f).padding(bottom = bottomInset),
            label = "sheet-content",
        ) { isExpanded ->
            if (isExpanded) {
                expandedContent()
            } else {
                collapsedContent()
            }
        }
    }
}

@Preview
@Composable
private fun BtExpandableSheetPreview() {
    BtPreview {
        BtExpandableSheet(
            expanded = false,
            onExpandedChange = {},
            collapsedHeight = 200.dp,
            expandedHeight = 400.dp,
            bottomInset = 0.dp,
            collapsedContent = {
                Text(
                    text = "Collapsed",
                    style = BetterTravelTheme.typography.body,
                    modifier = Modifier.padding(BtSpacing.l),
                )
            },
            expandedContent = {
                Text(
                    text = "Expanded",
                    style = BetterTravelTheme.typography.body,
                    modifier = Modifier.padding(BtSpacing.l),
                )
            },
        )
    }
}
