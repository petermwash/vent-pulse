# VentPulse Demo Script

Use this script for the final Phase 8 demo pass. The product story is emotional safety first: anonymous expression, community awareness, and gentle moderator visibility.

## Environment Check

- Android secrets stay local in `android-app/local.properties`.
- Dashboard secrets stay local in `dashboard/.env.local`.
- Only publishable Supabase keys are used by the clients.
- If Supabase is unavailable, both Android and dashboard should still show seeded demo data instead of a broken state.

## Validation Commands

```sh
cd android-app && ./gradlew test
cd dashboard && npm run lint
cd dashboard && npm run build
```

## Android Demo Flow

1. Launch the app and confirm the splash resolves into onboarding, community selection, or Pulse depending on local session state.
2. Complete onboarding if shown, then choose Brookside or Nairobi from the community selection screen.
3. On Pulse, swipe between moods, choose the mood that fits, and save the check-in.
4. Confirm the emotional pulse view appears with a soft mood visualization, supportive copy, and the Share anonymously action.
5. Open vent writing, type a short anonymous vent, save it, and confirm the app returns to Pulse.
6. Open Community, react with I hear you, report a vent, and confirm the feedback messages remain calm and non-punitive.
7. Open Connect, wait for matching or use Skip to Chat Demo, send a message, report a partner message, end the chat, and request expert support.
8. Open Profile and support surfaces to confirm the product still feels anonymous, safe, and lightweight.

## Dashboard Demo Flow

1. Start the dashboard with `cd dashboard && npm run dev`.
2. Open `http://localhost:3000` and confirm the page lands on the Nairobi atmosphere view.
3. Verify the canvas is the hero: bubbles are visible, animated softly, not hidden behind the right rail, and counts appear only in the hover/focus tooltip.
4. Open the community selector, move from Nairobi to Westlands or Brookside, and confirm the bubble field and right rail copy change.
5. Scroll the right rail through Community Insights, AI Observations, Flagged Vents, Support Team, and Weekly Emotional Flow.
6. Confirm the right rail reads as floating cards over the canvas, not a separate admin sidebar.
7. Confirm loading and error states are humane if the route is refreshed during slow Supabase reads or a temporary failure.

## Acceptance Notes

- The dashboard must feel like a living emotional observatory, not an enterprise admin panel.
- Empty states should sound hopeful and gentle.
- Motion should be soft and respect reduced-motion preferences.
- No screenshots, logs, or commits should expose real user data or private Supabase credentials.
