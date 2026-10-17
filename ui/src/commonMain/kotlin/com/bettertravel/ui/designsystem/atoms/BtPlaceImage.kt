package com.bettertravel.ui.designsystem.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.attraction_placeholder
import org.jetbrains.compose.resources.painterResource

@Composable
fun BtPlaceImage(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Image(
        painter = painterResource(Res.drawable.attraction_placeholder),
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun BtPlaceImagePreview() {
    BtPreview {
        BtPlaceImage(modifier = Modifier.size(160.dp))
    }
}
