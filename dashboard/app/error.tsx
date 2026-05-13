"use client";

import { RefreshCcw } from "lucide-react";

type ErrorPageProps = {
  reset: () => void;
};

export default function ErrorPage({ reset }: ErrorPageProps) {
  return (
    <main className="relative flex min-h-screen items-center justify-center overflow-hidden bg-[var(--background)] px-6 text-[var(--foreground)]">
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_42%_44%,rgba(255,155,133,0.13),transparent_28%),radial-gradient(circle_at_58%_34%,rgba(184,168,255,0.18),transparent_26%),linear-gradient(180deg,#fefcff,#faf8ff)]" />
      <section className="glass-panel relative w-full max-w-md rounded-[2rem] p-8 text-center">
        <div className="mx-auto flex h-14 w-14 items-center justify-center rounded-full bg-[rgba(255,155,133,0.16)]">
          <RefreshCcw className="h-7 w-7 text-[var(--emotion-frustration)]" />
        </div>
        <h1 className="mt-5 text-2xl font-semibold">The atmosphere needs a refresh</h1>
        <p className="mt-3 text-sm leading-6 text-[var(--foreground-soft)]">
          The demo can recover without exposing technical details or user data.
        </p>
        <button
          type="button"
          onClick={reset}
          className="mt-6 rounded-full bg-[var(--brand-primary)] px-5 py-3 text-sm font-semibold text-white shadow-[0_16px_34px_rgba(139,124,246,0.28)]"
        >
          Try again
        </button>
      </section>
    </main>
  );
}
