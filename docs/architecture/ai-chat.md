# AI chat and voice mode

Screens: Figma "Clean Discovery Landing" (3:2), "AI Conversation State" (3:70) and "Live Voice Mode" (3:199).

The Discover tab **is** the chat. With no messages, the chat's empty state (`AIChatStubViewModel` with an accent title)
renders the Discover landing: heading, subtitle and Betty. The input pill at the bottom is the real input field, so the
user types and sends from the landing itself, and the conversation replaces the landing once the first message is sent.
There is no separate chat destination.

## Layers

| Layer | Content |
|---|---|
| `domain.chat` | `ChatMessage`, `ChatAuthor`, `SuggestedRoute`, `ChatReply`; `AIChatting.reply(history): Single<ChatReply>` |
| `domainMock` | `AIChattingMock` (configurable result, records the histories it received) |
| `networking.chat` | `HttpBasedAIChatting`: `POST /chat` with `{"messages":[{"role","text"}]}`, returns `{"text","route"?}` |
| `mockServer` | `POST /chat` with canned replies: walk/route/Warsaw returns the Old Town loop, "shorter" trims it, coffee returns a text tip |
| `presentation.aichat` | ViewModels below |
| `ui.aichat` | `AIChatContent`, `AIVoiceContent`, `AIChatItems`, `AIChatStub` (landing and error layouts) |

There is no real AI backend yet; the mock server stands in for it. Swap `HttpBasedAIChatting` or point it at a real
endpoint without touching presentation.

## ViewModels

```kotlin
interface AIChatViewModel : ViewModel {
    val title: String
    val items: BehaviorObservable<List<Item>>
    val loading: BehaviorObservable<Boolean>
    val stub: Observable<AIChatStubViewModel?>
    val mode: BehaviorObservable<AIChatMode>      // Text or Voice
    val inputViewModel: AIChatInputViewModel
    val voiceViewModel: AIVoiceViewModel
    sealed interface Item { DateDivider, UserMessage, AssistantMessage }
}
```

- `AIChatInputViewModel`: `placeholder` (observable: "Tell BetterTravel where & how long..." until the first message, then "Ask Betty anything or adjust route..."), `text`, `suggestions` (the "Suggested adjustments" chips, shown after a reply with a route and cleared on the next message), `canSend` (non-blank and not waiting for a reply), `onTextChanged`, `send`, `startVoice`.
- `AIUserMessageViewModel`, `AIAssistantMessageViewModel` (with optional `AIRouteCardViewModel`),
  `AIQuickRepliesViewModel` (the suggestions): plain view data plus actions.
- `AIChatStubViewModel`: empty state (`title` + `accentTitle` + `message`, shown as the Discover landing) and error state (retry action).
- `AIVoiceViewModel`: `isListening`, `statusText`, `transcript`, `suggestions`, `toggleListening`, `select`, `close`.

`AIChatViewModelImpl` owns the conversation (a list of `ChatMessage`), rebuilds `items` after every change, tracks
loading with `ActivityIndicator`, and registers a back callback so system back leaves voice mode first.

## Voice mode

Tapping the mic in the input switches `mode` to `Voice` and starts "listening". The UI uses a shared-element transition
(`SharedTransitionLayout` + `AnimatedContent` in `MainScreen`, keyed on `AIChatViewModel.mode`) so the mic button moves from the input pill to the centre of the voice
dock and grows into the hero mic. While listening, the mic shows pulsing rings (`BtPulsingMic`), the live badge dot and
the mascot halo pulse. Voice mode is a full-screen overlay above the tab bar and top bar. Closing it, the keyboard button and system back return to the text chat. While the software keyboard is visible, the tab bar is hidden and the input pill sits directly above the keyboard.

When the latest assistant reply carries a route, voice mode shows a route card (title, duration and distance) with a
"Preview on map" button. It stops listening and speech, leaves voice mode and previews the route on the Route tab, exactly
like the button on the chat's route card (`AIVoiceViewModel.route` / `previewRoute()`).

### Speech input (STT, native)

`domain.speech.SpeechRecognizing.listen(languageTag): Observable<SpeechEvent>` is a cold stream: subscribing opens the
microphone, disposing closes it. It emits `Partial(text)` while the user speaks and one `Final(text)`; failures arrive as
`SpeechRecognitionException(SpeechFailure.PermissionDenied | Unavailable | NoSpeech | Failed)`.

| Platform | Implementation |
|---|---|
| Android | `AndroidSpeechRecognizer` (`android:platform`) on `android.speech.SpeechRecognizer`; `MainActivity` registers the `RECORD_AUDIO` launcher through `MicrophonePermission.requester` |
| iOS | `IosSpeechRecognizer` (`application`, iosMain) on `SFSpeechRecognizer` + `AVAudioEngine`; asks for speech and microphone permission itself and ends the request after 1.5 s of silence |

`AIVoiceViewModelImpl` subscribes when the mic is tapped, shows the partial text as a live line in the transcript, sends
the final phrase through the normal chat pipeline and maps failures to a status message. The recognizers emit on the main
thread, so the ViewModel stays scheduler-agnostic and testable with `SpeechRecognizingMock`. The app language
(`PlatformConfig.languageCode`) selects the recognition language.

### Speech output (TTS, Google Cloud)

`SpeechSynthesizing.speak(text, languageTag): Completable` is cold: subscribing speaks, completing means finished,
disposing stops. `GoogleSpeechSynthesizing` (`networking`) calls `text:synthesize` (Neural2 voices: `en-US-Neural2-F`,
`fr-FR-Neural2-A`, MP3) and hands the audio to `AudioPlaying`, implemented natively by `AndroidAudioPlayer`
(`MediaPlayer`) and `IosAudioPlayer` (`AVAudioPlayer`).

Betty speaks only in voice mode: `AIChatViewModelImpl` calls `AIVoiceViewModelImpl.speak` when a reply arrives while the
mode is `Voice`. Speech stops when the user starts listening, sends a message or closes voice mode. The key is read from
`google.tts.apiKey` in the git-ignored `local.properties` (or the `GOOGLE_TTS_API_KEY` environment variable) and generated
into `BuildSecrets`, so it ends up in the app binary; use a backend proxy before release. Without a key `speak` fails
with `Unavailable` and voice mode stays silent.

## Open items

- Wire "Preview on map" and "Start walk" (`TODO` in `DefaultRootComponent`) once the Route screen exists.
- Persist conversations if needed (the `database` module is available).

## Suggestions

The suggestion chips belong to the input, not to the conversation: they are exposed by `AIChatInputViewModel.suggestions`
and rendered in the floating area above the input pill, outside the scrolling list. The chips wrap onto several lines
instead of scrolling horizontally. The list measures the height of that floating area and pads its bottom accordingly,
so messages scroll underneath it.

## Scroll position

`AIChatViewModel.scrollPosition` stores the list's first visible item and offset. The list restores it when the tab is
shown again and only scrolls to the bottom when a new message arrives. It lives in memory, so it survives tab switches and
rotation but not an app restart.
