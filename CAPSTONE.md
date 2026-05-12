# CAPSTONE.md

# VentPulse Capstone Specification

## Purpose

This document defines the product, UX direction, architecture constraints, and execution priorities for VentPulse.

Codex should use this file as a working specification during:

- planning
- implementation
- refactoring
- documentation
- demo preparation

If any implementation choice conflicts with this document, this document wins.

## Project Overview

VentPulse is an anonymous emotional check-in and community emotional awareness platform designed to help people safely express how they feel, emotionally connect with others anonymously, and reduce emotional isolation before frustration escalates into social tension or conflict.

The platform focuses on creating a safe, playful, emotionally comforting, and lightweight experience instead of a clinical mental health application.

The core philosophy behind VentPulse is:

> "Sometimes people simply need a safe place to express themselves, feel heard, and know they are not alone."

---

# Core Problem

Many people silently carry:

* stress,
* frustration,
* loneliness,
* anxiety,
* anger,
* emotional exhaustion,
* or emotional pressure

without having a safe place to express themselves.

Existing social platforms are often:

* judgmental,
* performative,
* toxic,
* identity-driven,
* and emotionally unsafe.

As a result:

* people suppress emotions,
* emotional pressure accumulates,
* isolation increases,
* and communities lose visibility into collective emotional wellbeing.

VentPulse aims to create a healthier emotional outlet while preserving anonymity and emotional safety.

---

# Proposed Solution

VentPulse is a mobile-first anonymous emotional check-in platform where users:

* check in daily with their mood,
* optionally share anonymous vents,
* react to community emotions,
* and optionally connect anonymously with others or experts.

The system organizes users into concentric communities:

* Neighborhood
* Local Community
* City
* County/Region
* Country

This allows the platform to:

* visualize emotional pulse across communities,
* create localized emotional awareness,
* and eventually enable AI-powered emotional trend analysis.

---

# Product Vision

The product should feel:

* emotionally warm,
* playful,
* calming,
* safe,
* lightweight,
* expressive,
* and delightful.

The experience should NEVER feel:

* clinical,
* corporate,
* overly serious,
* surveillance-like,
* or emotionally cold.

The emotional experience is one of the most important parts of the project.

---

# Product Scope (MVP)

## Android Mobile App

Primary product experience.

- Use Supabase as a Backend and generated random anonymous identities store in Datastore whenever the app is lauched on new device or cache/data is cleared.
- Do NOT do full authentication flow

### Core Features

* onboarding flow
* concentric community selection
* daily mood check-in
* playful mood selection UI
* anonymous vent posting
* anonymous community feed
* supportive reactions
* anonymous random matching
* anonymous chat
* emotional pulse visualization
* profile/preferences
* lightweight moderation/reporting

### Reference Assets

Codex must reference these assets and skills when planning or building the Android app:

- `android-app/specs/*`

---

## Lightweight Moderator Dashboard

Simple web dashboard for moderators and experts.

- Use Supabase as a Backend and generated random anonymous identities for simple auth flow

### Dashboard Features

* community emotional pulse overview
* mood trend charts
* concentric community breakdowns
* flagged vent visibility
* emotional trend summaries
* basic AI insight cards (prototype)
* expert availability overview

This dashboard is primarily intended for:

* demo storytelling,
* visualization,
* and showcasing future extensibility.

### Reference Assets

Codex must reference these assets and skills when planning or building the dashboard:

- `dashboard/specs/*`
- `dashboard/AGENTS.md`

---

## Supabase

Use Supabase as a Backend serving both the Android App and the dashboard

- When setting up, ensure secrets like `SUPABASE_PUBLISHABLE_KEY` and `SUPABASE_URL` are stored securely in `local.properties` for android and `.env` fo the dashboard and they should not be commited to version control
- After setting up, have some sample communities pre-populated in the DB, for example we could have one community of Nairobi region with many concentric communities inside them like Westlands, Upperhill, South B e.t.c and those also will have even other concentric communities inside them based on the different neighbourhoods in those areas, from which the user will then be assigned/mapped

---

# AI Vision (Future Extensibility)

AI is NOT a core deliverable for MVP.

However, the system should clearly demonstrate future AI extensibility.

Future AI capabilities may include:

* sentiment analysis,
* emotional trend detection,
* community emotional pulse analysis,
* rising frustration detection,
* escalation prediction,
* anonymous emotional summarization,
* multilingual emotional understanding,
* AI-assisted moderation,
* and emotional wellbeing recommendations.

Example future AI insight:

> "Community Ring 2 has shown increasing frustration levels over the last 7 days."

The AI layer should feel:

* assistive,
* subtle,
* non-invasive,
* and privacy-conscious.

---

# Technical Architecture

## High-Level Stack

### Mobile App

* Kotlin
* Jetpack Compose
* MVVM Architecture
* Room Database
* Supabase SDK
* Coroutines + Flow

### Dashboard

* Next.js
* Tailwind CSS
* Recharts

### Backend Infrastructure

* Supabase

  * PostgreSQL
  * Realtime
  * Authentication
  * Storage
  * Row-Level Security

### AI Integration

* simulated/demo AI insights for MVP

---

# Architecture Philosophy

This project intentionally avoids:

* heavy backend infrastructure,
* microservices,
* unnecessary complexity,
* overengineering,
* and premature scalability concerns.

The focus should remain on:

* speed,
* polish,
* emotional UX,
* reliability during demo,
* and product storytelling.

---

# Offline-First Considerations

The Android app should support lightweight offline-first behavior where reasonable.

Examples:

* cached mood selections,
* cached feed state,
* local draft vents,
* optimistic UI updates.

Full offline synchronization complexity is NOT required for the MVP.

---

# Important UX/UI Principles

## The UI should feel:

* soft,
* expressive,
* emotionally calming,
* modern,
* tactile,
* and highly polished.

Recommended design direction:

* smooth animations
* expressive illustrations
* soft gradients
* floating cards
* cozy typography
* warm spacing
* emotionally friendly interactions

Avoid:

* harsh enterprise dashboards
* excessive text density
* clinical medical aesthetics
* dark/heavy emotional themes

---

# Anonymous Interaction Principles

Anonymity is a core product principle.

The system should:

* avoid exposing identities,
* avoid public usernames,
* avoid follower mechanics,
* and avoid popularity systems.

Users should feel emotionally safe.

Anonymous interactions may use:

* generated aliases,
* temporary identities,
* or abstract profile visuals.

---

# Emotional Safety Considerations

Because anonymity can create abuse risks, lightweight moderation systems are required.

MVP moderation may include:

* report functionality,
* keyword filtering,
* moderator review,
* cooldowns/rate limits,
* and community guidelines.

The platform should encourage:

* empathy,
* emotional support,
* respectful interaction,
* and emotional safety.

---

# Demo Goals

The final demo should emotionally communicate:

* emotional expression,
* anonymity,
* emotional connection,
* and collective emotional awareness.

The demo should feel:

* polished,
* smooth,
* emotionally memorable,
* and believable.

---

# Suggested Demo Flow

1. User opens app
2. Beautiful onboarding
3. User selects mood
4. User anonymously shares vent
5. User views community emotional pulse
6. User reacts to another anonymous vent
7. User should not be able to view other people's mood for the day or react to them until they have submitted theirs
8. User joins anonymous chat
9. Dashboard visualizes community mood trends
10. AI insight card appears

This should create a strong emotional and technical narrative.

---

# Repository Structure

```text
vent-pulse/
├── AGENTS.md
├── android-app/
├── dashboard/
├── CAPSTONE.md
├── README.md
```

---

# Codex Usage Instructions

Codex should continuously reference this CAPSTONE.md file during:

* planning,
* architecture,
* implementation,
* documentation,
* refactoring,
* and feature development.

The purpose of this file is to:

* preserve product vision,
* maintain UX consistency,
* avoid scope drift,
* and ensure implementation decisions align with the intended experience.

When making implementation decisions, prioritize:

1. emotional UX quality,
2. demo reliability,
3. simplicity,
4. polish,
5. maintainability,
6. speed of development.

Avoid unnecessary complexity unless explicitly required.

---

# Success Criteria

The project succeeds if:

* users emotionally connect with the experience,
* the demo feels polished and memorable,
* the emotional pulse concept is clearly communicated,
* the architecture feels realistic,
* and the AI extensibility vision is believable.

MVP success depends more on:

* product clarity,
* emotional storytelling,
* UX polish,
* and strategic AI usage

than on backend complexity.

---

# Final Product Philosophy

VentPulse is not just another social platform.

It is emotional infrastructure.

The goal is to create:

* safer emotional expression,
* emotional connection,
* collective emotional awareness,
* and healthier, safer and more peaceful communities

through anonymous, playful, and emotionally supportive experiences.
