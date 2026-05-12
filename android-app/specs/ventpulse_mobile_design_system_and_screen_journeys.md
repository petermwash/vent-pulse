# VentPulse - Mobile Design System & UX Journey Specification

## Purpose

This document is the implementation guide for the VentPulse Android app UI.

Use it to plan, design, and build the app so the result matches the screenshots in the `screenshots/` folder as closely as possible.

The screenshots are the visual source of truth.

The design system in this document defines the rules, but the screenshots define the exact look, spacing, scale, emoji treatment, and emotional tone.

This file is written for Codex and future contributors.

---

## How To Use This File

When planning or building VentPulse, follow this order:

1. Inspect the screenshots in `screenshots/`.
2. Match the screen layout, emoji style, spacing, and component proportions from those screenshots.
3. Apply the design rules in this document.
4. Only then implement code or mockups.

If this document and the screenshots ever conflict, ask me guidance.

If the screenshots and implementation drift apart, stop and realign to the screenshots before continuing.

---

## Required Reference Assets

Use these files together:

- `screenshots/01-splash.jpeg`
- `screenshots/02-onboarding-1.jpeg`
- `screenshots/03-onboarding-2.jpeg`
- `screenshots/04-onboarding-3.jpeg`
- `screenshots/05-community-selection.jpeg`
- `screenshots/06-mood-checkin-1.jpeg`
- `screenshots/07-mood-checkin-2.jpeg`
- `screenshots/08-mood-checkin-3.jpeg`
- `screenshots/09-mood-checkin-4.jpeg`
- `screenshots/10-mood-checkin-5.jpeg`
- `screenshots/11-vent-writing-1.jpeg`
- `screenshots/12-vent-writing-2.jpeg`
- `screenshots/13-community-pulse-1.jpeg`
- `screenshots/14-community-pulse-2.jpeg`
- `screenshots/15-anonymous-match.jpeg`
- `screenshots/16-anonymous-chat.jpeg`
- `screenshots/17-expert-support-1.jpeg`
- `screenshots/18-expert-support-2.jpeg`
- `screenshots/19-expert-support-3.jpeg`
- `screenshots/20-emotional-pulse-1.jpeg`
- `screenshots/21-emotional-pulse-2.jpeg`
- `screenshots/22-emotional-pulse-3.jpeg`
- `screenshots/23-emotional-pulse-4.jpeg`
- `screenshots/24-emotional-pulse-5.jpeg`
- `Fonts/Plus_Jakarta_Sans/*`

---

## Product Definition

VentPulse is:

- a safe emotional space
- a warm digital journal
- an anonymous emotional companion
- a calm community pulse experience

VentPulse is not:

- a productivity app
- a clinical mental health app
- a traditional social platform
- a fintech-style product

The experience should always reduce emotional tension.

---

## Visual Direction

The app must feel:

- soft
- warm
- playful
- dreamy
- human
- gentle
- emotionally safe
- lightweight
- airy

Avoid:

- enterprise aesthetics
- harsh contrast
- dense dashboards
- sterile cards
- heavy shadows
- hard edges
- overly technical UI

---

## Non-Negotiable UI Rules

These are required.

- Light mode only.
- Use `Plus Jakarta Sans` as the primary font.
- Use soft purple, mint, blue, pink, and warm yellow tones.
- Keep surfaces bright and breathable.
- Use rounded corners everywhere.
- Keep spacing generous.
- Keep motion soft and calming.
- Use anonymous identities instead of real profiles.
- Do not introduce a dark theme.
- Do not introduce hard black shadows.
- Do not use enterprise or finance-like layouts.

---

## Design System

### Typography

Primary font:

- `Plus Jakarta Sans`

Fallbacks:

- `Inter`
- `DM Sans`
- `Nunito`

Typography should feel:

- rounded
- soft
- readable
- modern
- emotionally friendly

Avoid:

- condensed fonts
- aggressive geometric fonts
- technical or clinical typography

### Color Tokens

Use these base colors:

- Primary brand: `#8B7CF6`
- Secondary accent: `#B8A8FF`
- Emotional pink: `#F4A6C8`
- Calm blue: `#8FD6FF`
- Mint calm: `#8EE3C4`
- Main background: `#FAF8FF`
- Secondary background: `#F3F0FF`
- Primary text: `#2D2A4A`
- Secondary text: `#6E6A8A`

### Mood Colors

Use these mood colors exactly as the system basis:

- Happy: `#FFD166`
- Calm: `#8FD6FF`
- Sad: `#A0A9FF`
- Angry: `#FF9B85`
- Anxious: `#C8B6FF`
- Lonely: `#B5C0D0`

### Elevation

Shadows should be:

- soft
- diffused
- lightly colored
- dreamy

Recommended shadow style:

```css
box-shadow: 0px 10px 30px rgba(139, 124, 246, 0.08);
```

Avoid heavy black shadow stacks.

### Radius Scale

Use these radii consistently:

- Small: 16dp
- Medium: 22dp
- Large: 28dp
- XL cards: 36dp
- Mood blobs: 999dp

---

## Motion And Micro-Interactions

Motion is a core part of the product.

It should feel:

- calming
- warm
- emotional
- breathable
- organic

Avoid:

- flashy transitions
- aggressive movement
- excessive bounce
- distracting animation

### Preferred Motion Types

- gentle floating
- soft scale transitions
- slow breathing pulses
- smooth fades
- soft morphing
- liquid blob movement
- subtle blur transitions

### Mood Switching Behavior

When users switch moods, the UI should:

- softly morph the blob
- slightly bounce the selected mood
- animate facial expression changes
- pulse once
- settle gently

Recommended duration:

```text
300ms - 700ms
```

Recommended easing:

```text
FastOutSlowInEasing
```

### Interaction Feedback

Use subtle haptics for:

- mood selection
- successful check-ins
- reactions
- connection events

Use soft sound design only if needed:

- gentle taps
- soft ambient pops
- warm transitions

Never use loud notification sounds.

---

## Component Rules

### Primary Button

Use:

- rounded pill shape
- centered text
- soft shadow
- gentle elevation
- slightly oversized touch target

Recommended size:

- Height: 56dp
- Radius: 28dp

On press:

- slight scale down
- soft glow
- gentle haptic feedback

### Mood Blob

This is the most important component in the app.

It should feel:

- alive
- soft
- expressive
- comforting
- emotionally present

It should occupy:

- 40% - 60% of the vertical screen space

The blob should have:

- soft organic geometry
- slightly imperfect shape
- emotional facial expression
- floating idle motion
- warm gradients
- subtle shading

Each mood must have:

- a distinct face
- a distinct motion behavior
- a distinct color palette
- a distinct glow treatment

Examples:

- Happy: warm yellow, gentle bounce, smiling eyes
- Sad: softer blue, slower movement, drooping expression
- Angry: warm coral, compressed face, subtle vibration
- Calm: mint/blue, slow floating, soft glow pulse

### Anonymous Identity

Avoid real-profile behavior.

Use:

- generated aliases
- abstract avatars
- soft geometric visuals
- tiny emotional mascots

Examples:

- Quiet Cloud
- Soft River
- Gentle Orbit
- Calm Echo

### Navigation

Use a floating bottom navigation with:

- Pulse
- Community
- Connect
- Safe Space
- Profile

Navigation should:

- float above the bottom edge
- feel lightweight
- use soft blur/elevation
- remain minimal

---

## Screen Planning Rules

When planning the app, treat the screenshot folder as the screen map.

Each screen in the app should reproduce the screenshot style for that journey, including:

- emoji choice
- emoji scale
- icon treatment
- card shape
- spacing
- bottom nav appearance
- tag size
- button radius
- glow intensity
- empty space

Do not substitute generic UI patterns if a screenshot already defines the look.

---

## Screen Journey Specifications

### 1. Splash Screen

Goal:

- communicate emotional warmth
- communicate safety
- communicate softness

Layout:

- centered floating orb/mascot
- VentPulse logo near bottom center
- subtle tagline

Background:

- soft gradient from `#FAF8FF` to `#F3F0FF`

Animation:

- floating mascot
- subtle breathing pulse
- slow ambient gradient movement

Reference:

- `screenshots/01-splash.jpeg`

### 2. Onboarding Flow

Goal:

- make users feel safe
- make users feel welcomed
- make users feel understood
- make users feel not judged

Screen 1:

- headline: `A safe place to feel.`
- use floating emotional blobs

Reference:

- `screenshots/02-onboarding-1.jpeg`

Screen 2:

- headline: `Share how you feel anonymously.`
- use anonymous emotional pulse circles

Reference:

- `screenshots/03-onboarding-2.jpeg`

Screen 3:

- headline: `Connect with people who understand.`
- use two emotional blobs gently connecting

Reference:

- `screenshots/04-onboarding-3.jpeg`

### 3. Community Selection Screen

Goal:

- allow selection of neighborhood, city, and region

Visual style:

- concentric animated circles
- soft pulse motion
- glowing selection states

Reference:

- `screenshots/05-community-selection.jpeg`

### 4. Main Mood Check-In Screen

This is the most important screen in the app.

Goal:

- be focused
- be uncluttered
- be emotionally calming
- feel visually immersive

Layout:

- top: large title and soft subtitle
- center: large mood blob
- bottom: mood selector carousel
- bottom nav: floating pill

User interactions:

- swipe moods
- tap moods
- long press to preview

Selected mood should:

- scale up softly
- glow lightly
- animate facial expression

Emotional CTA:

- `Continue`

or:

- `Capture today's pulse`

References:

- `screenshots/06-mood-checkin-1.jpeg`
- `screenshots/07-mood-checkin-2.jpeg`
- `screenshots/08-mood-checkin-3.jpeg`
- `screenshots/09-mood-checkin-4.jpeg`
- `screenshots/10-mood-checkin-5.jpeg`

### 5. Vent Writing Screen

Goal:

- create a quiet and intimate writing space

Layout:

- large expressive text field
- minimal distractions
- warm, lightly textured background

Helpful prompts:

- `What's weighing on your heart today?`
- `What are you celebrating today today?`
- `You don't have to carry it alone.`

Voice input:

- large soft microphone button
- pulse animation when recording

References:

- `screenshots/11-vent-writing-1.jpeg`
- `screenshots/12-vent-writing-2.jpeg`

### 6. Community Pulse Feed

Goal:

- allow users to feel connected and supported
- avoid becoming a traditional social feed

Rules:

- no follower counts
- no likes
- no popularity mechanics
- no aggressive engagement patterns

Cards should:

- feel soft
- have large spacing
- use rounded corners
- use emotional accent rails or gradients

Supportive reactions should be:

- `I hear you`
- `Celebrating with you`
- `Sending warmth`
- `You are not alone`
- `Stay strong`

Use soft reaction motion:

- floating hearts
- soft pulse animation
- celebrate animation
- gentle glow
- subtle upward movement

References:

- `screenshots/13-community-pulse-1.jpeg`
- `screenshots/14-community-pulse-2.jpeg`

### 7. Anonymous Match Screen

Goal:

- create emotionally safe anonymous connection

Match UI:

- floating emotional avatars
- soft orbiting circles
- calm animations

Matching state text:

- `Finding someone who understands...`

Reference:

- `screenshots/15-anonymous-match.jpeg`

### 8. Anonymous Chat Screen

Goal:

- make conversations feel safe, warm, private, and human

Chat bubbles:

- rounded
- soft
- warm

Avoid:

- sharp messaging UI
- enterprise chat appearance

Emotional safety actions:

- report
- end conversation
- request expert
- pause conversation

Keep these subtle and non-threatening.

Reference:

- `screenshots/16-anonymous-chat.jpeg`

### 9. Expert Support Screen

Goal:

- allow users to connect with therapists, listeners, and volunteers

Visual direction:

- approachable
- warm
- emotionally safe

Avoid:

- hospital visuals
- sterile layouts
- clinical card patterns

Reference:

- `screenshots/17-expert-support-1.jpeg`
- `screenshots/18-expert-support-2.jpeg`
- `screenshots/19-expert-support-3.jpeg`

### 10. Emotional Pulse Screen

Goal:

- visualize emotional trends across concentric communities

Visualization style:

- soft flowing charts
- pulse rings
- animated gradients
- glowing emotional clusters

Avoid:

- harsh analytics dashboards
- enterprise charts

Reference:

- `screenshots/20-emotional-pulse-1.jpeg`
- `screenshots/21-emotional-pulse-2.jpeg`
- `screenshots/22-emotional-pulse-3.jpeg`
- `screenshots/23-emotional-pulse-4.jpeg`
- `screenshots/24-emotional-pulse-5.jpeg`

---

## Empty State Rules

Empty states should:

- feel hopeful
- feel emotionally gentle
- never feel cold or dead

Use:

- floating illustrations
- calming gradients
- comforting copy

---

## Accessibility Rules

Maintain:

- large touch targets
- strong readability
- soft contrast
- emotional clarity

Avoid visual overload.

Keep the interface understandable at a glance.

---

## Implementation Instructions For Codex

When Codex plans or builds VentPulse, it must:

- open and inspect the screenshots first
- match the screenshot composition exactly as the primary visual reference
- follow the motion rules and micro-interactions in this document
- preserve the light-mode-only constraint
- use the exact design system tokens defined here
- reproduce the same emotional tone seen in the screenshots
- keep the UI airy, soft, and non-crowded

When making UI decisions, Codex should prefer:

1. Screenshot fidelity
2. This document
3. Existing app structure
4. Generic defaults

If a generic default conflicts with the screenshots, do not use the default.

If an implementation choice changes the look and feel, verify it against the screenshots before shipping it.

---

## Final Emotional Goal

Every screen in VentPulse should communicate:

```text
You are safe here.
```

The app should feel like:

- a soft emotional exhale
- a quiet safe space
- a comforting companion

The emotional feeling is more important than feature density.

