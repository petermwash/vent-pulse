import { Cloud } from "lucide-react";

export default function Loading() {
  return (
    <main className="relative flex min-h-screen items-center justify-center overflow-hidden bg-[var(--background)] px-6 text-[var(--foreground)]">
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_46%_48%,rgba(142,227,196,0.2),transparent_28%),radial-gradient(circle_at_58%_30%,rgba(255,209,102,0.16),transparent_24%),linear-gradient(180deg,#fefcff,#faf8ff)]" />
      <section className="glass-panel relative w-full max-w-md rounded-[2rem] p-8 text-center">
        <div className="mx-auto flex h-14 w-14 items-center justify-center rounded-full bg-[rgba(184,168,255,0.2)]">
          <Cloud className="h-7 w-7 text-[var(--brand-primary)]" />
        </div>
        <h1 className="mt-5 text-2xl font-semibold">Gathering the atmosphere</h1>
        <p className="mt-3 text-sm leading-6 text-[var(--foreground-soft)]">
          Preparing a soft community pulse view for the demo.
        </p>
      </section>
    </main>
  );
}
