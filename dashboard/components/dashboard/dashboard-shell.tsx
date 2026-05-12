"use client";

import {
  ArrowDownRight,
  ArrowUpRight,
  ChevronDown,
  ChevronUp,
  Cloud,
  Heart,
  Map,
  MapPin,
  Settings,
  Sparkles,
  UserCircle2,
} from "lucide-react";
import { AnimatePresence, motion } from "framer-motion";
import { startTransition, useState } from "react";

import { AtmosphereCanvas } from "@/components/dashboard/atmosphere-canvas";
import {
  aiObservations,
  bubbleData,
  communityOptions,
  insightCards,
  navItems,
  supportMembers,
  weekLabels,
  weeklyFlow,
} from "@/components/dashboard/dashboard-data";

type DashboardShellProps = {
  supabaseReady: boolean;
};

const navIcons = {
  atmosphere: Cloud,
  communities: Map,
  insights: Sparkles,
  support: Heart,
  settings: Settings,
};

const statusToneColor = {
  calm: "var(--brand-mint)",
  busy: "var(--emotion-frustration)",
  warm: "var(--emotion-joy)",
};

function TrendMark({ trend }: { trend: "up" | "down" | "flat" }) {
  if (trend === "up") {
    return <ArrowUpRight className="h-7 w-7 text-[var(--brand-mint)]" />;
  }

  if (trend === "down") {
    return <ArrowDownRight className="h-7 w-7 text-[var(--emotion-frustration)]" />;
  }

  return <span className="text-3xl leading-none text-[var(--brand-secondary)]">-</span>;
}

export function DashboardShell({ supabaseReady }: DashboardShellProps) {
  const [activeNav, setActiveNav] = useState("atmosphere");
  const [selectedCommunityId, setSelectedCommunityId] = useState("nairobi");
  const [isCommunityPickerOpen, setCommunityPickerOpen] = useState(false);

  const selectedCommunity =
    communityOptions.find((option) => option.id === selectedCommunityId) ??
    communityOptions[0];

  return (
    <main className="relative isolate min-h-screen overflow-hidden px-4 py-5 text-[var(--foreground)] sm:px-6 lg:px-8">
      <div className="pointer-events-none absolute inset-0 -z-10">
        <div className="absolute left-[-8%] top-[12%] h-[28rem] w-[28rem] rounded-full bg-[rgba(184,168,255,0.18)] blur-[120px]" />
        <div className="absolute bottom-[8%] right-[18%] h-[26rem] w-[26rem] rounded-full bg-[rgba(143,214,255,0.16)] blur-[120px]" />
        <div className="absolute right-[6%] top-[14%] h-[18rem] w-[18rem] rounded-full bg-[rgba(255,209,102,0.12)] blur-[110px]" />
      </div>

      <div className="mx-auto flex min-h-[calc(100vh-2.5rem)] max-w-[1820px] flex-col gap-6">
        <nav className="glass-panel mx-auto flex w-full max-w-[82rem] items-center justify-between gap-2 rounded-full p-3">
          <div className="grid w-full grid-cols-2 gap-2 sm:flex sm:justify-between">
            {navItems.map((item) => {
              const Icon = navIcons[item.id as keyof typeof navIcons];
              const isActive = item.id === activeNav;

              return (
                <button
                  key={item.id}
                  type="button"
                  onClick={() => setActiveNav(item.id)}
                  className={`flex items-center justify-center gap-3 rounded-full px-4 py-3 text-sm font-medium transition sm:min-w-[9.5rem] sm:text-[1rem] ${
                    isActive
                      ? "bg-[var(--brand-primary)] text-white shadow-[0_18px_36px_rgba(139,124,246,0.32)]"
                      : "text-[var(--foreground-soft)] hover:bg-white/55"
                  }`}
                >
                  <Icon className="h-5 w-5" strokeWidth={2.1} />
                  <span>{item.label}</span>
                </button>
              );
            })}
          </div>
        </nav>

        <section className="grid flex-1 gap-6 xl:grid-cols-[minmax(18rem,24rem)_minmax(0,1fr)_minmax(20rem,29rem)]">
          <aside className="flex flex-col gap-6 xl:pt-20">
            <div className="px-2">
              <div className="mb-4 flex items-center gap-3 text-[var(--foreground-soft)]">
                <MapPin className="h-6 w-6" />
                <span className="text-xl font-medium">Viewing Community</span>
              </div>

              <button
                type="button"
                onClick={() => setCommunityPickerOpen((open) => !open)}
                className="group flex items-end gap-3 text-left"
              >
                <h1 className="text-4xl font-semibold tracking-[-0.05em] text-[var(--foreground)] sm:text-6xl">
                  {selectedCommunity.pathLabel}
                </h1>
                {isCommunityPickerOpen ? (
                  <ChevronUp className="mb-2 h-9 w-9 text-[var(--brand-primary)] transition group-hover:translate-y-[-2px]" />
                ) : (
                  <ChevronDown className="mb-2 h-9 w-9 text-[var(--brand-primary)] transition group-hover:translate-y-[2px]" />
                )}
              </button>

              <p className="mt-4 text-2xl text-[var(--foreground-soft)]">
                {selectedCommunity.subtitle}
              </p>
              <p className="mt-6 max-w-md text-base leading-8 text-[var(--foreground-soft)]">
                {selectedCommunity.signalTone}
              </p>
            </div>

            <AnimatePresence initial={false}>
              {isCommunityPickerOpen ? (
                <motion.div
                  key="community-picker"
                  initial={{ opacity: 0, y: -12 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -10 }}
                  transition={{ duration: 0.28, ease: "easeOut" }}
                  className="glass-panel soft-ring rounded-[2rem] p-4"
                >
                  <div className="flex flex-col gap-3">
                    {communityOptions.map((option) => {
                      const isSelected = option.id === selectedCommunity.id;

                      return (
                        <button
                          key={option.id}
                          type="button"
                          onClick={() =>
                            startTransition(() => {
                              setSelectedCommunityId(option.id);
                              setCommunityPickerOpen(false);
                            })
                          }
                          className={`flex items-start gap-4 rounded-[1.65rem] px-5 py-6 text-left transition ${
                            isSelected
                              ? "bg-[rgba(184,168,255,0.15)]"
                              : "hover:bg-white/45"
                          }`}
                        >
                          <span
                            className={`mt-3 h-4 w-4 rounded-full ${
                              isSelected
                                ? "bg-[var(--brand-primary)]"
                                : "bg-[rgba(184,168,255,0.32)]"
                            }`}
                          />
                          <span>
                            <span className="block text-2xl font-semibold text-[var(--foreground)]">
                              {option.name}
                            </span>
                            <span className="block text-xl text-[var(--foreground-soft)]">
                              {option.parentLabel}
                            </span>
                          </span>
                        </button>
                      );
                    })}
                  </div>
                </motion.div>
              ) : null}
            </AnimatePresence>
          </aside>

          <section className="relative min-h-[32rem]">
            <AtmosphereCanvas bubbles={bubbleData} />
          </section>

          <aside className="glass-panel rounded-[2.2rem] p-5 sm:p-6 xl:max-h-[78vh] xl:overflow-y-auto xl:p-8">
            <div className="mb-8 flex items-center justify-between gap-4">
              <div>
                <p className="text-sm uppercase tracking-[0.32em] text-[var(--brand-primary)]">
                  Phase 6 Foundation
                </p>
                <h2 className="mt-3 text-3xl font-semibold tracking-[-0.04em]">
                  Community Insights
                </h2>
              </div>
              <div
                className={`rounded-full px-4 py-2 text-sm font-medium ${
                  supabaseReady
                    ? "bg-[rgba(142,227,196,0.18)] text-[var(--foreground)]"
                    : "bg-[rgba(255,209,102,0.18)] text-[var(--foreground)]"
                }`}
              >
                {supabaseReady ? "Supabase ready" : "Seeded demo data"}
              </div>
            </div>

            <section className="space-y-4">
              {insightCards.map((card, index) => (
                <motion.article
                  key={card.id}
                  initial={{ opacity: 0, x: 16 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ duration: 0.4, delay: index * 0.08 }}
                  className="glass-panel rounded-[2rem] p-6"
                >
                  <div className="mb-5 flex items-start justify-between gap-4">
                    <h3 className="text-2xl font-semibold tracking-[-0.04em]">
                      {card.title}
                    </h3>
                    <TrendMark trend={card.trend} />
                  </div>
                  <p className="text-lg leading-9 text-[var(--foreground-soft)]">
                    {card.body}
                  </p>
                </motion.article>
              ))}
            </section>

            <section className="mt-10">
              <h3 className="text-3xl font-semibold tracking-[-0.04em]">
                AI Observations
              </h3>
              <div className="mt-5 space-y-4">
                {aiObservations.map((observation, index) => (
                  <motion.article
                    key={observation.id}
                    initial={{ opacity: 0, y: 12 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.42, delay: 0.2 + index * 0.08 }}
                    className="glass-panel rounded-[2rem] p-6"
                  >
                    <h4 className="text-xl font-semibold">{observation.title}</h4>
                    <p className="mt-3 text-lg leading-8 text-[var(--foreground-soft)]">
                      {observation.body}
                    </p>
                  </motion.article>
                ))}
              </div>
            </section>

            <section className="mt-10">
              <div className="glass-panel rounded-[2rem] p-6">
                <div className="mb-6 flex items-center gap-4">
                  <div className="flex h-[4.5rem] w-[4.5rem] items-center justify-center rounded-full bg-[rgba(184,168,255,0.18)]">
                    <Heart className="h-8 w-8 text-[var(--brand-primary)]" />
                  </div>
                  <div>
                    <h3 className="text-3xl font-semibold tracking-[-0.04em]">
                      Support Team
                    </h3>
                    <p className="text-lg text-[var(--foreground-soft)]">
                      3 available now
                    </p>
                  </div>
                </div>

                <div className="space-y-4">
                  {supportMembers.map((member) => (
                    <article
                      key={member.id}
                      className="rounded-[1.8rem] bg-[rgba(255,255,255,0.54)] p-5 shadow-[0_18px_36px_rgba(139,124,246,0.08)]"
                    >
                      <div className="flex items-center gap-4">
                        <div className="relative flex h-16 w-16 items-center justify-center rounded-full bg-[rgba(184,168,255,0.12)]">
                          <UserCircle2 className="h-9 w-9 text-[var(--brand-primary)]" />
                          <span
                            className="absolute bottom-0 right-0 h-5 w-5 rounded-full border-4 border-white"
                            style={{ backgroundColor: statusToneColor[member.statusTone] }}
                          />
                        </div>
                        <div>
                          <h4 className="text-2xl font-semibold tracking-[-0.04em]">
                            {member.name}
                          </h4>
                          <p className="text-lg text-[var(--foreground-soft)]">
                            {member.role}
                          </p>
                        </div>
                      </div>
                    </article>
                  ))}
                </div>
              </div>
            </section>

            <section className="mt-10">
              <div className="glass-panel rounded-[2rem] p-6">
                <h3 className="text-3xl font-semibold tracking-[-0.04em]">
                  Weekly Emotional Flow
                </h3>
                <div className="mt-6 space-y-8">
                  {weeklyFlow.map((series) => (
                    <div key={series.id}>
                      <div className="mb-4 flex items-center gap-3">
                        <span
                          className="h-6 w-6 rounded-full"
                          style={{ backgroundColor: `var(${series.colorVar})` }}
                        />
                        <span className="text-xl font-medium">{series.label}</span>
                      </div>
                      <div className="grid grid-cols-7 gap-3">
                        {series.values.map((value, index) => (
                          <div key={`${series.id}-${weekLabels[index]}`} className="text-center">
                            <div className="flex h-20 items-end justify-center">
                              <div
                                className="rounded-full shadow-[0_0_28px_rgba(255,255,255,0.45)]"
                                style={{
                                  backgroundColor: `var(${series.colorVar})`,
                                  width: `${Math.max(2.8, value / 18)}rem`,
                                  height: `${Math.max(2.8, value / 18)}rem`,
                                  opacity: 0.92,
                                }}
                              />
                            </div>
                            <span className="mt-3 block text-lg text-[var(--foreground-soft)]">
                              {weekLabels[index]}
                            </span>
                          </div>
                        ))}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </section>
          </aside>
        </section>
      </div>
    </main>
  );
}
