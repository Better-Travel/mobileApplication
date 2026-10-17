package com.bettertravel.ui.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
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
import com.bettertravel.ui.resources.ic_add
import com.bettertravel.ui.resources.ic_mic
import com.bettertravel.ui.resources.ic_send

@Composable
fun BtInputPill(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onSend: () -> Unit,
    onVoice: () -> Unit,
    modifier: Modifier = Modifier,
    onAdd: (() -> Unit)? = null,
    canSend: Boolean = true,
    voiceModifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    val typography = BetterTravelTheme.typography
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Md, CircleShape)
                .clip(CircleShape)
                .background(colors.glassStrong)
                .border(1.dp, colors.glassBorder, CircleShape)
                .padding(BtSpacing.xs + 3.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onAdd != null) {
            BtIconButton(Res.drawable.ic_add, onClick = onAdd, style = BtIconButtonStyle.Plain, size = 36.dp)
        }
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = BtSpacing.m),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (value.isEmpty()) {
                Text(text = placeholder, style = typography.bodySmall, color = colors.textTertiary)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = typography.bodySmall.copy(color = colors.textPrimary),
                cursorBrush = SolidColor(colors.primary),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (canSend) onSend() }),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        BtIconButton(
            Res.drawable.ic_mic,
            onClick = onVoice,
            modifier = voiceModifier,
            style = BtIconButtonStyle.Tonal,
            size = 36.dp,
            iconSize = 16.dp,
        )
        BtIconButton(
            Res.drawable.ic_send,
            onClick = onSend,
            style = BtIconButtonStyle.Brand,
            size = 36.dp,
            iconSize = 16.dp,
            enabled = canSend,
        )
    }
}

@Preview
@Composable
private fun BtInputPillPreview() {
    BtPreview {
        BtInputPill(
            value = "",
            onValueChange = {},
            placeholder = "Ask Betty anything or adjust route...",
            onSend = {},
            onVoice = {},
            onAdd = {},
        )
    }
}
