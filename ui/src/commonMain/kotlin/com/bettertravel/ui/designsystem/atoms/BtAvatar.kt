package com.bettertravel.ui.designsystem.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.betty
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class BtAvatarRing {
    None,
    Border,
    Companion,
    Map,
    Soft,
}

@Composable
fun BtAvatar(
    image: DrawableResource,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    ring: BtAvatarRing = BtAvatarRing.Border,
    contentDescription: String? = null,
) {
    val colors = BetterTravelTheme.colors
    val brush: Brush? =
        when (ring) {
            BtAvatarRing.Companion -> BtGradients.companionRing
            BtAvatarRing.Map -> BtGradients.mapRing
            BtAvatarRing.Soft -> BtGradients.avatarRing
            else -> null
        }
    val base =
        modifier
            .size(size)
            .clip(CircleShape)
    val framed =
        when {
            brush != null -> {
                base.background(brush)
            }

            ring == BtAvatarRing.Border -> {
                base
                    .background(colors.surface)
                    .border(1.dp, colors.skyTint, CircleShape)
            }

            else -> {
                base.background(colors.surface)
            }
        }
    Box(modifier = framed.padding(if (ring == BtAvatarRing.None) 0.dp else 2.dp)) {
        Image(
            painter = painterResource(image),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(colors.surface),
        )
    }
}

@Composable
fun BtBetty(
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    contentDescription: String? = null,
) {
    Image(
        painter = painterResource(Res.drawable.betty),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier.size(size),
    )
}

@Preview
@Composable
private fun BtAvatarPreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
            BtAvatar(Res.drawable.betty, ring = BtAvatarRing.None)
            BtAvatar(Res.drawable.betty, ring = BtAvatarRing.Border)
            BtAvatar(Res.drawable.betty, ring = BtAvatarRing.Companion)
            BtAvatar(Res.drawable.betty, ring = BtAvatarRing.Soft, size = 48.dp)
        }
    }
}

@Preview
@Composable
private fun BtBettyPreview() {
    BtPreview {
        BtBetty(size = 160.dp)
    }
}
