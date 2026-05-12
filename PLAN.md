# VentPulse Incremental Implementation Plan

## Summary

Build VentPulse in phased branches, with each phase ending in validation, push, and a GitHub pull request. `CAPSTONE.md` remains the product source of truth; Android is the primary user experience, the dashboard supports moderation and demo storytelling, and Supabase is the shared backend from the start.

Chosen defaults:

- Backend: existing Supabase project, connected early.
- Android: Kotlin, Jetpack Compose, Koin, Room, DataStore, and Supabase Kotlin SDK.
- Dashboard: Next.js, Tailwind CSS, Framer Motion, and custom canvas visualization.
- Auth: Supabase anonymous sign-ins for app users, with generated anonymous display identities stored locally and in Supabase.
- Chat: realtime MVP with basic random matching, anonymous messages, report, and end-chat actions.
- AI: simulated insight cards only for MVP.

## Phase Plan

Use this branch and PR rhythm for every phase:

```text
git checkout main
git pull
git checkout -b phase-N-short-name
implement
run checks
git push -u origin phase-N-short-name
open pull request
review and merge after approval
```

### Phase 0: Project Foundation

Branch: `phase-0-project-foundation`

- Add root `PLAN.md` after this plan is approved.
- Confirm Supabase project URL and publishable key setup for `android-app/local.properties` and `dashboard/.env.local`.
- Add required ignored env examples only if missing.
- Verify baseline commands: Android test, dashboard lint, and dashboard build.

### Phase 1: Supabase Schema

Branch: `phase-1-supabase-schema`

- Add Supabase migrations for MVP tables: `communities`, `anonymous_profiles`, `mood_checkins`, `vents`, `vent_reactions`, `reports`, `matches`, `chat_messages`, `expert_profiles`, and optional `demo_insights`.
- Enable RLS on all exposed tables.
- Allow anonymous authenticated users to create and read their own profile, post vents, react, report, match, and chat only inside permitted community or session scope.
- Seed Nairobi hierarchy: Kenya, Nairobi, Westlands, Upper Hill, South B, Brookside, and neighborhood children.
- Run Supabase security and performance advisors after schema work.

### Phase 2: Android Foundation

Branch: `phase-2-android-foundation`

- Replace default Android scaffold with VentPulse foundations: light-only theme, Plus Jakarta Sans, design tokens, app typography, navigation shell, Koin setup, Supabase client, Room database, and DataStore anonymous session storage.
- Add feature package structure for onboarding, community selection, mood check-in, venting and feed, pulse, matching and chat, support, and profile.
- Use MVI screen state/action/event patterns and typed navigation from the checked-in Android skills.
- Add tests for identity/session creation and basic ViewModel behavior.

### Phase 3: Android Core Flow

Branch: `phase-3-android-core-flow`

- Build screenshot-faithful splash, onboarding, community selection, mood check-in, and vent writing screens.
- Persist anonymous identity, selected community, mood check-ins, and vents through Room plus Supabase.
- Support offline-friendly reads where useful: Room as local cache, Supabase as sync source.
- Verify UI against `android-app/specs/screenshots/01-12`.

### Phase 4: Android Community Pulse

Branch: `phase-4-android-community-pulse`

- Build anonymous community feed, supportive reactions, report flow, emotional pulse visualization, profile/preferences, and expert support screens.
- Wire feed, reactions, reports, community pulse data, and expert availability to Supabase.
- Add behavior tests for posting, reacting, reporting, and community filtering.
- Verify UI against `android-app/specs/screenshots/13-24`.

### Phase 5: Realtime Matching And Chat

Branch: `phase-5-realtime-matching-chat`

- Implement basic anonymous matching using Supabase tables and Realtime subscriptions.
- Build match screen and anonymous chat with send/receive, end conversation, report, and request expert actions.
- Keep chat identity anonymous: generated aliases and avatars only, no real profile data.
- Add tests for match state transitions and chat repository behavior.

### Phase 6: Dashboard Foundation

Branch: `phase-6-dashboard-foundation`

- Replace default Next starter page with VentPulse dashboard layout: light-only theme, Plus Jakarta Sans, top navigation, community context header, central atmosphere canvas, and right insight rail.
- Add Supabase JS client using `.env.local`.
- Add Framer Motion and lucide icons; use custom canvas for the atmospheric bubble field.
- Respect `dashboard/AGENTS.md` by checking local Next docs before implementation.

### Phase 7: Dashboard Data And Demo

Branch: `phase-7-dashboard-data-and-demo`

- Wire dashboard to Supabase reads for communities, mood summaries, reports, expert availability, and demo AI insights.
- Implement community dropdown, Nairobi/Brookside state changes, right-rail scroll states, support presence, weekly emotional flow, and flagged vent visibility.
- Keep charts atmospheric: bubbles, ripples, clusters, and soft nodes instead of enterprise tables.
- Verify against all dashboard screenshots.

### Phase 8: Polish And Demo Readiness

Branch: `phase-8-polish-demo-readiness`

- Run an end-to-end demo pass across Android and dashboard.
- Fix UI drift from screenshots, accessibility gaps, empty states, loading/error states, and seed-data gaps.
- Confirm no secrets are committed.
- Run Android tests, dashboard lint/build, Supabase advisors, and a manual demo script.

## Libraries And SDKs

### Android

- Jetpack Compose Material 3, Navigation Compose, and lifecycle runtime/Compose.
- Koin for dependency injection.
- Room for local cache.
- DataStore Preferences for anonymous identity and session flags.
- Supabase Kotlin SDK with Auth, PostgREST, and Realtime modules.
- Ktor Android engine, Kotlinx Serialization, Coroutines, and Flow.
- JUnit, AndroidX test, coroutine test, and Turbine for focused async tests.

### Dashboard

- Existing Next.js, React, TypeScript, and Tailwind CSS.
- `@supabase/supabase-js` for backend access.
- `framer-motion` for soft transitions and rail interactions.
- `lucide-react` for UI icons.
- Custom canvas animation for the emotional atmosphere; avoid Recharts unless a simple fallback is needed.

### Supabase

- PostgreSQL, Auth anonymous sign-ins, Realtime, RLS, migrations, and seed data.
- Use publishable client keys only in Android and dashboard.
- Do not expose a service role key in client code.
- Follow current Supabase docs during implementation:
  - https://supabase.com/docs/guides/auth/auth-anonymous
  - https://supabase.com/docs/guides/getting-started/quickstarts/kotlin
  - https://supabase.com/docs/reference/kotlin/initializing

## Test Plan

- Android: `cd android-app && ./gradlew test`
- Dashboard: `cd dashboard && npm run lint && npm run build`
- Supabase: run advisors after schema/RLS changes and test representative insert/select flows as anonymous authenticated users.
- Manual acceptance: complete onboarding, select community, check in mood, post vent, react/report, view pulse, match/chat, inspect dashboard community pulse and flagged content.
- Visual acceptance: compare Android and dashboard screens against their `specs/screenshots` folders before each UI phase is considered done.

## Assumptions

- You will provide or confirm access to the existing Supabase project before Phase 1 implementation.
- The app will use Supabase anonymous Auth, not a full email/password login flow.
- Dashboard moderator auth remains lightweight for MVP; stronger role-based access can be added after the demo loop is stable.
- AI insights are seeded/simulated copy for MVP, with no OpenAI or external AI SDK required yet.
- `PLAN.md` is the tracking reference for this incremental implementation plan.
