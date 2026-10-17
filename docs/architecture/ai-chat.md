# AI chat and voice mode

Screens: Figma "AI Conversation State" (3:70) and "Live Voice Mode" (3:199). Opened from the Discover input and mic.

## Layers

| Layer | Content |
|---|---|
| `domain.chat` | `ChatMessage`, `ChatAuthor`, `SuggestedRoute`, `ChatReply`; `AIChatting.reply(history): Single<ChatReply>` |
| `domainMock` | `AIChattingMock` (configurable result, records the histories it received) |
| `networking.chat` | `HttpBasedAIChatting`: `POST /chat` with `{"messages":[{"role","text"}]}`, returns `{"text","route"?}` |
| `mockServer` | `POST /chat` with canned replies: walk/route/Warsaw returns the Old Town loop, "shorter" trims it, coffee returns a text tip |
| `presentation.aichat` | ViewModels below |
| `ui.aichat` | `AIChatScreen`, `AIChatContent`, `AIVoiceContent`, `AIChatItems`, `AIChatStub` |

There is no real AI backend yet; the mock server stands in for it. Swap `HttpBasedAIChatting` or point it at a real
endpoint without touching presentation.

## ViewModels

```kotlin
interface AIChatViewModel : ViewModel {
    val title: String
    val status: String
    val items: BehaviorObservable<List<Item>>
    val loading: BehaviorObservable<Boolean>
    val stub: Observable<AIChatStubViewModel?>
    val mode: BehaviorObservable<AIChatMode>      // Text or Voice
    val inputViewModel: AIChatInputViewModel
    val voiceViewModel: AIVoiceViewModel
    fun back()
    sealed interface Item { DateDivider, UserMessage, AssistantMessage, QuickReplies }
}
```

- `AIChatInputViewModel`: `text`, `canSend` (non-blank and not waiting for a reply), `onTextChanged`, `send`, `startVoice`.
- `AIUserMessageViewModel`, `AIAssistantMessageViewModel` (with optional `AIRouteCardViewModel`),
  `AIQuickRepliesViewModel`: plain view data plus actions.
- `AIChatStubViewModel`: empty state (title, message, suggestion chips) and error state (retry action).
- `AIVoiceViewModel`: `isListening`, `statusText`, `transcript`, `suggestions`, `toggleListening`, `select`, `close`.

`AIChatViewModelImpl` owns the conversation (a list of `ChatMessage`), rebuilds `items` after every change, tracks
loading with `ActivityIndicator`, and registers a back callback so system back leaves voice mode first.

## Voice mode

Tapping the mic in the input switches `mode` to `Voice` and starts "listening". The UI uses a shared-element transition
(`SharedTransitionLayout` + `AnimatedContent`) so the mic button moves from the input pill to the centre of the voice
dock and grows into the hero mic. While listening, the mic shows pulsing rings (`BtPulsingMic`), the live badge dot and
the mascot halo pulse. Closing voice mode, the keyboard button and system back return to the text chat.

Speech recognition and speech synthesis are not implemented. The transcript shows the last two chat messages, and the
suggestion chips send their phrase through the same chat pipeline, so voice mode is functional end to end without audio.

## Open items

- Wire "Preview on map" and "Start walk" (`TODO` in `DefaultRootComponent`) once the Route screen exists.
- Real speech input and output per platform (`expect/actual` in `common` or platform modules).
- Persist conversations if needed (the `database` module is available).
