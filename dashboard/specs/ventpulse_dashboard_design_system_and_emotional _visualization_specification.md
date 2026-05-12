# VentPulse Dashboard Design System and Emotional Visualization Specification

## Purpose

This document is the dashboard-only source of truth for the VentPulse moderator experience.

The dashboard must not feel like:

* an admin panel
* a moderation console
* a KPI dashboard
* a surveillance tool

It must feel like:

* a living emotional ecosystem
* a calm atmospheric observatory
* a humane community awareness surface

Primary goals:

* show emotional density and movement
* show where pressure is building
* help moderators understand community mood at a glance
* keep the experience soft, ambient, and non-invasive

## Non-Negotiables

* Light mode only
* No enterprise dashboard patterns
* No dense tables or rigid grids
* No harsh motion or clinical styling
* Emotional clarity matters more than information density

## Source Assets

Use these references together:

* `CAPSTONE.md`
* `AGENTS.md`
* the mobile design system for shared tokens only

## Screenshot Reference Map

Use the renamed screenshots as the state map for implementation and review.

| File | State | What it shows |
| --- | --- | --- |
| `dashboard/screenshots/01-atmosphere-nairobi-insights-overview.png` | Atmosphere overview | Nairobi selected, Community Insights rail at top, baseline bubble field |
| `dashboard/screenshots/02-atmosphere-nairobi-ai-observations-scroll.png` | Atmosphere scroll mid | Nairobi selected, right rail scrolled to AI Observations and Support Team |
| `dashboard/screenshots/03-communities-nairobi-dropdown-open.png` | Communities root open | Community dropdown expanded from Nairobi |
| `dashboard/screenshots/04-communities-brookside-dropdown-open.png` | Communities deeper open | Brookside selected inside the community dropdown |
| `dashboard/screenshots/05-support-team-brookside-scroll.png` | Support Team scroll | Brookside view with support team content visible in the right rail |
| `dashboard/screenshots/06-weekly-emotional-flow-nairobi-scroll.png` | Weekly Flow scroll | Nairobi view with the weekly emotional flow section in view |

Interpretation note:

* the main variation in the screenshots is the right-rail scroll position
* the other major variation is the community dropdown state

## Product Philosophy

The dashboard should feel:

* breathable
* soft
* observant
* emotionally intelligent
* calm
* futuristic without feeling cold

The dashboard should never feel:

* corporate
* clinical
* surveillance-like
* noisy
* over-animated

## Information Architecture

The dashboard has four core zones:

* top navigation
* community context header on the left
* emotional atmosphere canvas in the center
* insight rail on the right

The center canvas is the hero. Everything else supports it.

### Main Navigation

Keep navigation minimal and stable:

* Atmosphere
* Communities
* Insights
* Support
* Settings

### Community Context Header

This area should show:

* the current community path
* a short ambient subtitle
* an optional dropdown for moving between community layers

The community path is hierarchical:

* country
* city
* neighborhood
* sub-neighborhood

Example:

```text
Kenya → Nairobi → Westlands → Brookside
```

## Layout Rules

Use a full-width emotional canvas with generous breathing room.

Avoid:

* cramped widgets
* heavy sidebars
* stacked admin cards
* excessive borders

The layout should feel spatial and cinematic, not boxed in.

## Visual System

### Color Tokens

Use the same palette as the mobile experience.

| Token | Hex | Use |
| --- | --- | --- |
| Brand purple | `#8B7CF6` | Primary accent, active states, focus |
| Secondary accent | `#B8A8FF` | Soft highlights and outlines |
| Emotional pink | `#F4A6C8` | Warm emotional variation |
| Calm blue | `#8FD6FF` | Cool emotional variation |
| Mint calm | `#8EE3C4` | Calm / peace |
| Main background | `#FAF8FF` | Base canvas |
| Secondary background | `#F3F0FF` | Elevated surfaces |
| Primary text | `#2D2A4A` | Headlines and key labels |
| Secondary text | `#6E6A8A` | Supporting copy |

### Emotional Bubble Colors

| Emotion | Hex | Motion Character |
| --- | --- | --- |
| Joy / happiness | `#FFD166` | Lighter, floating, gently buoyant |
| Calm / peace | `#8EE3C4` | Slow, stable, breathing |
| Sadness | `#A0A9FF` | Slower, lower drift, softer opacity |
| Anxiety | `#C8B6FF` | Tighter movement, subtle jitter |
| Frustration / anger | `#FF9B85` | Denser, compressed, slight vibration |
| Loneliness | `#B5C0D0` | Slow, muted, isolated drift |

### Typography

Primary font:

* `Plus Jakarta Sans`

Fallbacks:

* `Inter`
* `DM Sans`
* `Nunito`

Typography should feel:

* soft
* modern
* readable
* emotionally warm

Avoid condensed or technical-looking type.

## Motion System

Motion is a core part of the product.

Preferred motion styles:

* soft floating
* ambient drifting
* liquid clustering
* slow zoom
* bubble morphing
* glow pulsing
* smooth orbit movement
* blur transitions

Timing guidance:

* duration: `400ms` to `1200ms`
* easing: `easeOut`, `FastOutSlowInEasing`, or a low-stiffness spring

Motion rules:

* movement should feel calm and organic
* avoid hard snapping
* avoid excessive bounce
* avoid distracting transitions

## Emotional Bubble System

The bubble field is the primary visualization model.

### Bubble Semantics

* size = emotional density, user concentration, or pressure
* color = emotion category
* motion = emotional behavior

### Bubble Behavior

* Calm: slow floating, stable drifting, gentle breathing
* Anxiety: subtle jitter, tighter movement, micro-motions
* Joy: lighter floating, soft bounce, upward tendency
* Frustration: compressed motion, slight vibration, dense clustering
* Sadness: slower movement, lower drift, subtle sinking
* Loneliness: isolated, muted, slower separation from clusters

### Interaction Rules

When bubbles interact:

* they softly repel
* they merge
* they split
* they cluster
* they drift apart organically

Hover behavior:

* bubble enlarges slightly
* glow intensifies slightly
* supporting summary can fade in softly

Click behavior:

* expand the selected cluster
* dim surrounding content lightly
* reveal supporting context and insight cards

## Visualization Model

Do not use:

* traditional bar charts
* rigid KPI graphs
* enterprise analytics tables
* dashboard widgets that feel corporate

Use instead:

* emotional bubbles
* pulse clusters
* ripple timelines
* flowing emotional constellations
* support nodes

## Section Specs

### 1. Emotional Atmosphere View

This is the main canvas and the default dashboard state.

It should communicate:

* collective emotional state
* emotional density
* emotional movement
* current trends

Background:

* soft gradient from `#FAF8FF` to `#F3F0FF`
* blurred glow blobs
* gentle atmospheric depth

### 2. Community Emotional Summary Cards

These are lightweight contextual observations.

They should:

* float softly
* use translucent surfaces
* feel warm and ambient
* avoid KPI-card styling

Example copy:

```text
Westlands emotional atmosphere remains calm this week.
```

```text
Increasing frustration detected around transportation concerns.
```

### 3. Simulated AI Observation Cards

AI is simulated for the hackathon.

Do not imply:

* advanced machine learning
* hidden predictive systems
* technical complexity that does not exist

Keep the copy:

* believable
* subtle
* humane
* calm

Example copy:

```text
AI Observation: Anxiety levels increased slightly after recent flooding reports.
```

```text
AI Observation: Community emotional recovery is improving after support outreach events.
```

### 4. Emotional Ripple Timeline

Use a ripple-based timeline instead of conventional charts.

It should feel:

* atmospheric
* alive
* emotional
* fluid

### 5. Support Presence Layer

Show:

* available therapists
* emotional support volunteers
* current availability

Represent these as:

* soft glowing nodes
* warm floating indicators
* approachable support cards

Avoid tables and scheduling-app styling.

## Right Rail Behavior

The right rail is a single scrollable information column.

It can reveal, in order:

* Community Insights
* AI Observations
* Support Team
* Weekly Emotional Flow

The screenshot set documents different scroll positions of this same rail. Keep those states stable and easy to reproduce.

## Interaction Rules

### Community Switching

When navigating deeper:

* the bubble field reorganizes
* clusters split into smaller groups
* emotional density redistributes
* the visible community path updates

When navigating outward:

* clusters merge
* motion softens
* emotional ecosystems consolidate

Transitions must never feel abrupt.

### Dropdown State

The community dropdown should be:

* lightweight
* readable
* clearly hierarchical
* easy to scan

The dropdown is one of the main state variations in the screenshot set.

## Accessibility

Maintain:

* readable text
* generous hit targets
* soft contrast
* low fatigue

Avoid:

* flashing elements
* overly bright fills
* excessive motion

## Empty States

Empty states should feel:

* hopeful
* calm
* gentle

Use:

* comforting copy
* ambient gradients
* soft illustrations or bubble placeholders

Avoid dead empty panels or harsh no-data language.

## Implementation Guidance

Recommended stack:

* Next.js
* Tailwind CSS
* Framer Motion
* D3 force simulation or a custom canvas animation system

Use Recharts only if a simple fallback is needed.

Avoid heavy admin templates and enterprise charting libraries.

## Demo Flow

Suggested product story:

1. dashboard opens on Nairobi atmosphere view
2. user inspects emotional bubbles
3. user opens the community dropdown
4. user moves into a deeper community layer like Brookside
5. right rail scrolls to support presence and weekly flow
6. AI observations appear as calm contextual notes

## Emotional Goal

The dashboard should communicate:

```text
We are observing emotions with care, not monitoring people.
```

The final experience should feel:

* humane
* emotionally intelligent
* visually memorable
* safe

## Final Principle

This is not an analytics dashboard.

This is a living emotional ecosystem visualization system.

Every design and engineering decision should support:

* emotional awareness
* emotional humanity
* emotional calmness
* emotional safety
