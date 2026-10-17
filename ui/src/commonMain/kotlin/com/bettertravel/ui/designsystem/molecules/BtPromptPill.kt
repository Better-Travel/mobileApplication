package com.bettertravel.ui.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.bettertravel.ui.designsystem.atoms.BtIconButton
import com.bettertravel.ui.designsystem.atoms.BtIconButtonStyle
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_mic

@Composable
fun BtPromptPill(
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Md, CircleShape)
                .clip(CircleShape)
                .background(colors.glassStrong)
                .border(1.dp, colors.glassBorder, CircleShape)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(start = 21.dp, top = 7.dp, end = 7.dp, bottom = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = placeholder,
            style = BetterTravelTheme.typography.body.copy(fontSize = BetterTravelTheme.typography.body.fontSize),
            color = colors.textTertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        BtIconButton(
            icon = Res.drawable.ic_mic,
            onClick = onClick,
            style = BtIconButtonStyle.Brand,
            size = 44.dp,
        )
    }
}

@Preview
@Composable
private fun BtPromptPillPreview() {
    BtPreview {
        BtPromptPill(placeholder = "Tell BetterTravel where & how long...", onClick = {})
    }
}
