package com.bettertravel.ui.aichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bettertravel.presentation.aichat.AIVoiceViewModel
import com.bettertravel.ui.designsystem.molecules.BtVoiceSuggestionChip
import com.bettertravel.ui.designsystem.molecules.BtVoiceTopBar
import com.bettertravel.ui.designsystem.organisms.BtTranscriptCard
import com.bettertravel.ui.designsystem.organisms.BtTranscriptEntry
import com.bettertravel.ui.designsystem.organisms.BtVoiceControlDock
import com.bettertravel.ui.designsystem.organisms.BtVoiceStage
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.preview.PreviewAIChat
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
            if (transcript.isEmpty()) {
                Text(
                    text = viewModel.emptyTranscript,
                    style = BetterTravelTheme.typography.body,
                    color = BetterTravelTheme.colors.textSecondary,
                )
            } else {
                BtTranscriptCard(
                    header = viewModel.transcriptTitle,
                    liveLabel = viewModel.liveLabel,
                    entries = transcript.map { BtTranscriptEntry(it.speaker, it.text, it.fromUser) },
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
