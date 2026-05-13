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
  type CommunityExperience,
  communityExperiences,
  navItems,
  weekLabels,
} from "@/components/dashboard/dashboard-data";

type DashboardShellProps = {
  communities?: CommunityExperience[];
  dataMode: "supabase" | "demo";
  dataWarnings: string[];
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
    return <ArrowUpRight className="h-5 w-5 text-[var(--brand-mint)]" />;
  }

  if (trend === "down") {
    return <ArrowDownRight className="h-5 w-5 text-[var(--emotion-frustration)]" />;
  }

  return <span className="text-2xl leading-none text-[var(--brand-secondary)]">-</span>;
}

export function DashboardShell({
  communities = communityExperiences,
  dataMode,
  dataWarnings,
}: DashboardShellProps) {
  const [activeNav, setActiveNav] = useState("atmosphere");
  const [selectedCommunityId, setSelectedCommunityId] = useState("nairobi");
  const [isCommunityPickerOpen, setCommunityPickerOpen] = useState(false);

  const communityOptions = communities.map((community) => ({
    id: community.id,
    name: community.name,
    parentLabel: community.parentLabel,
    depth: community.depth,
  }));
  const selectedCommunity =
    communities.find((community) => community.id === selectedCommunityId) ??
    communities.find((community) => community.id === "nairobi") ??
    communities[0];
  const availableNow = selectedCommunity.supportMembers.filter(
    (member) => member.statusTone !== "busy",
  ).length;

  return (
    <main className="relative isolate min-h-screen overflow-hidden bg-[var(--background)] text-[var(--foreground)]">
      <motion.nav
        initial={{ opacity: 0, y: -18 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.55, ease: "easeOut" }}
        className="glass-panel absolute left-1/2 top-5 z-40 w-[min(72rem,calc(100%-2rem))] -translate-x-1/2 rounded-full p-2"
      >
        <div className="grid grid-cols-2 gap-2 md:grid-cols-5">
          {navItems.map((item) => {
            const Icon = navIcons[item.id as keyof typeof navIcons];
            const isActive = item.id === activeNav;

            return (
              <button
                key={item.id}
                type="button"
                aria-current={isActive ? "page" : undefined}
                onClick={() =>
                  startTransition(() => {
                    setActiveNav(item.id);
                    setCommunityPickerOpen(item.id === "communities");
                  })
                }
                className={`flex items-center justify-center gap-2 rounded-full px-3 py-2.5 text-xs font-medium transition sm:text-sm ${
                  isActive
                    ? "bg-[var(--brand-primary)] text-white shadow-[0_16px_32px_rgba(139,124,246,0.32)]"
                    : "text-[var(--foreground-soft)] hover:bg-white/65"
                }`}
              >
                <Icon className="h-4 w-4" strokeWidth={2.1} />
                <span>{item.label}</span>
              </button>
            );
          })}
        </div>
      </motion.nav>

      <motion.section
        initial={{ opacity: 0, scale: 0.985 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.7, ease: "easeOut", delay: 0.08 }}
        className="relative min-h-screen overflow-hidden"
      >
        <div className="absolute left-4 top-28 z-20 w-[min(17rem,calc(100%-2rem))] sm:left-6">
            <div className="glass-panel rounded-[1.4rem] p-4">
              <div className="mb-2 flex items-center gap-2 text-[var(--foreground-soft)]">
                <MapPin className="h-4 w-4" />
                <span className="text-xs font-medium">Viewing Community</span>
              </div>

              <button
                type="button"
                aria-expanded={isCommunityPickerOpen}
                aria-controls="community-picker"
                aria-label={`Viewing ${selectedCommunity.headingLabel}. Open community selector.`}
                onClick={() => setCommunityPickerOpen((open) => !open)}
                className="group flex w-full items-start justify-between gap-3 text-left"
              >
                <span>
                  <span className="block text-lg font-semibold leading-tight text-[var(--foreground)]">
                    {selectedCommunity.headingLabel}
                  </span>
                  <span className="mt-1 block text-xs leading-5 text-[var(--foreground-soft)]">
                    {selectedCommunity.subtitle}
                  </span>
                </span>
                {isCommunityPickerOpen ? (
                  <ChevronUp className="mt-1 h-5 w-5 shrink-0 text-[var(--brand-primary)] transition group-hover:translate-y-[-1px]" />
                ) : (
                  <ChevronDown className="mt-1 h-5 w-5 shrink-0 text-[var(--brand-primary)] transition group-hover:translate-y-[1px]" />
                )}
              </button>
            </div>

            <AnimatePresence initial={false}>
              {isCommunityPickerOpen ? (
                <motion.div
                  key="community-picker"
                  id="community-picker"
                  initial={{ opacity: 0, y: -8 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -8 }}
                  transition={{ duration: 0.24, ease: "easeOut" }}
                  className="glass-panel soft-ring mt-3 rounded-[1.4rem] p-2"
                >
                  <div className="flex max-h-[28rem] flex-col gap-1 overflow-y-auto soft-scrollbar pr-1">
                    {communityOptions.map((option) => {
                      const isSelected = option.id === selectedCommunity.id;

                      return (
                        <button
                          key={option.id}
                          type="button"
                          aria-pressed={isSelected}
                          onClick={() =>
                            startTransition(() => {
                              setSelectedCommunityId(option.id);
                              setCommunityPickerOpen(false);
                              setActiveNav("atmosphere");
                            })
                          }
                          className={`rounded-[1.1rem] px-3 py-3 text-left transition ${
                            isSelected
                              ? "bg-[rgba(184,168,255,0.16)]"
                              : "hover:bg-white/60"
                          }`}
                          style={{ paddingLeft: `${0.75 + option.depth * 0.42}rem` }}
                        >
                          <div className="flex items-start gap-3">
                            <span
                              className={`mt-2 h-2.5 w-2.5 rounded-full ${
                                isSelected
                                  ? "bg-[var(--brand-primary)]"
                                  : "bg-[rgba(184,168,255,0.34)]"
                              }`}
                            />
                            <span>
                              <span className="block text-sm font-semibold leading-5">
                                {option.name}
                              </span>
                              <span className="block text-xs leading-5 text-[var(--foreground-soft)]">
                                {option.parentLabel}
                              </span>
                            </span>
                          </div>
                        </button>
                      );
                    })}
                  </div>
                </motion.div>
              ) : null}
            </AnimatePresence>
        </div>

        <AtmosphereCanvas
          key={selectedCommunity.id}
          bubbles={selectedCommunity.bubbles}
          className="h-screen min-h-screen rounded-none"
        />
      </motion.section>

      <motion.aside
        initial={{ opacity: 0, x: 26 }}
        animate={{ opacity: 1, x: 0 }}
        transition={{ duration: 0.65, ease: "easeOut", delay: 0.16 }}
        aria-label="Dashboard insight rail"
        className="soft-scrollbar absolute bottom-0 right-4 top-28 z-30 w-[min(25.5rem,calc(100%-2rem))] overflow-y-auto pb-10 sm:right-6"
      >
          <div className="space-y-10">
            <section>
              <h2 className="text-2xl font-semibold text-[var(--foreground)]">
                Community Insights
              </h2>
              <p className="mt-2 max-w-xl text-xs leading-6 text-[var(--foreground-soft)]">
                {dataMode === "supabase"
                  ? "Live Supabase reads are shaping this emotional atmosphere."
                  : "Seeded demo atmosphere is active while Supabase data remains unavailable."}
              </p>
              {dataWarnings.length > 0 ? (
                <p className="mt-2 text-xs leading-5 text-[var(--foreground-soft)]">
                  Some reads fell back to demo data for this preview.
                </p>
              ) : null}

              <div className="mt-6 space-y-5">
                {selectedCommunity.insights.map((card, index) => (
                  <motion.article
                    key={card.id}
                    initial={{ opacity: 0, x: 16 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ duration: 0.4, delay: index * 0.08 }}
                    className="glass-panel rounded-[1.7rem] p-5"
                  >
                    <div className="mb-3 flex items-start justify-between gap-4">
                      <h3 className="text-xl font-semibold leading-tight">
                        {card.title}
                      </h3>
                      <TrendMark trend={card.trend} />
                    </div>
                    <p className="text-sm leading-7 text-[var(--foreground-soft)]">
                      {card.body}
                    </p>
                  </motion.article>
                ))}
              </div>
            </section>

            <section>
              <h3 className="text-2xl font-semibold">AI Observations</h3>
              <div className="mt-6 space-y-5">
                {selectedCommunity.observations.map((observation, index) => (
                  <motion.article
                    key={observation.id}
                    initial={{ opacity: 0, y: 12 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.42, delay: 0.18 + index * 0.08 }}
                    className="rounded-[1.8rem] border border-[rgba(184,168,255,0.46)] bg-[rgba(243,240,255,0.34)] p-5 shadow-[0_26px_70px_rgba(139,124,246,0.11)] backdrop-blur-2xl"
                  >
                    <div className="flex gap-4">
                      <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-[rgba(184,168,255,0.22)] shadow-[0_0_34px_rgba(139,124,246,0.24)]">
                        <Sparkles className="h-6 w-6 text-[var(--brand-primary)]" />
                      </div>
                      <div>
                        <p className="text-sm font-semibold text-[var(--brand-secondary)]">
                          AI Observation
                        </p>
                        <p className="mt-3 text-sm leading-7 text-[var(--foreground)]">
                          {observation.body}
                        </p>
                      </div>
                    </div>
                  </motion.article>
                ))}
              </div>
            </section>

            <section>
              <h3 className="text-2xl font-semibold">Flagged Vents</h3>
              <div className="mt-6 space-y-5">
                {selectedCommunity.flaggedVents.length > 0 ? (
                  selectedCommunity.flaggedVents.map((vent, index) => (
                    <motion.article
                      key={vent.id}
                      initial={{ opacity: 0, y: 12 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{ duration: 0.42, delay: 0.14 + index * 0.08 }}
                      className="glass-panel rounded-[1.7rem] p-5"
                    >
                      <div className="mb-3 flex items-start justify-between gap-4">
                        <div>
                          <p className="text-sm font-semibold text-[var(--foreground)]">
                            {vent.mood}
                          </p>
                          <p className="text-xs text-[var(--foreground-soft)]">
                            {vent.latestReportedAt}
                          </p>
                        </div>
                        <span className="rounded-full bg-[rgba(255,155,133,0.18)] px-3 py-1 text-xs font-semibold text-[var(--foreground)]">
                          {vent.reportCount} reports
                        </span>
                      </div>
                      <p className="text-sm leading-7 text-[var(--foreground-soft)]">
                        {vent.body}
                      </p>
                      <p className="mt-3 text-xs font-medium text-[var(--brand-primary)]">
                        {vent.moderationStatus.replace("_", " ")}
                      </p>
                    </motion.article>
                  ))
                ) : (
                  <article className="glass-panel rounded-[1.7rem] p-5">
                    <p className="text-sm leading-7 text-[var(--foreground-soft)]">
                      No flagged vents are visible for this community right now.
                    </p>
                  </article>
                )}
              </div>
            </section>

            <section>
              <div className="glass-panel rounded-[1.8rem] p-5">
                <div className="mb-5 flex items-center gap-3">
                  <div className="flex h-12 w-12 items-center justify-center rounded-full bg-[rgba(184,168,255,0.18)]">
                    <Heart className="h-6 w-6 text-[var(--brand-primary)]" />
                  </div>
                  <div>
                    <h3 className="text-2xl font-semibold">Support Team</h3>
                    <p className="text-sm text-[var(--foreground-soft)]">
                      {availableNow} available now
                    </p>
                  </div>
                </div>

                <div className="space-y-4">
                  {selectedCommunity.supportMembers.map((member) => (
                    <article
                      key={member.id}
                      className="rounded-[1.4rem] bg-[rgba(255,255,255,0.52)] p-4 shadow-[0_18px_36px_rgba(139,124,246,0.08)]"
                    >
                      <div className="flex items-center gap-3">
                        <div className="relative flex h-12 w-12 items-center justify-center rounded-full bg-[rgba(184,168,255,0.12)]">
                          <UserCircle2 className="h-7 w-7 text-[var(--brand-primary)]" />
                          <span
                            className="absolute bottom-0 right-0 h-4 w-4 rounded-full border-[3px] border-white"
                            style={{ backgroundColor: statusToneColor[member.statusTone] }}
                          />
                        </div>
                        <div>
                          <h4 className="text-base font-semibold">{member.name}</h4>
                          <p className="text-sm text-[var(--foreground-soft)]">
                            {member.role}
                          </p>
                        </div>
                      </div>
                    </article>
                  ))}
                </div>
              </div>
            </section>

            <section>
              <div className="glass-panel rounded-[1.8rem] p-5">
                <h3 className="text-2xl font-semibold">Weekly Emotional Flow</h3>
                <div className="mt-6 space-y-7">
                  {selectedCommunity.weeklyFlow.map((series) => (
                    <div key={series.id}>
                      <div className="mb-3 flex items-center gap-3">
                        <span
                          className="h-3.5 w-3.5 rounded-full"
                          style={{ backgroundColor: `var(${series.colorVar})` }}
                        />
                        <span className="text-base font-medium">{series.label}</span>
                      </div>
                      <div className="grid grid-cols-7 gap-2">
                        {series.values.map((value, index) => (
                          <div key={`${series.id}-${weekLabels[index]}`} className="text-center">
                            <div className="flex h-14 items-end justify-center">
                              <div
                                className="rounded-full shadow-[0_0_30px_rgba(255,255,255,0.48)]"
                                style={{
                                  backgroundColor: `var(${series.colorVar})`,
                                  width: `${Math.max(1.9, value / 24)}rem`,
                                  height: `${Math.max(1.9, value / 24)}rem`,
                                  opacity: 0.92,
                                }}
                              />
                            </div>
                            <span className="mt-2 block text-xs text-[var(--foreground-soft)]">
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
          </div>
      </motion.aside>
    </main>
  );
}
