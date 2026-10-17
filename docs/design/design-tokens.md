# BetterTravel design tokens and component inventory

Source: Figma file D6YhVjHcpzK2dS1uq2qAw6 (https://www.figma.com/design/D6YhVjHcpzK2dS1uq2qAw6/BetterTravel), read from raw values in the generated code of each frame (the file has no Figma variables). All frames are mobile, 390 px wide. Assets are in `/private/tmp/claude-503/-Users-anhelina-sudenkova-AndroidStudioProjects-BetterTravel/e9ece3d1-1c4f-4f68-97f8-c3cafd84a3f4/scratchpad/figma-assets/` (see its `manifest.md`).

Status note: this is a recommendation for human review, not a definitive conclusion. It was produced by reading generated code (plus a few screenshots) and may contain misreadings. Values marked "verify" are uncertain.

Frame 3:2 (Clean Discovery Landing) was analysed earlier and is only summarised here from the brief; its values were not re-read.

## 0. Most important finding: the file contains TWO visual languages

The frames split into two groups that disagree on palette, font and tab labels. A design-system build has to decide whether to unify them. Everything below is tagged A or B.

| | Palette A "Slate and Sky" | Palette B "Lavender Material-like" |
|---|---|---|
| Frames | 3:2, 3:70, 3:199, 8:549, 9:995 | 3:270 (sheet, cards, nav), 9:764, 9:1194, 9:1325, 9:1455, 9:1724 |
| Page background | #f8fafc / gradient #f9fafb to #f1f4f9 | #faf8ff |
| Text | #0f172a, #1e293b, #334155, #475569, #64748b, #94a3b8 | #131b2e, #3f484f, #6f7880 |
| Primary action | #4ea8de (chat, input) and #0284c7 (route screens) | #006590 (most), #4ea8de (3:270 CTA, filter chip) |
| Secondary accent | amber #f59e0b / #b45309 | coral #fe9d7c / #94492e / #773219, gold #ffdf9b / #785a00 |
| Body font | Plus Jakarta Sans (chat), Inter (a few stats) | Inter Regular body, Plus Jakarta Sans headings and labels |
| Tab labels | Discover, Route, Memories | Explore, Route, Memories (3:270, 9:1455), Discover in 8:549 |
| Radii | 9999 pills, 12, 16, 24, 32 | 16, 24, 32, 48 (list thumbnails), 9999 |

Possible bridge (recommendation only): both share brand blue #4ea8de, the app title in Liberation Sans Bold 20/30, the mascot, the pill/glass language (white at 0.85 to 0.95 alpha with backdrop blur 12 to 20) and the same app bar and bottom-pill-bar structure. The 3:270 frame mixes both (A app bar and A-style header, B sheet and cards).

Other cross-frame inconsistencies:
- Fonts: the brief lists 'Liberation Sans' for headings and 'Roboto' for body (from 3:2). In the other frames the dominant font is **Plus Jakarta Sans** (Bold, ExtraBold, SemiBold, Medium, Regular); **Inter** appears for body/stat text; **Liberation Sans Bold** appears only for the "BetterTravel" app title (20/30); **Liberation Mono Regular** only for an audio duration (0:24). **Roboto does not appear in any frame read here.** 'Liberation Sans' and 'Roboto' look like Figma fallbacks for unavailable fonts (verify what 3:2 really uses).
- Mascot name: the same cloud character is called Nimbus (layer names, "Ask Nimbus", "Nimbus Voice Guide", "Meet Nimbus"), Betty (3:70 "with Betty", 3:199 "Betty is listening...", 9:764 "Betty SECRET", the brief) and Cirrus (9:1325 "Cirrus the Cloud Mascot", "by your side", 9:1455 "Cirrus Voice Memory", "Cirrus suggested"). One name must be chosen; a text-resource decision, not a layout one.
- Node 9:764 (DetailsScreen): almost every text is 16/24 (Plus Jakarta Medium for labels, Inter Regular for body), including the stat chips, badges, header and button labels. This looks like an unstyled/auto-layout default rather than intentional design (the other frames use 10 to 14 px for the same roles). Treat the 16 px sizes there as suspect; its structure and colours are reliable.
- Details/complete/navigate/memories header title text is placeholder-ish ("Live Navigation Hud", "Trip Milestone Celebration") and is the same header component with a different title.
- Side drawer (9:1724) list cards and tiles have no radius emitted in the generated code, and the screenshot confirms square corners. Every other card in the file is rounded (16 to 32). Probably a design omission; flag.
- 8:549 contains text map labels (NOWE MIASTO, STARE MIASTO, POWIŚLE, ...) whose text nodes have opacity 0 (hidden). Not part of the DS.
- The tab bar exists in three styles (section 4, TabBar variants) with different heights, label sizes and active indicators.
- Dates in the mock data are Feb 2026; copy is sample content, not DS.
- 9:995 "RouteScreenPointSelected" is 8:549 plus one extra component (Stop Preview Card). The map inside is vector art; use a real map SDK.

## 1. Colour palette

Alpha colours are written as rgba(r,g,b,a). Roles are my recommendation.

### 1.1 Palette A (Slate and Sky)

Text
| Token (suggested) | Value | Role / where |
|---|---|---|
| a.text.title | #0f172a | Titles, app title, hamburger bars, pin labels |
| a.text.strong | #1e293b | Badge titles (Optimal Loop, Warsaw secret?, Old Town & Courtyards) |
| a.text.body | #334155 | Chat body, chip text, tab label 3:2 |
| a.text.muted | #475569 | Tab label inactive (3:70), waypoint names, voice chip text, expand hint |
| a.text.secondary | #64748b | Secondary text, stat separators text, "OLD TOWN LOOP" tag |
| a.text.placeholder | #94a3b8 | Placeholders, timestamps, "with Betty", inactive tab (8:549) |
| a.text.disabledish | #cbd5e1 | Bullet separators between stats |
| a.text.onPrimary | #ffffff | Text on primary buttons and user bubble |

Brand and accents
| a.brand.primary | #4ea8de | Active tab 3:70, send button, Start Walk, route badge text |
| a.brand.primaryDeep | #3a86ff | End of user bubble gradient and dock shadow colour |
| a.sky.600 | #0284c7 | Route screen CTA START, active tab (Route), distance numerals |
| a.sky.700 | #0369a1 | "Regenerate" label |
| a.sky.500 | #0ea5e9 | Route overview dot |
| a.sky.400 | #38bdf8 | Prompt-card status dot, shadow tint rgba(56,189,248,x) |
| a.sky.100/50 | #e0f2fe / #f0f9ff | Icon tile backgrounds, "Live"/"Selected" badge fill, waypoint borders |
| a.sky.200 | #bae6fd | Waypoint connector line (2 px) |

Status
| a.status.online | #10b981 (ring pulse #34d399 at 0.75) | Online dot, live indicator |
| a.status.amber | #f59e0b | Route status dot (8 px) next to "Today's Warsaw Route" |
| a.status.warnFill/warnText | #fef3c7 / #b45309 | Waypoint 3 number tile (café) |

Surfaces and borders
| a.bg.screen | #f8fafc (3:70), #f6f8fb (8:549 map bg), gradient see 1.3 | Screen backgrounds |
| a.surface.pill | rgba(255,255,255,0.95) | Dock/tab bar, FAB, bottom sheet, input pill 0.9 |
| a.surface.glass | rgba(255,255,255,0.7/0.75/0.8/0.82/0.85/0.9) | Header 0.7/0.85, voice buttons 0.75, transcript 0.82, bubble 0.85, chips 0.9 |
| a.surface.date | rgba(226,232,240,0.5) | "TODAY" date pill |
| a.border.light | #f1f5f9 | Header bottom border (0.6), divider in sheet, avatar border |
| a.border.default | #e2e8f0 (rgba(226,232,240,0.6/0.8/0.9)) | Pills, FABs, sheet top border, secondary buttons |
| a.border.sky | #e0f2fe, rgba(224,242,254,0.8) | Route preview card, avatar ring |
| a.border.white | #ffffff, rgba(255,255,255,0.8/0.85/0.9) | Glass borders |
| a.handle | #cbd5e1 | Sheet drag handle, home indicator (at 0.8) |

### 1.2 Palette B (Lavender Material-like)

| Token (suggested) | Value | Role / where |
|---|---|---|
| b.bg | #faf8ff | Page background, header base (at 0.85/0.9 alpha) |
| b.surface.container | #f2f3ff | Cards, stat tiles, rows, metrics strips, summary banner |
| b.surface.containerHigh | #eaedff | Secondary buttons, filter chips (inactive), thumbnails placeholder, voice toggle |
| b.surface.containerHighest | #e2e7ff | Icon circles, rating badge, "Open Now" badge, progress track |
| b.surface.containerMax | #dae2fd | Drag handle (3:270), vertical dividers, photo placeholder |
| b.surface.white | #ffffff | Waypoint cards, memory cards |
| b.surface.overlay | rgba(255,255,255,0.95/0.9/0.85/0.8) | Glass cards (nav), nav bar 0.9, header 0.85 |
| b.text.primary | #131b2e (also on-image #faf8ff) | Titles and values |
| b.text.secondary | #3f484f | Body, captions, labels |
| b.text.tertiary | #6f7880 | Meta text ("1,420 reviews", "· Departure 10:00 AM", separators) |
| b.outline.variant | #bfc7d0 | Waveform inactive bars, dashed spine (0.4), "·" separator |
| b.primary | #006590 | Primary buttons, active tab, links, progress start, ring (rgba(0,101,144,0.2)) |
| b.primary.container | #c8e6ff | "Walkable" chip, selected city fill at 0.3, complete indicator |
| b.on.primary.container | #004c6d | Text on #c8e6ff |
| b.brand.blue | #4ea8de | 3:270 CTA, active filter chip, waveform mid bars, counter icon tile at 0.2 |
| b.accent.coral | #fe9d7c | Stop pin 3, bullets, level badge fill at 0.2/0.3, glow |
| b.accent.coralLight | #ffdbd0 | "Arriving" chip, medal-small circle |
| b.accent.coralText | #94492e | Eyebrow text, "Remove from this walk", stop 4 pin fill |
| b.accent.coralDeep | #773219 | Badge text (Level, ROUTE COMPLETE), pin 3 numeral |
| b.accent.coralInk | #390c00 | Text on #ffdbd0 chip |
| b.accent.coralSoft | #ffb59c | Mid stop of the "Route Complete" badge gradient |
| b.gold | #ffdf9b | Sparkle disc, badge gradient end, calendar/duration tile at 0.6 |
| b.gold.dark | #785a00 | Ratings text and star, gradient start of XP badge |
| b.scrim.dark | rgba(19,27,46,0.6 / 0.4 / 0.7) | Photo caption chips, drawer scrim (0.4), photo gradient bottom (0.7) |
| b.status | none distinct | (no red or green status colours in B) |

Semantic icon colours observed inside the SVGs (assets): pink/red exit icon (about #f43f5e, end session), brown #773219-like glyphs, olive #785a00-like glyphs, dark navy #131b2e glyphs, blue #006590 glyphs.

### 1.3 Gradients (stops and angles in CSS degrees)

| Name | Definition | Where |
|---|---|---|
| a.page | vertical (180deg) #f9fafb to #f6f8fc to #f1f4f9 | 3:2 page background (from brief) |
| a.heading text | 136deg #3ea5e6 to #60a5fa at 55% to #94a3b8 | 3:2 heading text (from brief) |
| a.userBubble | 157.28deg #4ea8de (0%) to #3a86ff (100%) | User message bubble (3:91) |
| a.voiceBg | 180deg #f2f8fd (0%), #f8fafc (50%), #eff6fb (100%) | Live voice screen background |
| a.micButton | 45deg #4ea8de to #65baed | Hero mic button (64 px) |
| a.avatarRing | 45deg #7dd3fc (0%), #fbcfe8 (50%), #fde68a (100%) | Chat header avatar ring |
| a.avatarSoft | 45deg #e0f2fe to #eef2ff | Profile button inner (8:761) |
| a.mascotRingMap | 45deg #4ea8de to #fe9d7c | Mascot mini-button ring on map (36 px) |
| a.routePreviewCard | vertical rgba(240,249,255,0.6) to rgba(248,250,252,0.8) | Embedded route preview card |
| a.ambientTop | vertical rgba(186,230,253,0.4), mid rgba(243,232,255,0.3), end rgba(243,232,255,0) blurred 32, 384 circle at top -96 | Chat background glow |
| a.ambientRight | 45.9deg rgba(254,243,199,0.35), rgba(255,228,230,0.25) at 50%, rgba(255,228,230,0) blurred 32, 288 wide | Chat background glow |
| a.halo (voice) | radial rgba(186,221,255,0.5), rgba(254,215,226,0.25) at 45%, 0 at 70%; blur 14; 256 circle behind mascot | Voice screen |
| a.haloAmbient (voice) | radial rgba(78,168,222,0.28), rgba(167,163,174,0.26) at 22.5%, rgba(255,158,125,0.24) at 45%, rgba(253,224,71,0.12) at 70%, 0 at 85%; blur 18; 320 circle | Voice screen |
| b.avatarCompanion | 45deg #fe9d7c, #4ea8de at 50%, #faf8ff at 100% | Details insight card avatar ring (48) |
| b.avatarMini | 45deg #ffdbd0 to #c8e6ff | Navigate audio widget ring (44) |
| b.progress | horizontal #006590 to #fe9d7c | Navigate progress fill |
| b.routeCompleteBadge | horizontal #fe9d7c, #ffb59c at 50%, #ffdf9b | "ROUTE COMPLETE" pill |
| b.xpBadge | 45deg #785a00 to #ffdf9b | XP medal circle (48) |
| b.warmAura | 45deg rgba(254,157,124,0.3), rgba(255,223,155,0.4) at 50%, rgba(78,168,222,0.2) blur 20, 256 circle | Completion screen behind mascot |
| b.mapTint | vertical rgba(0,101,144,0.1), rgba(0,101,144,0) at 50%, rgba(255,255,255,0.8) | Navigate map overlay |
| b.mapFadeTop | vertical rgba(250,248,255,0.3), 0.6 at 50%, #faf8ff | Completion map scrim |
| b.heroScrim | transparent to dark, vertical (asset hero_gradient_overlay.svg, 390x374) | Details hero |
| b.photoCaption | upward rgba(19,27,46,0.7) at bottom to 0 at 50% | Memory highlight photo |
| b.coralGlow | rgba(254,157,124,0.2) discs, blur 20 (and blur 2 on 48 disc) | Banner and side-nav city decorations |

## 2. Typography scale (deduplicated)

Format: size/line-height, weight, letter spacing. Family PJS = Plus Jakarta Sans, Inter, LibSans = Liberation Sans, LibMono = Liberation Mono. Letter spacing is in px as designed (convert to em or sp if needed). "uc" = uppercase.

| Name (suggested) | Family | Weight | Size / line | Tracking | Used for |
|---|---|---|---|---|---|
| display | PJS | Bold | 34/38 | -0.85 | Landing heading (3:2, from brief) |
| display.lg (B) | PJS | Bold | 28/36 | -0.56 | Memories title, side-nav home greeting |
| headline.l | PJS | Bold / ExtraBold | 22/28 | -0.33 (Bold), -0.55 (ExtraBold) | Navigate destination name, completion heading |
| appTitle | LibSans | Bold | 20/30 | -0.5 | "BetterTravel" in app bar (A header) |
| headline.m | PJS | Bold | 18/24 | -0.18 | App bar title (B header), instruction, metric values, section stat values |
| headline.m.sb | PJS | SemiBold | 18/24 | -0.18 | Counter banner, city names, memory card title, drawer stats |
| headline.m.xb | PJS | ExtraBold | 18/24 | -0.45 | "Today's Sequence" |
| statValue | PJS | Bold | 18/18 | -0.18 | Bottom-sheet stat strip values |
| title.l | PJS | Bold | 16/24 | -0.4 | Collapsed-sheet title ("Today's Warsaw Route") |
| title.l.tight | PJS | Bold | 16/16 | -0.4 | Chat header title |
| title.details | PJS | Medium | 16/20 and 16/24 | -0.4 (title), 0 | Details screen headings, labels (suspect, see section 0) |
| title.m | PJS | Bold | 14/22 | 0 | Waypoint card title |
| button.m | PJS | Bold | 14/20 | 0.14 | Button labels (M3 frames), drawer route title |
| body.chat | PJS | Regular | 14/22.75 | 0 | Chat bubble body |
| body.chat.strong | PJS | SemiBold | 14/22.75 | 0 | Inline bold, voice reply |
| body.voice | PJS | Medium | 14/22.75 | 0 | Voice user transcript (#64748b) |
| body.m | Inter | Regular | 14/22 | 0 | Home greeting subtitle, completion subtitle |
| body.s | Inter | Regular | 12/18 | 0.12 | Captions, card descriptions, (italic variant Inter Italic for quotes) |
| body.s.semibold/medium | Inter | SemiBold / Medium | 12/18 or 12/16 | 0.12 (18 lh) or 0 | Next-stop name, step labels, A-frame stat text (Inter Semi_Bold/Medium 12/16) |
| body.l | Inter | Regular / Italic | 16/24 | 0 | Details body (suspect) |
| label.l | PJS | Bold / SemiBold / Medium | 12/16 | 0.24 (M3), 0 (A) | Buttons small, chips, cap labels; SemiBold 0.3 for "Optimal Loop"; Bold 12/16 in A |
| label.l.xb | PJS | ExtraBold | 12/16 | 0.3 uc | START CTA, route badge numbers (0.24) |
| label.section | PJS | SemiBold | 12/16 | 0.6 uc | "EXPLORED CITIES", "VISITED ROUTES" |
| label.eyebrow | PJS | SemiBold | 11/14 | 0.55 uc | "BETTERTRAVEL" above title |
| label.m | PJS | Bold / SemiBold | 11/14 | 0.44 (0.55 for uc/emphasis) | Meta chips, stat captions, tab labels M3 (Bold) |
| label.m.alt | PJS | SemiBold | 11/16.5 | 0 or 0.55 uc | A-frame: date pill "TODAY", "Live" badge, transcript caption (Bold, 0.55 uc) |
| label.prompt | PJS | Bold | 11.5/14.38 | 0 | "Warsaw secret?" (also 3:2 tab label 11.5/17.25, from brief) |
| label.s | PJS | Bold / SemiBold / Medium | 10/15 | 0 | Tab label (Bold active, SemiBold/Medium inactive), timestamp (Medium), prompt sublabel SemiBold |
| label.s.xb | PJS | ExtraBold | 10.5/15.75 | 0 | Active Route tab (8:549) |
| label.s.badge | PJS | Bold | 10/14 uc | 0.5 | "SELECTED" badge |
| label.s.inter | Inter | Bold | 10/15 uc | 0.5 | "OLD TOWN LOOP" tag |
| mono.s | LibMono | Regular | 12/18 | 0.12 | Audio clip duration |
| input.text (A) | PJS | Regular | 12/normal | 0 | Chat input placeholder (3:2: 14.5, from brief) |
| pin.number | PJS | ExtraBold | 10.9 | 0 | Map pin numeral (white) |
| mapLabel | PJS | Bold | 9.4 | 0 | Map place labels (#0f172a) |

Weights in use: Regular 400, Medium 500, SemiBold 600, Bold 700, ExtraBold 800 (Plus Jakarta Sans); Regular/Italic/Medium/SemiBold/Bold (Inter).

## 3. Spacing, radii, shadows

### 3.1 Spacing (px, 4-point-ish with 2/6/10/14 half steps)
2, 3, 4, 6, 8, 10, 12, 14, 16, 20, 24, 28, 32, 36, 48, 56, 64, 112 (reserved bottom inset for floating tab bar).
Common patterns: screen horizontal padding 16 (M3 frames, chat) or 24 (headers, completion card, voice); card inner padding 12 / 14 / 15 / 16 / 17 / 21; gap between list cards 8 to 12; section gap 12 / 16 / 24; header top padding 16 (+ 48 on voice and 56/64 status-bar allowance).
Touch sizes: 36, 40, 44, 48 circle buttons; mic 64; tab item 48 high min 56 wide.
Safe areas: status bar allowance 56 (A frames) or 64 header (B); bottom floating tab bar has 8 bottom padding + 64 bar; scroll content bottom padding 112.

### 3.2 Corner radii
| Token | Value | Used for |
|---|---|---|
| radius.pill | 9999 | Chips, buttons, tab bar (A route), inputs, badges, circles |
| radius.2 / chat tail | 2 | Sharp tail corner of chat bubbles (top-right user, top-left assistant) |
| radius.xs | 6 | Small tags (duration tag 6), Live badge, icon tile 6 |
| radius.sm | 12 | Secondary buttons in embedded card |
| radius.md | 16 | Cards, tiles, route preview card, waypoint card, stat tiles |
| radius.lg | 20 | Stop preview card |
| radius.xl | 24 | Chat bubbles, transcript card, hero bottom corners, completion card, map header bottom |
| radius.2xl | 32 | Tab dock (3:70), bottom sheet top corners, nav guidance cards, memory cards, summary banner, drawer right corners |
| radius.thumb | 48 (on 64 box) | Thumbnails in lists (effectively circle on 64); stat inner map 48 on 128 box |
| radius.square (side drawer) | 0 | Drawer metric bar, city cards, route cards (design gap, see section 0) |

### 3.3 Shadows and elevations (x y blur spread colour)
| Token | Value | Used for |
|---|---|---|
| shadow.xs | 0 1 2 0 rgba(0,0,0,0.05) | Chips, small pills, avatars, glass buttons |
| shadow.drop.xs | drop 0 1 1 rgba(0,0,0,0.05) | Filled tags and number tiles |
| shadow.pillA | 0 12 35 -8 rgba(15,23,42,0.08) | Pill surfaces in 3:2 (from brief) |
| shadow.input | 0 4 20 0 rgba(0,0,0,0.06) | Chat input pill |
| shadow.bubble | 0 8 30 0 rgba(0,0,0,0.05) | Assistant bubble, assistant avatar |
| shadow.userBubble | drop 0 12 18 rgba(56,189,248,0.2) | User bubble |
| shadow.dockA | 0 12 36 0 rgba(58,134,255,0.12) | Chat bottom dock |
| shadow.voiceChip | 0 6 18 0 rgba(78,168,222,0.08) | Voice chips and circular dock buttons |
| shadow.transcript | 0 16 40 -12 rgba(78,168,222,0.12) + 0 4 12 0 rgba(0,0,0,0.03) | Live transcript card |
| shadow.md | 0 4 6 -1 rgba(0,0,0,0.1) + 0 2 4 -2 rgba(0,0,0,0.1) | Map capsules, FABs, memory cards, nav small |
| shadow.lg | 0 10 15 -3 rgba(0,0,0,0.1) + 0 4 6 -4 rgba(0,0,0,0.1) | Voice dock, Nimbus prompt card, nav quick buttons (8 px) |
| shadow.xl | 0 20 25 -5 rgba(0,0,0,0.1) + 0 8 10 -6 rgba(0,0,0,0.1) | Navigate guidance card and mascot widget |
| shadow.2xl | 0 25 50 -12 rgba(0,0,0,0.25) | Nav destination glass card, side drawer, device mock frame |
| shadow.brandBtn | 0 4 6 -1 rgba(56,189,248,0.25/0.3) + 0 2 4 -2 same | Start Walk, send button |
| shadow.mic | 0 10 15 -3 rgba(56,189,248,0.35) + 0 4 6 -4 same | Hero mic |
| shadow.skyBtn | 0 4 6 -1 rgba(2,132,199,0.25) + 0 2 4 -2 same | START CTA |
| shadow.primaryBtn (B) | 0 4 6 -1 rgba(0,101,144,0.25) + 0 2 4 -2 same | Completion primary |
| shadow.ctaBlue (B) | 0 10 24 -4 rgba(78,168,222,0.45) | 3:270 Start Route Navigation |
| shadow.sheetA | 0 -8 30 0 rgba(0,0,0,0.08) | Collapsed bottom sheet (8:549) |
| shadow.sheetB | 0 -12 40 0 rgba(19,27,46,0.08) | Bottom sheet (3:270) |
| shadow.tabBarA | 0 12 36 -6 rgba(2,132,199,0.2) + 0 2 10 0 rgba(15,23,42,0.06) | Route-style tab bar |
| shadow.tabBarB | 0 16 40 -6 rgba(78,168,222,0.2) + 0 4 12 -2 rgba(15,23,42,0.06) | M3-style tab bar |
| shadow.headerA | 0 2 12 0 rgba(0,0,0,0.03) | 56 px status header |
| shadow.headerB | 0 1 8 0 rgba(0,0,0,0.04) | App bars |
| shadow.card.completion | 0 20 25 -5 rgba(78,168,222,0.15) + 0 8 10 -6 same | Completion card |
| shadow.waypointCard | drop 0 4 8 rgba(15,23,42,0.04) | Waypoint cards (3:270) |
| shadow.pin | drop 0 4 8 rgba(0,101,144,0.25); coral pin 0 6 10 rgba(254,157,124,0.4); stop 3 pin 0 4 8 rgba(254,157,124,0.35) | Map pins |
| shadow.ring.profile | 0 0 0 2 rgba(0,101,144,0.2) (spread ring) | Header profile |
| shadow.ring.avatar | 0 0 0 1 rgba(186,230,253,0.6) + 0 1 2 rgba(0,0,0,0.05) | Prompt card avatar |
| shadow.inset.thumb | inset 0 2 4 rgba(0,0,0,0.05) | Thumbnails |

Blur (backdrop): 2 (date pill), 6 (photo chips, hero pills), 8 (voice glass), 12 (headers, input, FABs, sheet), 20 (tab bars, nav cards, drawer). Soft decorative blurs: 2, 14, 18, 20, 32.

Other sizes: icons 12 to 28 (most 14, 16, 18 to 20); avatars 32, 36, 40, 44, 48; mascot 160 / 176 / 192 / 240; border widths 1 (hairline), 2 (avatar ring), dashed 2 (timeline spine).

## 4. Component inventory

Frame tags: CH = 3:70 AI Conversation, VO = 3:199 Live Voice, RT = 8:549 RouteScreen, RP = 9:995 Point Selected, RS = 3:270 Route Bottom Sheet, DT = 9:764 Details, NV = 9:1194 Navigate, CM = 9:1325 Completion, ME = 9:1455 Memories, SN = 9:1724 Side Nav. Stated sizes are px (dp).

### 4.1 Atoms

**Icon (24 slots)**: monochrome single-colour glyphs, sizes 9 to 28 (see assets manifest, 105 unique SVGs). Colours are baked per file; Compose build should tint instead. Sizes seen: 12, 14, 16, 18.975, 20, 22 (tab active), 28 (hero mic).

**Dot / status indicator**: filled circle. Sizes 6, 8, 10. Colours: #10b981 online (CH 3:80, 6; VO 3:260, 10 with pulsing halo #34d399 at 0.75), #0ea5e9 route dot (RT 8:638, 10), #f59e0b amber (RT 8:677, 8), #38bdf8 (RT 8:666, 6), #006590 (SN 9:1792, 8; DT 9:957), #fe9d7c bullet (NV 9:1224 6; DT 9:926), #94492e bullet (CM 9:1401).

**Avatar**: circle with image. Variants: 
- Mascot avatar plain 32 (white, border #e0f2fe, padding 3, shadow.bubble) CH 3:97; 
- Ringed avatar 40 (gradient ring a.avatarRing, 2 px padding) CH 3:84; 
- Profile button 40 (white 0.8, border #f1f5f9, p3, inner gradient a.avatarSoft, mascot 32 scaled 110%) RT/RS/ME header; 
- Companion button 36 (gradient ring 45deg #4ea8de to #fe9d7c, p2) RT 13:5, RS 3:480; 
- Prompt avatar 36 (bg #e0f2fe, 2 px border white .9 + 1 px ring rgba(186,230,253,.6)) RT 8:667; 
- Header profile 32 round with 2 px ring rgba(0,101,144,.2) DT/NV/CM; 
- Insight avatar 48 (gradient b.avatarCompanion, p2) DT 9:920; 
- Widget avatar 44 + 14 px coral notification dot with white 2 px ring and inner 6 px #773219 dot NV 9:1260; 
- Traveler avatar 48 (bg #e2e7ff, 12 px primary status dot at bottom-end) SN 9:1752; 
- Mini mascot tile 40 (rgba(200,230,255,0.4), mascot 32) SN 9:1766; 
- Voice note avatar 40 + 16 px primary mic badge at bottom-end (-4,-4) ME 9:1569.
States: none (static).

**Circular icon button**: sizes 36, 40, 44, 48. Variants:
- Glass 44 (voice header): bg rgba(255,255,255,0.75), border rgba(255,255,255,0.9) 1, blur 8, shadow.xs, icon 20. VO 3:254, 3:263.
- Glass 48 (voice dock): same, shadow.voiceChip. VO 3:238, 3:248.
- Hero mic 64: gradient a.micButton, shadow.mic, icon 28, with two ripple rings (64 at 0.3 and 80 at 0.2, #4ea8de) VO 3:241. Pulse = scale animation (state "listening").
- Input add 36 (no bg, icon 20) CH 3:162; voice 36 (bg rgba(78,168,222,0.1), icon 16) CH 3:169; send 36 (bg #4ea8de, shadow.brandBtn, icon 16) CH 3:173.
- Map FAB 44: bg rgba(255,255,255,0.95), border rgba(226,232,240,0.8), blur 12, shadow.md RT 8:650/8:654; 36 small (0.9 white, shadow.xs) RS 3:331/3:334.
- Nav quick action 48: bg rgba(255,255,255,0.9), blur 12, shadow.lg NV 9:1238 (compass rotated -35deg), 9:1242, 9:1246.
- Back button 44: bg rgba(255,255,255,0.8), chevron 20 high DT 9:1443, NV, CM.
- Voice toggle 40: bg #eaedff, icon 15 NV 9:1234.
- Hamburger button 40: three bars 21x2, radius 9999, #0f172a, gap 5 (RT 8:753, CH 3:73 etc.).
- Drawer close 36: bg rgba(255,255,255,0.8), shadow.lg, chevron-left 10 SN 9:1943.
- Play 24: #006590 with white triangle 7 (audio) ME 9:1582.
- Complete indicator 24: #c8e6ff with blue check SN 9:1849.
Pressed state not designed anywhere (assume 0.9 scale or overlay at DS discretion).

**Button** (all pill, 9999):
- Primary (A): bg #4ea8de, 12/16 Bold white, shadow.brandBtn, padding 8/py (121 wide with 14 icon) CH 3:140 ("Start Walk", radius 12 though: it is 12). 
- Primary CTA (route, A): bg #0284c7, height 44, px 20, icon 11.4x14.25 + "START" 12 ExtraBold uc tracking 0.3 white, shadow.skyBtn, radius 9999. RT 8:690.
- Primary (B): bg #006590, white label 14/20 Bold (0.14), height 48 (DT 9:963, label there 16 Medium) or 52 (CM 9:1408, full width) or padding py12 px~26 (NV 9:1299), radius 9999; shadow.primaryBtn on CM/NV.
- Primary blue (3:270): bg #4ea8de, 46 high, 210 wide, px 24, label 14/20 Bold #fefefe, icon 13.3x15.8, shadow.ctaBlue RS 3:483.
- Secondary (B): bg #eaedff (DT swap #e2e7ff), label #131b2e 14/20 Bold, height 48/52 or py12 px26 (NV 9:1294 Pause Walk) CM 9:1414, DT 9:968, NV 9:1294.
- Secondary (A outlined): bg white, border rgba(226,232,240,0.8), radius 12, label 12/16 SemiBold #334155 (CH 3:138 "Preview on Map", 123 wide).
- Outlined pill (A, RP stop preview): bg white, border #e2e8f0, px 12 py 8, 110 wide, label 11/16 Bold #0f172a (Show details, Change, Delete) RP 10:15/10:17/10:19.
- Tonal small: "Regenerate" capsule: bg rgba(255,255,255,0.9), blur 12, px 14/15 py 6/9, icon 10x14 + 12/16 Bold #0369a1 (RT) / SemiBold #131b2e (RS), shadow.md or shadowHeader.
- Text/link: "Back to Home" 12/16 SemiBold #3f484f py 8 CM 9:1419; "Remove from this walk" 16 Medium #94492e pill px 12 py 4 DT 9:974 (destructive soft); "Map" link 16 Medium #006590 + arrow 9.3 DT 9:847; "Edit order" 12/16 Bold #006590 + 9.3x11.7 icon RS 3:377; "Map view" 11/14 Bold #006590 SN 9:1784; "See All" 12 SemiBold #006590 SN 9:1826.
- Chip-button "Re-walk": bg #eaedff, px 12 py 6, label 11/14 Bold #006590 + icon 7x9 ME 9:1529.
Disabled state not designed.

**Chip (filter / quick reply)**
- Quick reply chip (A chat): bg rgba(255,255,255,0.9), border rgba(226,232,240,0.9), px 15 py 7, label 12/16 Medium #334155, shadow.xs, radius 9999, emoji prefix; horizontally scrollable row 30 high, gap about 8. CH 3:152.. Section title above: "SUGGESTED ADJUSTMENTS" 11/16.5 SemiBold #94a3b8 uc.
- Voice suggestion chip: bg rgba(255,255,255,0.75), border rgba(255,255,255,0.9), blur 8, px 15 py 7, 12/16 Medium #475569, shadow.voiceChip; quoted text. VO 3:230.
- Segmented filter chip (B): active bg #4ea8de + drop shadow 0 1 1 .05, label white SemiBold 12/16 (0.24) + count (12/18 0.12 at opacity 0.8); inactive bg #eaedff label #3f484f, count rgba(63,72,79,0.7); px 16 py 8, gap 6, row height 40, gap 12 between. Variants: text+count (All (6), Warsaw (4), Prague (2)) and icon+text (Favorites, heart 11.7x10.7). ME 9:1490-9:1505.
- Tag chip "OLD TOWN LOOP": bg #f1f5f9, border rgba(226,232,240,0.5), px 11 py 3.5, Inter Bold 10/15 uc 0.5 #64748b RT 8:704.

**Badge / tag / pill label**
- Status badge A ("Live", "Selected"): bg #f0f9ff (+ border #e0f2fe in Live), radius 6 (Live) or 9999 (Selected), px 9 py 3 (Live) / px 8 py 4, text #4ea8de 11/16.5 SemiBold or #0284c7 10/14 Bold uc 0.5. VO 3:216, RP 10:12.
- Route time tag: bg rgba(224,242,254,0.7) radius 9999 px 8 py 2, text #4ea8de 11/16.5 SemiBold CH 3:117.
- Date divider pill: bg rgba(226,232,240,0.5), blur 2, px 12 py 4, 11/16.5 SemiBold #94a3b8 uc tracking 0.55 CH 3:88.
- Live status badge (voice): glass pill px 17 py 9, dot 10 (#10b981 with 0.75 halo #34d399), text 12/16 SemiBold #334155 tracking 0.3 "Betty is listening..." VO 3:257.
- Duration tag (B): radius 6, px 8 py 2, label 11/14 SemiBold 0.44; variants: primary tint (bg rgba(0,101,144,0.1), text #006590) for stops 1 to 2; coral tint (bg rgba(254,157,124,0.2), text #94492e) for stops 3 to 4. RS 3:393.
- Info chips (B, on hero/photo): bg rgba(255,255,255,0.8/0.9), blur 6, px 10 to 12 py 4 to 6, shadow.xs, icon 11 to 13 + text; examples "UNESCO World Heritage", "Free Entry Nave", "Mazovian Gothic", "Stop #2 of 4 · ..." DT 9:896-9:913.
- Rating badge: bg #e2e7ff, px 10 py 4, star 13.3x12.7 + "4.8" DT 9:775.
- "Open Now": bg #e2e7ff px 8 py 2 text #3f484f DT 9:819.
- Walkable chip: bg #c8e6ff px 8 py 2 text 11/14 SemiBold #004c6d RS 3:375.
- Level badge: bg rgba(254,157,124,0.3) px 12 py 6 (ME) or px 8 py 2 (SN), icon 12.8/10.8, text 11/14 Bold #773219 (ME) / #94492e (SN) ME 9:1465, SN 9:1761.
- Arrival chip: bg #ffdbd0 px 10 py 2, icon 8.75x9.9, text 11/14 Bold #390c00 tracking 0.44 NV 9:1271.
- Route complete pill: gradient b.routeCompleteBadge, px 16 py 6, icon 14.4x13.7, text 12/16 Bold uc 0.3 #773219 + emoji, drop shadow CM 9:1354.
- Photo caption: bg rgba(19,27,46,0.6), blur 6, px 6 py 2, 10/15 SemiBold #faf8ff ME 9:1557.
- Complete / count pills "Friendly Sky": bg #eaedff px 8 py 4, text 11/14 Bold + chevron 7x4.3 SN 9:1907.
- Place tag row in cards: see rating (olive star + "4.9 (1.2k)" 11/16.5 SemiBold #785a00, meta in #6f7880).

**Divider**: horizontal 1 px #f1f5f9 (sheet hint, border-top), #e0f2fe (route card header), rgba(241,245,249,0.8) (transcript); vertical 1x24 #dae2fd (stats strip); 2 px rgba(218,226,253,0.6) rounded (drawer); dashed 2 px rgba(191,199,208,0.4) vertical spine (RS 3:383); connector 2x8 #bae6fd (route preview card).

**Progress bar**: track h 8 radius 9999 #e2e7ff, fill gradient b.progress, 78% width example ("85% Complete" label, NV 9:1285); labels 11/14 SemiBold #3f484f (left stop) and 11/14 Bold #006590 (right). Waveform bars: 4 px wide, radius 9999, heights 6 to 16, played #006590/#4ea8de, unplayed #bfc7d0, gap 4, row height 16 (ME 9:1585).

**Switch**: 44x24, radius 9999, on = bg #006590 with 20 px white knob at end, padding 2 (SN 9:1922). Off state not designed.

**Drag handle**: 40x6 #cbd5e1 (RT) or 48x6 #dae2fd (RS), radius 9999. Reorder affordance: 6 dots icon 7.5x12, padding 4 (RS 3:404). Home indicator 128x4 #cbd5e1 at 0.8, radius 9999 (VO 3:251, iOS only).

**Number marker**: 
- Waypoint number small (A): 20 circle, white, border #e0f2fe, text 10/16 Bold #4ea8de; variant filled #fef3c7 with #b45309 (stop 3) CH 3:121/3:133.
- Order anchor (B): 28 circle: #006590 (1,2), #fe9d7c (3), #94492e (4), numeral 11/14 Bold white (3 uses #773219 ExtraBold) RS 3:385-3:454.
- Preview badge: 28 circle bg #f0f9ff, numeral 12 ExtraBold #0284c7 (RP 10:7).
- Map pin: numbered teardrop; blue pins with white 10.9 ExtraBold numerals (RT groups); label text beside it 9.4 Bold #0f172a. In RS: 32 white circle pin with #006590 Bold 12 number (drop shadow.pin), stop 4 pin 36 coral #fe9d7c with #773219 ExtraBold number and bigger shadow; GPS dot 16 #006590 white 2 px border with 40 halo #4ea8de at 0.4.

**Rating (stars)**: star icon 10x9.5 gold #785a00 + text "4.9 (1.2k)" 11 SemiBold #785a00; or badge version above. One star only; no 5-star row exists.

**Image**: placeholder rectangle #eaedff / #dae2fd with photo; thumbnails 64x64 radius 48 (inset shadow.inset.thumb).

### 4.2 Molecules

**TopBar variants**
1. TopBar.Home (A, RT/RP/RS/ME): bg rgba(250,248,255,0.85) blur 12 shadow.headerB; row px 24 pt 16 pb 8; hamburger (40, bars #0f172a) | title "BetterTravel" LibSans Bold 20/30 -0.5 #0f172a | Profile avatar 40. RT/RP also stack a 56 high spacer header (rgba(255,255,255,0.85), border-bottom rgba(226,232,240,0.6), shadow.headerA) under it, i.e. status area.
2. TopBar.Chat (CH 3:72): bg rgba(255,255,255,0.7) blur 12, border-bottom rgba(241,245,249,0.6), px 20 pt 12 pb 13; hamburger | centre: "BetterTravel" PJS Bold 16/16 -0.4 #0f172a above row [dot 6 #10b981 + "with Betty" 12/16 SemiBold #94a3b8], gap 4 | ringed avatar 40.
3. TopBar.Detail (B: DT/NV/CM): h 64 px 16 bg rgba(250,248,255,0.85) blur 12 shadow.headerB; back button 44 | column [eyebrow "BETTERTRAVEL" 11/14 SemiBold 0.55 #006590; title 18/24 Bold -0.18 #131b2e max 200 ellipsis] | profile photo 32 ring.
4. TopBar.Voice (VO 3:253): transparent over gradient, px 24 pt 48 pb 12; glass 44 button (close) | LiveStatusBadge | glass 44 button (settings).
5. Map overlay capsules (RT 8:635): at top 12, left/right 16: left "Route Overview Badge" glass pill (bg 0.9 white, blur 12, border rgba(226,232,240,0.6), px 15 py 9, dot 10 #0ea5e9 + "Optimal Loop" 12/16 Bold #1e293b + "• 3.2 km" Inter Medium 12/16 #64748b); right "Regenerate" button. RS version at top 16: dot #4ea8de, "Optimal Loop" 12/16 SemiBold 0.3 #131b2e, "·" #bfc7d0, "3.4 km" 11/14 Bold #006590 tracking 0.44; and button.

**TabBar variants** (floating pill, bottom, 3 items; fixed at bottom 0 with 8 padding):
1. TabBar.Dock (A, CH 3:178): radius 32, bg rgba(255,255,255,0.95), blur 20, border rgba(241,245,249,0.8), px 25 py 11, shadow.dockA, margin x 8 inside 16 container. Items (space-between): icon 20 + gap 4 + label 10/15. Active "Discover": icon blue (#4ea8de) label Bold #4ea8de; inactive label SemiBold #475569. Tabs: Discover, Route, Memories. (3:2 version per brief: icons 22, label 11.5/17.25 Bold, #334155 inactive.)
2. TabBar.PillRoute (A, RT 8:723, RP 9:1167): height 64, max 448, radius 9999, bg rgba(255,255,255,0.95), blur 20, border rgba(226,232,240,0.8), px 13; shadow.tabBarA. Item: h 48, min w 56. Inactive: icon 19 + label Medium 10/15 #94a3b8, 2 px gap. Active: icon 21.7x22.3, label ExtraBold 10.5/15.75 #0284c7, plus 6 px dot #0284c7 below (shadow.xs). Items: Discover, Route (active), Memories. Task spec mentions "Drawer" in layer name; no 4th item drawn.
3. TabBar.PillM3 (B, RS 3:506, ME 9:1686): height 64, max 448, radius 9999, bg rgba(255,255,255,0.9), blur 20, px 16, space-between; shadow.tabBarB. Item h 48 min w 56, icon 20 + 2 px gap + label 11/14 Bold 0.44; inactive #3f484f, active #006590 (icon recolour). Items: Explore, Route, Memories. Active "Route" in RS, "Memories" in ME.

**InputPill (chat)** CH 3:161: radius 9999, bg rgba(255,255,255,0.9), blur 12, border white 1, p 7, shadow.input, gap 6; [add 36] [text field flex, px 4 py 8/9, PJS Regular 12 #94a3b8 placeholder "Ask Nimbus anything or adjust route..." (14.5 in 3:2)] [voice 36 tinted] [send 36 filled]. 

**Message bubbles (A)**
- UserBubble CH 3:91: gradient a.userBubble, radius 24 24 24 2 (top-right 2), px left 16, py 16, right pad about 38 (shrink to content), max w ~322, text 14/22.75 Regular white; aligned end with 32 start pad; timestamp "2:14 PM" 10/15 Medium #94a3b8 under, pr 8, pt 4. shadow.userBubble.
- AssistantBubble CH 3:100: glass bg rgba(255,255,255,0.85), blur 6, border rgba(255,255,255,0.8), p 17, radius 24 2 24 24 (top-left 2), gap 14, shadow.bubble; text 14/22.75 Regular #334155 with strong runs SemiBold #0f172a; sits next to MascotAvatar 32 (gap 10); timestamp below. Contains EmbeddedRouteCard.
- DateDivider (atom pill above).
- TranscriptEntry (VO 3:219, 3:223): speaker label 12/16 ("You" SemiBold #94a3b8; "Nimbus" Bold #4ea8de) over text 14/22.75 (user Medium #64748b; assistant SemiBold #0f172a), gap 1.4, entries gap 10.

**EmbeddedRouteCard** CH 3:108: radius 16, gradient a.routePreviewCard, border rgba(224,242,254,0.8), p 15. Header row (border-bottom #e0f2fe, pb 11): icon tile 22 (bg #e0f2fe radius 6 p 4, icon 14) + title "Old Town & Courtyards" 12/16 Bold #1e293b | time tag. Then waypoint list (3 rows 20 high step 44): number marker 20 + name 12/16 (Medium #475569; last SemiBold #0f172a) connected by 2x8 #bae6fd; then CTA row pt 4 gap 8: secondary "Preview on Map" + primary "Start Walk".

**StatTile (B details quick stat)** DT 9:784: bg #f2f3ff radius 16, column centered, px 12, py 24 (first) or p 12 (second; inconsistent), icon 13 to 17 + value (16/24 Medium #131b2e) + caption (16/24 #6f7880, "Ideal visit", "From Castle Sq"); two tiles flex 1 gap 8.
**StatTile.Metric (CM)** 9:1365: bg #f2f3ff radius 16 px 4 py 16 + drop shadow.xs; 32 circle icon (bg #eaedff, icon 12 to 16) + value 18/24 Bold -0.18 #131b2e + caption 11/14 SemiBold 0.44 uc #3f484f ("KM WALK", "ACTIVE TIME", "SIGHTS SEEN"). Three equal columns gap 8, container px 24.
**StatsStrip.Sheet (RS 3:341)**: bg rgba(242,243,255,0.8) radius 16 p 8, three equal cells separated by 1x24 #dae2fd dividers; each: 28 circle icon tile (tinted: rgba(0,101,144,0.1) / rgba(254,157,124,0.2) / rgba(255,223,155,0.6)) + value 18/18 Bold -0.18 #131b2e + caption 10/15 SemiBold #3f484f ("Total time", "Walking", "Curated"); gap 8.
**StatsStrip.Drawer (SN 9:1769)**: bg #f2f3ff p 12 three cells equal, text center, value 18/24 SemiBold -0.18, caption 11/14 Bold 0.44 #3f484f; middle cell tinted rgba(218,226,253,0.3). Cities / Routes / Walked.
**SummaryBanner (ME 9:1470)**: bg #f2f3ff radius 32 p 16 shadow.xs, glow disc 112 (rgba(254,157,124,0.2) blur 20, bottom -32 right -24); 48 circle icon (bg rgba(78,168,222,0.2)) + title 18/24 SemiBold -0.18 "18 Moments Captured" + caption 12/18 Inter "Across 2 countries • 42.6 km walked".
**QuickMetricsStrip (ME 9:1534)**: bg #f2f3ff, px 12 py 6, row gap 16: icon 10.5 to 11.7 + "3.4 km" 12/16 SemiBold 0.24 #3f484f, separators "•" at 0.3 opacity.

**InfoRow (details)** DT 9:811: bg #f2f3ff radius 16 p 14 gap 12; 36 circle icon (bg #e2e7ff) + column: title row [title 16/24 Medium #131b2e | trailing (badge "Open Now" / price 16 Bold #006590 / "Map" link)], then 16/24 Inter #3f484f line and #6f7880 note. Variants: opening hours, admission, address.
**HighlightRow (details)** DT 9:862: bg #f2f3ff radius 16 p 12 gap 12 centre; 64 thumbnail (bg #dae2fd, radius 48) + title 16 Medium #131b2e + 2-line description #3f484f + chevron 9.5x9.
**ListRow.Setting (SN 9:1900)**: 32 icon circle (bg #e2e7ff) + title 12/16 SemiBold 0.24 #131b2e (+ optional subtitle 11/14 Bold #3f484f) | trailing: dropdown chip / switch / value "EN • Metric" (11/14 Bold #6f7880); py 4, gap 10.
**ListRow.Route (SN 9:1829)**: bg #f2f3ff p 10 gap 12 (radius 0 as drawn): mini map 48x48 (bg #e2e7ff, mini route SVG p 4) + info column [title 14/20 Bold 0.14 #131b2e; "Warsaw • Feb 12" Inter 12/18 #3f484f; stats row 11/14 Bold: distance (#006590 or #94492e), dot, "5/5 Sights"] + trailing complete indicator (24 circle #c8e6ff) or chevron 6x10.
**CityCard (SN 9:1787)**: 128x about 86 (peek card 96 wide at 0.8 opacity), p 12, name 18/24 SemiBold -0.18 + dot (selected) or city icon, then "4 Routes" and "16 Sights logged" 11/14 Bold; selected bg rgba(200,230,255,0.3) + coral blur disc, others #f2f3ff; horizontal scroll, gap 10; radius 0 as drawn.
**SectionHeader**: (a) with icon: icon 16.7 to 18.3 + 16/24 Medium #131b2e (DT 9:806, 9:857); (b) label-with-action: uc label 12/16 SemiBold 0.6 #3f484f | action 11/14 Bold or 12 SemiBold #006590 (SN); (c) title + chip + action: "Today's Sequence" 18/24 ExtraBold + Walkable chip | "Edit order" (RS 3:371); (d) memories header "BetterTravel Memories" 28/36 + subtitle 12/18 + level badge (ME 9:1459); (e) transcript header: icon 14 + "REAL-TIME TRANSCRIPT" 11/16.5 Bold #94a3b8 tracking 0.55 uc + "Live" badge, border-bottom (VO 3:210).
**InsightCard (DT 9:918)** "Betty secret": bg #f2f3ff radius 16 p 16 shadow.xs; avatar 48 (gradient ring) + column: eyebrow row ["Betty SECRET" 16/24 Medium uc 0.8 #006590 (figma wrapped on two lines, treat as "BETTY SECRET"), 6 px coral dot, "Historical Flash" Inter #6f7880] and quote 16 Inter / 22 #131b2e. Persona name varies (see section 0).
**VoiceNoteSnippet (ME 9:1567)**: bg #f2f3ff p 8 gap 8; avatar 40 + mic badge; column: "Cirrus Voice Memory" 11/14 Bold 0.44 #006590 | "0:24" Liberation Mono 12/18; italic Inter 12/18 quote; PlayRow (24 play + waveform).
**JournalNote (ME 9:1647)**: bg #f2f3ff p 8 gap 8; 32 circle (bg rgba(254,157,124,0.3)) with pencil icon + "Traveler Reflection" 11/14 SemiBold 0.44 #131b2e + body 12/18 Inter #3f484f.
**PhotoMosaic (ME 9:1554)**: three equal tiles 103.3 high, gap 8, each with bottom-start caption chip (6 from edges), last tile "+5 more". **Single highlight photo (ME 9:1636)**: height 176, bottom gradient, caption: icon 12.8 + "Golden Hour Walk" 11/14 Bold #faf8ff and right "17:42 Sunset" Inter 12/18 at 0.9.
**CompanionPromptCard (RT 8:659, RS 3:477)**: glass pill with text + mascot avatar. RT: bg rgba(255,255,255,0.95), border rgba(226,232,240,0.8), blur 12, pl 15 pr 9 py 9, gap 10, shadow.lg; lines "Warsaw secret?" 11.5/14.38 Bold #1e293b / "Ask Nimbus" 10/15 SemiBold #0284c7 + dot 6 #38bdf8; avatar 36. RS: bg rgba(242,243,255,0.9), blur 12, 48 high, pl 14 pr 8, shadow 0 8 20 rgba(78,168,222,0.15); "Need changes? Ask me!" 12/16 SemiBold #131b2e (+ Bold #006590 run) + 36 gradient avatar. Placement: floating at bottom-end above the sheet.
**AudioGuideWidget (NV 9:1252)**: pill bg 0.95 white blur 20 shadow.xl, max w 280, pl 12 pr 8 py 6, gap 10; text right-aligned ["Audio guide ready!" 11/14 Bold #94492e, "Tap to listen together" Inter 12/18 #3f484f] + avatar 44 with notification dot.
**TurnGuidanceCard (NV 9:1214)**: glass card bg rgba(255,255,255,0.9) blur 20 radius 32 p 16 gap 16 shadow.xl; 56 circle #006590 turn glyph (icon 18.7, shadow.md) + column [eyebrow row "IN 80 METERS" 11/14 Bold uc 0.55 #94492e + dot 6 #fe9d7c + "Walking Mode" 11/14 SemiBold 0.44 #3f484f; instruction 18/24 Bold -0.18 #131b2e ellipsis; "Next: <b>St. John's Archcathedral</b>" with 12 icon, Inter 12/18 #3f484f, name SemiBold #131b2e] + voice toggle 40.
**DestinationSheetCard (NV 9:1266)**: bg rgba(255,255,255,0.95) blur 20 radius 32 p 16 gap 16 shadow.2xl: header [arrival chip + "Step 3 of 6" Inter Medium 12/18; title 22/28 Bold -0.33; "3 min • 180 m remaining" with icon 13.3; thumbnail 64 (radius 32, bg #eaedff)], progress bar block, CTA row (Pause Walk secondary + View Guide primary, gap 8, pt 4).
**StopPreviewCard (RP 10:4)**: floating at top 72 left 16 width 358, bg rgba(255,255,255,0.95) blur 12 border rgba(226,232,240,0.8) radius 20 p 12 gap 10 shadow 0 -8 30 rgba(0,0,0,0.08); header: [Badge 28 circle number + text stack (title 14/20 Bold #0f172a, subtitle 11/16 Medium #64748b, "Stop 2 of 4 ..." assumed) | Status badge "SELECTED"]; Buttons row of three outlined pills (Show details / Change / Delete, 110 wide each, gap 8; Delete is not coloured red in Figma).
**WaypointCard (RS 3:384)**: white, radius 16, p 12, gap 12, drop shadow.waypointCard; [order anchor 28] [thumbnail 64 radius 48] [details: title 14/22 Bold #131b2e + duration tag right; description Inter 12/18 #3f484f ellipsis; meta row: star 10x9.5 + "4.9 (1.2k)" 11 SemiBold #785a00 + "· Departure 10:00 AM" 11 SemiBold #6f7880] [reorder handle 7.5x12]; cards stacked gap 12 with dashed spine at x 24; one-line description variants.
**MemoryCard (ME 9:1514, 9:1596)**: bg white radius 32 p 16 gap 16 shadow.md; Top meta row: [calendar 9x10 + date 11/14 Bold 0.44 #3f484f • location 11/14 SemiBold #006590 (card 2 uses #94492e)] with title 18/24 SemiBold -0.18 #131b2e below (pt 2), Re-walk chip right; QuickMetricsStrip; media (PhotoMosaic or single highlight); VoiceNoteSnippet or JournalNote. Feed gap 24, px 16.
**CompletionCard (CM 9:1341)**: bg rgba(255,255,255,0.95) blur 20, radius 24, width full (px 16), height 760, overlapping map by 128, shadow.card.completion; contents: warm aura 256 blur 20, top badge pill at 24, mascot 160 (box 176) at 52 with sparkle disc 28 (#ffdf9b, TR -4,-4, icon 16.5) and heart disc 24 (#ffdbd0, BL -8, icon 11.7), heading 22/28 ExtraBold -0.55 centred, subtitle Inter 14/22 centred with "Cirrus" SemiBold #006590, metrics 3-grid (top 334, px 24), XP banner (top 464), CTA stack (top 560, gap 8), confetti overlay image on top.
**XpBanner (CM 9:1393)**: bg rgba(226,231,255,0.7) radius 16 p 16 gap 16 shadow.xs; medal circle 48 (gradient b.xpBadge, icon 10.8x21.7) + [row "+150 XP EARNED" 11/14 Bold uc 0.55 #94492e, dot 6 #94492e, "Level 4" 11/14 Bold 0.44 #3f484f; title "‘Old Town Explorer’ Unlocked!" 14/20 Bold 0.14 #131b2e].
**RouteSummaryRow (RT 8:672)**: title "Today's Warsaw Route" 16/24 Bold -0.4 #0f172a + dot 8 #f59e0b; stats row Inter 12/16: "3.2 km" SemiBold #0284c7, "•" #cbd5e1, "2h 10m walk" Medium #64748b, "•", "4 historic stops" #64748b; trailing Primary CTA START.
**ExpandHint (RT 8:696)**: border-top #f1f5f9, pt 13: icon tile 24 (bg #f0f9ff, chevron 4.7x9.8) + "Tap or swipe up for 4 detailed stops" 11/16.5 SemiBold 0.275 #475569 | tag chip OLD TOWN LOOP.
**DrawerProfileHeader (SN 9:1749)**: avatar 48 + name 18/24 SemiBold -0.18 + level badge | mini mascot tile 40.
**DrawerFooter (SN 9:1934)**: bg #f2f3ff px 12 pt 12 pb 8: help icon 15 + "Support & Nimbus Tips" 12/16 SemiBold 0.24 #3f484f | "v2.4" 11/14 Bold #006590.
**Mascot Hero (VO 3:268)**: mascot 240 (max 340) with halo 256 radial glow behind (blur 14) and larger ambient halo 320 (blur 18); shadow 0 1 1 rgba(0,0,0,.05). Other sizes: 192/160 (SN home), 176/160 (CM).

### 4.3 Organisms

1. **BottomSheet.Collapsed (RT 8:670)**: anchored above the tab bar (bottom 68), full width, bg rgba(255,255,255,0.95) blur 12, border-top rgba(226,232,240,0.8), radius 32 32 0 0, px 20 pt 21 pb 12, gap 12 centre, shadow.sheetA. Content: DragHandle (40x6 #cbd5e1) → RouteSummaryRow (with START) → ExpandHint. Expanded behaviour implied ("Tap or swipe up").
2. **BottomSheet.Itinerary (RS 3:338)**: bg rgba(255,255,255,0.95) blur 20, radius 32 32 0 0, px 16 pt 12 pb 24, gap 16, shadow.sheetB, overlapping map by 24 (top -24). Content: DragHandle (48x6 #dae2fd) → StatsStrip.Sheet → SectionHeader "Today's Sequence" + Walkable chip + Edit order → waypoint list (4 WaypointCards, dashed spine) → Primary CTA centred ("Start Route Navigation", 210 wide; Figma shows it floating) → CompanionPromptCard floating bottom-end (RS 3:477 at 736).
3. **MapOverlay.RouteControls**: capsule row (Route overview badge + Regenerate) top; FAB column (Locate me, Toggle layers, 44, gap 10) bottom-start (bottom 89 start 16); CompanionPrompt (RT) bottom-end (bottom 48 end 16) / mascot button 48 bg rgba(242,243,255,0.9) (RT 13:2 at 321,574; ring 36) when prompt collapsed; RS uses two 36 buttons (Center map, 3D tilt) bottom-start gap 6, bottom 24.
4. **MapOverlay.Navigate (NV)**: TurnGuidanceCard at top (pt 4 within 16 padding), right quick-action dock (3 x 48, gap 8, top 128 end 16), AudioGuideWidget aligned end above DestinationSheetCard at bottom. Map canvas is raster (512 placeholder) with tint gradient b.mapTint and route overlay SVG.
5. **ChatThread (CH 3:86)**: scroll column px 16 pt 28 pb 24 gap 20 over a stack of ambient glows; items: DateDivider → UserBubble → AssistantMessage(avatar + bubble + EmbeddedRouteCard + timestamp) → QuickReplyChips (title + horizontal row).
6. **ChatComposerDock (CH 3:160)**: floating bottom: pb 16 pt 8 px 16 gap 12 → InputPill → TabBar.Dock (margin x 8).
7. **VoiceControlDock (VO 3:236)**: bg rgba(255,255,255,0.82) blur 12 border rgba(255,255,255,0.85) radius 9999 px 21 py 13, max w 384, space-between, shadow 0 10 15 -3 rgba(0,0,0,.1) + 0 4 6 -4; [glass 48 keyboard/book] [hero mic 64 w/ ripple] [glass 48 end session]. Above it: horizontally centred VoiceSuggestionChips row (38 high); below: iOS home indicator.
8. **TranscriptCard (VO 3:208)**: bg rgba(255,255,255,0.82) blur 12 border rgba(255,255,255,0.85) radius 24 p 21 gap 8, max w 384, shadow.transcript; Header (section header e) → TranscriptEntry (user) → TranscriptEntry (assistant).
9. **VoiceStage (VO)**: gradient background a.voiceBg, TopBar.Voice, Mascot hero (centre, y 104 to 668), TranscriptCard (pt 24), VoiceControlDock (bottom, y 668, px 24 pb 36).
10. **DetailsHero (DT 9:892)**: h 320, radius 0 0 24 24, shadow.xs, image 389 high cropped, scrim gradient, pills row at top 75 (stop pill left, hero action button 36 right), bottom badges stack (3 info chips in 2 rows: one at 20 above centre, two at 20 below centre, within 72 high box at bottom 16).
11. **BottomActionDock (DT 9:960)**: bg rgba(250,248,255,0.9) blur 12, px 16 pt 12, max w 448, bottom 36; row [Primary "Keep in Route" 48 high flex + Secondary "Swap" 48 px 16] gap 10, then centred text button "Remove from this walk" #94492e. (The 36 bottom offset leaves room for the system bar.)
12. **SideNavDrawer (SN 9:1745)**: left drawer width 319.8 (about 82%), full height 832 to 884, bg rgba(255,255,255,0.95) blur 20, radius 0 32 32 0, shadow.2xl; right scrim rgba(19,27,46,0.4) blur 6 (70.2 wide) with 36 close button (rgba(255,255,255,0.8), chevron-left) vertically centred; content padding 16, gap 16: DrawerProfileHeader → StatsStrip.Drawer (pt 12) → EXPLORED CITIES section (header + horizontal CityCards h 90 incl. 4 padding) → VISITED ROUTES (header + 3 route rows gap 8) → divider (2 px rounded) → settings rows (Nimbus Voice Guide with dropdown chip "Friendly Sky", Offline Maps with switch, Language & Units) gap 8 → DrawerFooter pinned at y 723. Behind it, the underlying home screen is blurred 1 at opacity 0.4.
13. **MemoriesFeed (ME 9:1512)**: header block (title + level badge + SummaryBanner + segmented filter carousel) then MemoryCards gap 24, px 16, bottom 112 inset for the tab bar.
14. **CompletionStage (CM)**: map backdrop 288 high (radius 0 0 24 24, scrim gradient, top banner pill "Old Town Loop Complete / 100% finished": bg rgba(255,255,255,0.85) blur 6 px 16 py 8, left icon 15 + 12/16 SemiBold 0.24 #006590, right 11/14 SemiBold 0.44 #3f484f) then CompletionCard overlapping.

## 5. Per-screen layout outlines

**3:2 Clean Discovery Landing** (not re-read): per brief: gradient page bg, big gradient-text heading (34/38), app title bar, pill input, floating bottom tab bar (Discover, Route, Memories), Betty mascot.

**3:70 AI Conversation State (CH)**: bg #f8fafc + two ambient glows → TopBar.Chat → ChatThread: DateDivider ("TODAY") → UserBubble + timestamp → AssistantMessage [MascotAvatar 32 + AssistantBubble(text + EmbeddedRouteCard(header, 3 waypoint rows, Preview on Map + Start Walk)) + timestamp] → "SUGGESTED ADJUSTMENTS" + QuickReplyChips (3) → ChatComposerDock (InputPill + TabBar.Dock, Discover active).

**3:199 Live Voice Mode (VO)**: background gradient → TopBar.Voice (close, "Betty is listening..." badge, audio settings) → Mascot hero with halos → TranscriptCard (header, user line, assistant line) → VoiceSuggestionChips (3 quoted phrases) → VoiceControlDock (keyboard, hero mic, end) → home indicator.

**8:549 RouteScreen (RT)**: full-bleed vector map (#f6f8fb bg, 772 high) → map overlays (capsule row top, FAB column bottom-start, CompanionPromptCard bottom-end, mascot mini button) → BottomSheet.Collapsed above tab bar → TopBar.Home (+ 56 status header) → TabBar.PillRoute (Route active).

**9:995 RouteScreenPointSelected (RP)**: same as RT plus StopPreviewCard floating at top 72 (selected pin 2 in the sample; pin selection highlight not visible in the code, verify from screenshot) and the mini mascot button moved to (336,603).

**3:270 RouteBottomSheet (RS)**: TopBar.Home (z top) → map canvas 359 high (stylised vector map, route polyline gradient, numbered pins 1 to 4, GPS dot) with capsule row top (Optimal Loop 3.4 km + Regenerate) and two 36 map buttons bottom-start → BottomSheet.Itinerary (handle, StatsStrip, Today's Sequence header, 4 WaypointCards, Start Route Navigation) → CompanionPromptCard floating → TabBar.PillM3 (Route active).

**9:764 DetailsScreen (DT)**: DetailsHero (image, pills, badges) → TopBar.Detail overlay → title block: [place name + native name (Inter Italic) | rating badge + "1,420 reviews"] + 2 StatTiles (25 min Ideal visit, 160 m From Castle Sq) → InsightCard (Betty secret, at y 570) → SectionHeader "Visiting Essentials" + 3 InfoRows (hours, admission, address) → SectionHeader "Highlights & What to See" + 3 HighlightRows → "Surrounding Walk" map snapshot card (title + subtitle, 128 map with radius 48, overlay chip "Next stop: ...") → BottomActionDock.

**9:1194 NavigateScreen (NV)**: TopBar.Detail → full-bleed map canvas (740 high, tinted) → TurnGuidanceCard → right quick-action dock (compass, recalibrate, AR) → AudioGuideWidget → DestinationSheetCard (header, ProgressBar, Pause Walk + View Guide).

**9:1325 CompletionRouteScreen (CM)**: TopBar.Detail → map backdrop 288 with banner pill → CompletionCard (badge pill, mascot with accent discs, heading, subtitle, 3 metric StatTiles, XpBanner, Save to Memories primary, Share secondary, Back to Home text).

**9:1455 MemoriesScreen (ME)**: TopBar.Home → title "BetterTravel Memories" + Level badge + subtitle → SummaryBanner → Segmented filter carousel (All, Warsaw, Prague, Favorites) → MemoryCard 1 (meta, title, Re-walk, QuickMetricsStrip, PhotoMosaic (3), VoiceNoteSnippet) → MemoryCard 2 (meta, title, Re-walk, strip, single highlight photo with caption, JournalNote) → TabBar.PillM3 (Memories active).

**9:1724 SideNavigationBar (SN)**: blurred underlying home (header + greeting "Let's plan your trip Together" + mascot) → SideNavDrawer (profile header, stats strip, explored cities scroller, visited routes list, settings rows, footer) + right scrim with close button.

## 6. Open questions

1. Which palette (A or B) is the source of truth, or should they be unified (recommendation: treat B as the app system and A's brand blue #4ea8de as accent, but this needs design confirmation)?
2. Mascot name (Betty, Nimbus, Cirrus) and the Details insight label ("BETTY SECRET" in the generated text).
3. Whether 'Liberation Sans' and 'Roboto' (3:2) are real design fonts or Figma fallbacks; the other frames use Plus Jakarta Sans and Inter, which must be bundled (fonts are not Figma assets).
4. The 16 px text in DT (9:764) and the 0 radii in SN (9:1724) look like generator/design gaps.
5. Pressed, disabled, focus, error and toggle-off states are not designed anywhere; only active/inactive (tab, filter chip), selected (city card, stop preview), played/unplayed (waveform), and on (switch) exist.
6. Tab label sets differ (Discover vs Explore); decide the nav destinations (Discover, Route, Memories) and whether the side drawer is the fourth destination.
7. Map is vector/raster art in Figma; the app needs a real map SDK and pin/polyline styling taken from section 4.1 (pin) and the polyline gradient (#4ea8de to #fe9d7c) in the asset set.
8. Photos are placeholder content; mascot variants with white backgrounds (JPEG) should be replaced by the transparent PNG.
