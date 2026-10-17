# Design system

Location: `ui/src/commonMain/kotlin/com/bettertravel/ui/designsystem/`. Source: the BetterTravel Figma file
(see `docs/design/design-tokens.md` for the full extracted token and component inventory).

## Decisions

- The Figma file contains two visual languages. Palette B ("Lavender": `#faf8ff` background, `#006590` primary, coral
  and gold accents) is the base. From palette A we keep the brand blue `#4ea8de`, the glass pill bars and the
  Liberation Sans Bold app title.
- Mascot name: **Betty** (the file also uses Nimbus and Cirrus). The image is `drawable/betty.png`.
- Fonts (Compose resources, `ui/src/commonMain/composeResources/font/`): Plus Jakarta Sans and Inter (variable TTF,
  SIL OFL, from the Google Fonts repository) and Liberation Sans Bold (SIL OFL, app title only).
- Icons: Figma SVGs converted to Android vector drawables in `composeResources/drawable/ic_*.xml`, tinted at use.
  Colour variants were merged (e.g. `ic_compass_blue` is `ic_compass`). `ic_tab_discover_active` was skipped because it
  uses a clip path.
- Backdrop blur from the design is not available in Compose Multiplatform; glass surfaces use translucent fills.
- Previews use `androidx.compose.ui.tooling.preview.Preview` (`org.jetbrains.compose.ui:ui-tooling-preview`).

## Structure

| Package | Content |
|---|---|
| `theme` | `BetterTravelTheme`, `BtColors`, `BtGradients`, `BtTypography`, `BtSpacing`, `BtShapes`, `BtIconSize`, `BtElevation` and `btShadow` |
| `atoms` | `BtIcon`, `BtDot`, `BtAvatar`, `BtBetty`, `BtButton`, `BtIconButton`, `BtFilterChip`, `BtQuickReplyChip`, `BtBadge`, `BtDurationTag`, `BtRating`, dividers, `BtProgressBar`, `BtSwitch`, `BtDragHandle`, `BtNumberMarker` |
| `molecules` | top bars (home, chat, detail), `BtTabBar`, `BtInputPill`, chat bubbles, `BtStatTile`, `BtStatsStrip`, `BtSectionHeader`, `BtInfoRow`, `BtSettingRow`, `BtWaypointCard`, `BtCityCard`, `BtInsightCard`, `BtSummaryBanner`, `BtXpBanner`, `BtCompanionPrompt`, `BtAudioGuideWidget` |
| `organisms` | `BtBottomSheet`, `BtItinerarySheet`, `BtDestinationCard`, `BtEmbeddedRouteCard`, `BtTurnGuidanceCard`, `BtMemoryCard`, `BtMetricsStrip`, `BtJournalNote`, `BtTranscriptCard`, `BtVoiceControlDock`, `BtMapPlaceholder` |
| `layout` | `BtSafeArea`: applies `WindowInsets.safeDrawing` once, at the top of `RootContent` |
| `preview` | `BtPreview` wrapper used by previews |

## Rules

- Read colours, text styles, shapes and spacing from `BetterTravelTheme` / `BtShapes` / `BtSpacing`; no raw hex values or
  font sizes in screens.
- Components are stateless and take plain data (strings, lambdas). Screen text comes from the ViewModel
  (see `mvvm.md`); sample text exists only in previews.
- Every UI composable has a `@Preview` (private preview functions at the bottom of the file, wrapped in `BtPreview`).
  Non-visual helpers (`collectAsState`, `rememberBtTypography`) are exempt.
- Components that only show data from a ViewModel are split into a stateful `XxxScreen(viewModel)` and a stateless
  `XxxContent(...)` that carries the previews (see `RoutesListScreen.kt`).
- Pressed, disabled, focus and error states are not designed in Figma; `BtButton` supports `enabled` only as a
  functional flag without a distinct style yet.

## Safe areas

`RootContent` wraps the whole navigation stack in `BtSafeArea`, which paints the background edge to edge and then pads
the content by `WindowInsets.safeDrawing` (status bar, navigation bar, display cutout, keyboard). Screens and
components must not apply system-bar insets themselves; if a screen needs content under the bars (a map or a hero image),
pass narrower `insets` for that subtree. Android calls `enableEdgeToEdge()` in `MainActivity`; iOS uses
`.ignoresSafeArea()` in SwiftUI and Compose reads the safe area itself.

## Known gaps

- Map is a placeholder (`BtMapPlaceholder`); a map SDK has not been chosen.
- Photos in the design are placeholders; cards take a `DrawableResource` thumbnail for now.
- Letter spacing, shadows and some gradients are approximations of the Figma values (Compose shadows differ from CSS).
- Previews were compiled for Android and iOS but not visually reviewed yet; compare them with Figma in Android Studio.
