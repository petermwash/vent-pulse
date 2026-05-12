# Repository Guidelines

## Project Structure & Module Organization
`CAPSTONE.md` is the product source of truth.

- `android-app/` owns the Kotlin + Jetpack Compose mobile MVP.
- `dashboard/` owns the Next.js moderator dashboard.
- Keep implementation work inside the owning module. Put reusable code close to where it is used.

## Build, Test, and Development Commands
- Android: `cd android-app && ./gradlew test`
- Dashboard: `cd dashboard && npm run dev`
- Dashboard: `cd dashboard && npm run build`
- Dashboard: `cd dashboard && npm run lint`

## Coding Style & Naming Conventions
Follow the conventions documented in each module. Keep names aligned with product language such as `MoodCheckIn`, `communityPulse`, or `anonymousVent`.

## Testing Guidelines
Prefer behavior-focused tests next to the feature or in the module test directory. Cover onboarding, mood selection, posting, feed updates, and moderation states, not just the happy path.

## Commit & Pull Request Guidelines
Use short, imperative commit messages. Pull requests should include a brief summary, UI screenshots or recordings when relevant, and notes for any commands, env vars, or schema changes.

## Product Alignment
Prioritize emotional safety, polished UX, anonymity, and demo reliability from `CAPSTONE.md`. Avoid overengineering.
