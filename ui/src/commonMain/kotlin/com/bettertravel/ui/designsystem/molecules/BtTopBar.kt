package com.bettertravel.ui.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtAvatar
import com.bettertravel.ui.designsystem.atoms.BtAvatarRing
import com.bettertravel.ui.designsystem.atoms.BtDot
import com.bettertravel.ui.designsystem.atoms.BtIconButton
import com.bettertravel.ui.designsystem.atoms.BtIconButtonStyle
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.betty
import com.bettertravel.ui.resources.ic_back
import com.bettertravel.ui.resources.ic_menu

@Composable
fun BtHomeTopBar(
    title: String,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(BetterTravelTheme.colors.background.copy(alpha = 0.85f))
                .padding(start = BtSpacing.xxl, end = BtSpacing.xxl, top = BtSpacing.l, bottom = BtSpacing.s),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BtIconButton(Res.drawable.ic_menu, onClick = onMenuClick, style = BtIconButtonStyle.Plain, size = 40.dp)
        Text(
            text = title,
            style = BetterTravelTheme.typography.appTitle,
            color = BetterTravelTheme.colors.textPrimary,
        )
        BtAvatar(
            image = Res.drawable.betty,
            ring = BtAvatarRing.Soft,
            size = 40.dp,
            modifier = Modifier.clip(CircleShape).clickable(role = Role.Button, onClick = onProfileClick),
        )
    }
}

@Composable
fun BtChatTopBar(
    title: String,
    status: String,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(colors.glass)
                .padding(horizontal = BtSpacing.xl, vertical = BtSpacing.m),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BtIconButton(Res.drawable.ic_menu, onClick = onMenuClick, style = BtIconButtonStyle.Plain, size = 40.dp)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = title, style = BetterTravelTheme.typography.title, color = colors.textPrimary)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                BtDot(colors.online)
                Text(
                    text = status,
                    style = BetterTravelTheme.typography.label,
                    color = colors.textTertiary,
                )
            }
        }
        BtAvatar(image = Res.drawable.betty, ring = BtAvatarRing.Soft, size = 40.dp)
    }
}

@Composable
fun BtDetailTopBar(
    eyebrow: String,
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .background(colors.background.copy(alpha = 0.85f))
                .padding(horizontal = BtSpacing.l),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BtIconButton(Res.drawable.ic_back, onClick = onBackClick, style = BtIconButtonStyle.Glass)
        Column(modifier = Modifier.weight(1f)) {
            Text(text = eyebrow, style = BetterTravelTheme.typography.eyebrow, color = colors.primary)
            Text(
                text = title,
                style = BetterTravelTheme.typography.headline,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        BtAvatar(image = Res.drawable.betty, ring = BtAvatarRing.Border, size = 32.dp)
    }
}

@Preview
@Composable
private fun BtHomeTopBarPreview() {
    BtPreview {
        BtHomeTopBar(title = "BetterTravel", onMenuClick = {}, onProfileClick = {})
    }
}

@Preview
@Composable
private fun BtChatTopBarPreview() {
    BtPreview {
        BtChatTopBar(title = "BetterTravel", status = "with Betty", onMenuClick = {})
    }
}

@Preview
@Composable
private fun BtDetailTopBarPreview() {
    BtPreview {
        BtDetailTopBar(eyebrow = "BETTERTRAVEL", title = "St. John's Archcathedral", onBackClick = {})
    }
}
