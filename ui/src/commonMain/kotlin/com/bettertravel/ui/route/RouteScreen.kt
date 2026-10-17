package com.bettertravel.ui.route

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bettertravel.presentation.route.RouteViewModel
import com.bettertravel.ui.designsystem.molecules.BtEmptyState
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.preview.PreviewRouteViewModel

@Composable
fun RouteScreen(viewModel: RouteViewModel) {
    RouteContent(title = viewModel.title, emptyMessage = viewModel.emptyMessage)
}

@Composable
fun RouteContent(
    title: String,
    emptyMessage: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(BtGradients.page),
        contentAlignment = Alignment.Center,
    ) {
        BtEmptyState(title = title, message = emptyMessage)
    }
}

@Preview
@Composable
private fun RouteScreenPreview() {
    BtPreview {
        RouteScreen(PreviewRouteViewModel())
    }
}

@Preview
@Composable
private fun RouteContentPreview() {
    BtPreview {
        RouteContent(title = "Route", emptyMessage = "Your route will appear here once Betty has planned it.")
    }
}
