package com.bettertravel.ui.aichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.bettertravel.presentation.aichat.AIVoiceRoute
import com.bettertravel.presentation.aichat.AIVoiceViewModel
import com.bettertravel.ui.designsystem.atoms.BtButton
import com.bettertravel.ui.designsystem.molecules.BtVoiceSuggestionChip
import com.bettertravel.ui.designsystem.molecules.BtVoiceTopBar
import com.bettertravel.ui.designsystem.organisms.BtTranscriptCard
import com.bettertravel.ui.designsystem.organisms.BtTranscriptEntry
import com.bettertravel.ui.designsystem.organisms.BtVoiceControlDock
import com.bettertravel.ui.designsystem.organisms.BtVoiceStage
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.preview.PreviewAIChat
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_navigation
import com.bettertravel.ui.rx.collectAsState

@Composable
fun AIVoiceContent(
    viewModel: AIVoiceViewModel,
    modifier: Modifier = Modifier,
    micModifier: Modifier = Modifier,
) {
    val isListening by viewModel.isListening.collectAsState()
    val status by viewModel.statusText.collectAsState(initial = "")
    val transcript by viewModel.transcript.collectAsState()
    val partial by viewModel.partialText.collectAsState()
    val route by viewModel.route.collectAsState()
    val entries =
        transcript.map { entry -> BtTranscriptEntry(entry.speaker, entry.text, entry.fromUser) } +
            listOfNotNull(partial?.let { text -> BtTranscriptEntry(viewModel.partialSpeaker, "$text…", true) })

    Column(
        modifier = modifier.fillMaxSize().background(BtGradients.voice),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BtVoiceTopBar(status = status, isActive = isListening, onClose = { viewModel.close() })
        BtVoiceStage(isListening = isListening, modifier = Modifier.weight(1f).fillMaxWidth())
        Column(
            modifier = Modifier.padding(horizontal = BtSpacing.xxl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BtSpacing.m),
        ) {
            if (entries.isEmpty()) {
                Text(
                    text = viewModel.emptyTranscript,
                    style = BetterTravelTheme.typography.body,
                    color = BetterTravelTheme.colors.textSecondary,
                )
            } else {
                BtTranscriptCard(
                    header = viewModel.transcriptTitle,
                    liveLabel = viewModel.liveLabel,
                    entries = entries,
                )
            }
            route?.let { suggested ->
                VoiceRouteCard(
                    route = suggested,
                    previewTitle = viewModel.previewRouteTitle,
                    onPreview = { viewModel.previewRoute() },
                )
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(BtSpacing.s),
            ) {
                viewModel.suggestions.forEach { suggestion ->
                    BtVoiceSuggestionChip(text = suggestion, onClick = { viewModel.select(suggestion) })
                }
            }
            BtVoiceControlDock(
                isListening = isListening,
                onKeyboard = { viewModel.close() },
                onMic = { viewModel.toggleListening() },
                onEnd = { viewModel.close() },
                micModifier = micModifier,
                modifier = Modifier.padding(bottom = BtSpacing.xxl),
            )
        }
    }
}

@Composable
private fun VoiceRouteCard(
    route: AIVoiceRoute,
    previewTitle: String,
    onPreview: () -> Unit,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(BtShapes.xl)
                .background(colors.glassStrong)
                .padding(BtSpacing.m),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BtSpacing.xxs)) {
            Text(
                text = route.title,
                style = BetterTravelTheme.typography.title,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(text = route.details, style = BetterTravelTheme.typography.bodySmall, color = colors.textSecondary)
        }
        BtButton(text = previewTitle, onClick = onPreview, leadingIcon = Res.drawable.ic_navigation)
    }
}

@Preview
@Composable
private fun AIVoiceContentPreview() {
    BtPreview {
        AIVoiceContent(PreviewAIChat.voiceViewModel(withTranscript = true))
    }
}

@Preview
@Composable
private fun AIVoiceContentEmptyPreview() {
    BtPreview {
        AIVoiceContent(PreviewAIChat.voiceViewModel(withTranscript = false))
    }
}
