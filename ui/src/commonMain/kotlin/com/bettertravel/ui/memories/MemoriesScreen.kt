package com.bettertravel.ui.memories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bettertravel.presentation.memories.MemoriesViewModel
import com.bettertravel.ui.designsystem.molecules.BtEmptyState
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.preview.PreviewMemoriesViewModel

@Composable
fun MemoriesScreen(viewModel: MemoriesViewModel) {
    MemoriesContent(title = viewModel.title, emptyMessage = viewModel.emptyMessage)
}

@Composable
fun MemoriesContent(
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
private fun MemoriesScreenPreview() {
    BtPreview {
        MemoriesScreen(PreviewMemoriesViewModel())
    }
}

@Preview
@Composable
private fun MemoriesContentPreview() {
    BtPreview {
        MemoriesContent(title = "Memories", emptyMessage = "Your saved walks will appear here.")
    }
}
