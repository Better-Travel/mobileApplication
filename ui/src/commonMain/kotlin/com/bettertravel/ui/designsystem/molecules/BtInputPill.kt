package com.bettertravel.ui.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
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
import com.bettertravel.ui.resources.ic_add
import com.bettertravel.ui.resources.ic_arrow_right
import com.bettertravel.ui.resources.ic_mic

private val ButtonSize = 40.dp

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
    var field by remember { mutableStateOf(TextFieldValue(value)) }
    if (field.text != value) {
        field = TextFieldValue(text = value, selection = TextRange(value.length))
    }
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Md, CircleShape)
                .clip(CircleShape)
                .background(colors.glassStrong)
                .border(1.dp, colors.glassBorder, CircleShape)
                .heightIn(min = BtSpacing.inputPillHeight)
                .padding(horizontal = BtSpacing.s, vertical = BtSpacing.s),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onAdd != null) {
            BtIconButton(
                icon = Res.drawable.ic_add,
                onClick = onAdd,
                style = BtIconButtonStyle.Plain,
                size = ButtonSize,
                iconSize = 24.dp,
                tint = colors.placeholder,
            )
        }
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = BtSpacing.s),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = typography.input,
                    color = colors.placeholder,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            BasicTextField(
                value = field,
                onValueChange = { updated ->
                    field = updated
                    if (updated.text != value) {
                        onValueChange(updated.text)
                    }
                },
                textStyle = typography.input.copy(color = colors.textPrimary),
                cursorBrush = SolidColor(colors.brand),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (canSend) onSend() }),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        BtIconButton(
            icon = Res.drawable.ic_mic,
            onClick = onVoice,
            modifier = voiceModifier,
            style = BtIconButtonStyle.Tonal,
            size = ButtonSize,
            iconSize = 20.dp,
        )
        BtIconButton(
            icon = Res.drawable.ic_arrow_right,
            onClick = onSend,
            style = BtIconButtonStyle.Brand,
            size = ButtonSize,
            iconSize = 20.dp,
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
            canSend = false,
        )
    }
}

@Preview
@Composable
private fun BtInputPillTypedPreview() {
    BtPreview {
        BtInputPill(
            value = "Plan 3 hours in Warsaw",
            onValueChange = {},
            placeholder = "",
            onSend = {},
            onVoice = {},
            onAdd = {},
            canSend = true,
        )
    }
}
