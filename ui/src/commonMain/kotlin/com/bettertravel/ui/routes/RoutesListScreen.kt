package com.bettertravel.ui.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.bettertravel.domain.routes.Route
import com.bettertravel.presentation.routes.RoutesListViewModel
import com.bettertravel.ui.designsystem.atoms.BtButton
import com.bettertravel.ui.designsystem.organisms.BtMetricsStrip
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.preview.PreviewRoutesListViewModel
import com.bettertravel.ui.preview.sampleRoutes
import com.bettertravel.ui.rx.collectAsState

@Composable
fun RoutesListScreen(viewModel: RoutesListViewModel) {
    val routes by viewModel.routes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(initial = false)
    val errorMessage by viewModel.errorMessage.collectAsState()

    RoutesListContent(
        title = viewModel.title,
        emptyMessage = viewModel.emptyMessage,
        retryTitle = viewModel.retryTitle,
        routes = routes,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onReload = { viewModel.reload() },
        onSelect = { route -> viewModel.select(route) },
    )
}

@Composable
fun RoutesListContent(
    title: String,
    emptyMessage: String,
    retryTitle: String,
    routes: List<Route>,
    isLoading: Boolean,
    errorMessage: String?,
    onReload: () -> Unit,
    onSelect: (Route) -> Unit,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(BtSpacing.l),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.m),
    ) {
        Text(text = title, style = BetterTravelTheme.typography.headlineLarge, color = colors.textPrimary)
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = colors.primary)
            } else if (errorMessage != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(BtSpacing.m),
                ) {
                    Text(text = errorMessage, style = BetterTravelTheme.typography.body, color = colors.textSecondary)
                    BtButton(text = retryTitle, onClick = onReload)
                }
            } else if (routes.isEmpty()) {
                Text(
                    text = emptyMessage,
                    style = BetterTravelTheme.typography.body,
                    color = colors.textSecondary,
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
                    items(routes, key = { it.id }) { route ->
                        RouteRow(route = route, onClick = { onSelect(route) })
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteRow(
    route: Route,
    onClick: () -> Unit,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(BtShapes.xl)
                .background(colors.surface)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(BtSpacing.l),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.s),
    ) {
        Text(text = route.title, style = BetterTravelTheme.typography.headlineSemiBold, color = colors.textPrimary)
        Text(text = route.city, style = BetterTravelTheme.typography.bodySmall, color = colors.textSecondary)
        BtMetricsStrip(
            listOf(
                "${route.distanceKm} km",
                "${route.stopsCount} stops",
                "${route.durationMinutes} min",
            ),
        )
    }
}

@Preview
@Composable
private fun RoutesListScreenPreview() {
    BtPreview {
        RoutesListScreen(PreviewRoutesListViewModel())
    }
}

@Preview
@Composable
private fun RoutesListContentLoadedPreview() {
    BtPreview {
        RoutesListContent("Your routes", "No routes yet", "Try again", sampleRoutes, false, null, {}, {})
    }
}

@Preview
@Composable
private fun RoutesListContentErrorPreview() {
    BtPreview {
        RoutesListContent(
            title = "Your routes",
            emptyMessage = "No routes yet",
            retryTitle = "Try again",
            routes = emptyList(),
            isLoading = false,
            errorMessage = "Something went wrong while loading your routes",
            onReload = {},
            onSelect = {},
        )
    }
}

@Preview
@Composable
private fun RouteRowPreview() {
    BtPreview {
        RouteRow(route = sampleRoutes.first(), onClick = {})
    }
}
