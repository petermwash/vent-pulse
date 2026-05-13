import { createSupabaseServerClient, hasSupabasePublicEnv } from "@/lib/supabase/server";

import {
  type BubbleDatum,
  type CommunityExperience,
  type FlaggedVent,
  type InsightCard,
  type Observation,
  type SupportMember,
  communityExperiences,
} from "@/components/dashboard/dashboard-data";

type DashboardDataMode = "supabase" | "demo";

export type DashboardDataResult = {
  communities: CommunityExperience[];
  mode: DashboardDataMode;
  warnings: string[];
};

type CommunityRow = {
  id: string;
  parent_id: string | null;
  name: string;
  path: string;
  level: string;
  description: string | null;
  sort_order: number;
};

type MoodRollupRow = {
  community_id: string;
  mood: string;
  checkin_count: number;
};

type ExpertProfileRow = {
  community_id: string | null;
  display_name: string;
  role: string;
  specialty: string | null;
  availability_status: string;
};

type DemoInsightRow = {
  community_id: string | null;
  insight_type: string;
  title: string;
  body: string;
  severity: string;
  sort_order: number;
};

type ReportCountRow = {
  vent_id: string;
  community_id: string;
  report_count: number;
  latest_reported_at: string | null;
};

type VentRow = {
  id: string;
  community_id: string;
  mood: string;
  body: string;
  moderation_status: "visible" | "under_review" | "hidden";
};

const knownCommunityIdsByName = new Map(
  communityExperiences.map((community) => [normalizeKey(community.name), community.id]),
);

const moodToBubbleLabel: Record<string, string> = {
  happy: "Joy",
  calm: "Calm",
  sad: "Sadness",
  angry: "Frustration",
  anxious: "Anxiety",
  lonely: "Loneliness",
};

export async function loadDashboardData(): Promise<DashboardDataResult> {
  const fallback = cloneCommunities(communityExperiences);
  const warnings: string[] = [];

  if (!hasSupabasePublicEnv()) {
    return {
      communities: fallback,
      mode: "demo",
      warnings: ["Supabase env is missing; using seeded dashboard demo data."],
    };
  }

  const supabase = createSupabaseServerClient();

  try {
    const [communitiesResult, expertsResult, insightsResult] = await Promise.all([
      supabase
        .from("communities")
        .select("id,parent_id,name,path,level,description,sort_order")
        .order("path", { ascending: true })
        .order("sort_order", { ascending: true }),
      supabase
        .from("expert_profiles")
        .select("community_id,display_name,role,specialty,availability_status"),
      supabase
        .from("demo_insights")
        .select("community_id,insight_type,title,body,severity,sort_order")
        .order("sort_order", { ascending: true }),
    ]);

    if (communitiesResult.error) warnings.push(communitiesResult.error.message);
    if (expertsResult.error) warnings.push(expertsResult.error.message);
    if (insightsResult.error) warnings.push(insightsResult.error.message);

    let communities = mergeCommunityRows(
      fallback,
      (communitiesResult.data ?? []) as CommunityRow[],
    );

    communities = mergeExpertRows(
      communities,
      (expertsResult.data ?? []) as ExpertProfileRow[],
    );
    communities = mergeInsightRows(
      communities,
      (insightsResult.data ?? []) as DemoInsightRow[],
    );

    const { data: authData, error: authError } = await supabase.auth.signInAnonymously();

    if (authError || !authData.user) {
      warnings.push(authError?.message ?? "Anonymous Supabase auth was unavailable.");
    } else {
      const [moodResult, reportCountsResult] = await Promise.all([
        supabase
          .from("community_mood_rollups")
          .select("community_id,mood,checkin_count"),
        supabase
          .from("vent_report_counts")
          .select("vent_id,community_id,report_count,latest_reported_at")
          .gt("report_count", 0)
          .order("latest_reported_at", { ascending: false, nullsFirst: false })
          .limit(12),
      ]);

      if (moodResult.error) warnings.push(moodResult.error.message);
      if (reportCountsResult.error) warnings.push(reportCountsResult.error.message);

      communities = mergeMoodRows(
        communities,
        (moodResult.data ?? []) as MoodRollupRow[],
      );

      const reportRows = (reportCountsResult.data ?? []) as ReportCountRow[];
      const ventIds = reportRows.map((row) => row.vent_id);

      if (ventIds.length > 0) {
        const ventsResult = await supabase
          .from("vents")
          .select("id,community_id,mood,body,moderation_status")
          .in("id", ventIds);

        if (ventsResult.error) warnings.push(ventsResult.error.message);

        communities = mergeFlaggedVentRows(
          communities,
          reportRows,
          (ventsResult.data ?? []) as VentRow[],
        );
      }
    }

    return {
      communities,
      mode: warnings.length > 0 ? "demo" : "supabase",
      warnings,
    };
  } catch (error) {
    return {
      communities: fallback,
      mode: "demo",
      warnings: [error instanceof Error ? error.message : "Unable to read Supabase data."],
    };
  }
}

function cloneCommunities(communities: CommunityExperience[]): CommunityExperience[] {
  return communities.map((community) => ({
    ...community,
    bubbles: community.bubbles.map((bubble) => ({ ...bubble })),
    insights: community.insights.map((insight) => ({ ...insight })),
    observations: community.observations.map((observation) => ({ ...observation })),
    supportMembers: community.supportMembers.map((member) => ({ ...member })),
    weeklyFlow: community.weeklyFlow.map((series) => ({
      ...series,
      values: [...series.values],
    })),
    flaggedVents: community.flaggedVents.map((vent) => ({ ...vent })),
  }));
}

function mergeCommunityRows(
  fallback: CommunityExperience[],
  rows: CommunityRow[],
): CommunityExperience[] {
  if (rows.length === 0) return fallback;

  const rowsById = new Map(rows.map((row) => [row.id, row]));
  const existingById = new Map(fallback.map((community) => [community.id, community]));
  const experiences = rows
    .filter((row) => row.level !== "country")
    .map((row) => {
      const knownId = knownCommunityIdsByName.get(normalizeKey(row.name));
      const base =
        (knownId ? existingById.get(knownId) : undefined) ??
        existingById.get("nairobi") ??
        fallback[0];

      return {
        ...base,
        id: knownId ?? normalizeKey(row.name),
        databaseId: row.id,
        name: row.name,
        parentLabel: parentLabelFor(row, rowsById),
        headingLabel: headingLabelFor(row),
        subtitle: subtitleFor(row.level),
        signalTone: row.description ?? base.signalTone,
        depth: depthFor(row.level),
      };
    });

  return experiences.length > 0 ? experiences : fallback;
}

function mergeMoodRows(
  communities: CommunityExperience[],
  rows: MoodRollupRow[],
): CommunityExperience[] {
  if (rows.length === 0) return communities;

  const countsByCommunity = new Map<string, Map<string, number>>();
  rows.forEach((row) => {
    const label = moodToBubbleLabel[row.mood];
    if (!label) return;

    const counts = countsByCommunity.get(row.community_id) ?? new Map<string, number>();
    counts.set(label, (counts.get(label) ?? 0) + row.checkin_count);
    countsByCommunity.set(row.community_id, counts);
  });

  return communities.map((community) => {
    if (!community.databaseId) return community;

    const counts = countsByCommunity.get(community.databaseId);
    if (!counts || counts.size === 0) return community;

    return {
      ...community,
      bubbles: community.bubbles.map((bubble): BubbleDatum => ({
        ...bubble,
        value: counts.get(bubble.label) ?? bubble.value,
      })),
    };
  });
}

function mergeExpertRows(
  communities: CommunityExperience[],
  rows: ExpertProfileRow[],
): CommunityExperience[] {
  if (rows.length === 0) return communities;

  const rowsByCommunity = groupBy(rows, (row) => row.community_id ?? "global");

  return communities.map((community) => {
    const rowsForCommunity = community.databaseId
      ? rowsByCommunity.get(community.databaseId)
      : undefined;

    if (!rowsForCommunity || rowsForCommunity.length === 0) return community;

    return {
      ...community,
      supportMembers: rowsForCommunity.map((row, index): SupportMember => ({
        id: `${community.id}-expert-${index}`,
        name: row.display_name,
        role: row.specialty ?? roleLabel(row.role),
        statusTone: availabilityTone(row.availability_status),
      })),
    };
  });
}

function mergeInsightRows(
  communities: CommunityExperience[],
  rows: DemoInsightRow[],
): CommunityExperience[] {
  if (rows.length === 0) return communities;

  const rowsByCommunity = groupBy(rows, (row) => row.community_id ?? "global");

  return communities.map((community) => {
    const rowsForCommunity = community.databaseId
      ? rowsByCommunity.get(community.databaseId)
      : undefined;

    if (!rowsForCommunity || rowsForCommunity.length === 0) return community;

    const insights = rowsForCommunity
      .filter((row) => row.insight_type !== "ai_observation")
      .map((row, index): InsightCard => ({
        id: `${community.id}-insight-${index}`,
        title: row.title,
        body: row.body,
        trend: trendForSeverity(row.severity),
      }));

    const observations = rowsForCommunity
      .filter((row) => row.insight_type === "ai_observation")
      .map((row, index): Observation => ({
        id: `${community.id}-observation-${index}`,
        title: row.title,
        body: row.body,
      }));

    return {
      ...community,
      insights: insights.length > 0 ? insights : community.insights,
      observations: observations.length > 0 ? observations : community.observations,
    };
  });
}

function mergeFlaggedVentRows(
  communities: CommunityExperience[],
  reportRows: ReportCountRow[],
  ventRows: VentRow[],
): CommunityExperience[] {
  if (reportRows.length === 0 || ventRows.length === 0) return communities;

  const reportsByVentId = new Map(reportRows.map((row) => [row.vent_id, row]));
  const ventsByCommunity = groupBy(ventRows, (row) => row.community_id);

  return communities.map((community) => {
    if (!community.databaseId) return community;

    const ventsForCommunity = ventsByCommunity.get(community.databaseId);
    if (!ventsForCommunity || ventsForCommunity.length === 0) return community;

    return {
      ...community,
      flaggedVents: ventsForCommunity.map((vent): FlaggedVent => {
        const report = reportsByVentId.get(vent.id);

        return {
          id: vent.id,
          body: vent.body,
          mood: moodToBubbleLabel[vent.mood] ?? vent.mood,
          reportCount: report?.report_count ?? 1,
          latestReportedAt: formatReportedAt(report?.latest_reported_at),
          moderationStatus: vent.moderation_status,
        };
      }),
    };
  });
}

function normalizeKey(value: string): string {
  return value.toLowerCase().replace(/[^a-z0-9]+/g, "-").replace(/(^-|-$)/g, "");
}

function parentLabelFor(row: CommunityRow, rowsById: Map<string, CommunityRow>): string {
  const parent = row.parent_id ? rowsById.get(row.parent_id) : undefined;
  return parent?.name ?? "Kenya";
}

function headingLabelFor(row: CommunityRow): string {
  return row.path
    .split("/")
    .slice(1)
    .reverse()
    .map((part) =>
      part
        .split("-")
        .map((word) => `${word.charAt(0).toUpperCase()}${word.slice(1)}`)
        .join(" "),
    )
    .join(", ");
}

function subtitleFor(level: string): string {
  if (level === "neighborhood" || level === "sub_neighborhood") {
    return "Live neighborhood atmosphere";
  }

  if (level === "local_community") {
    return "Local community atmosphere";
  }

  return "Live emotional atmosphere";
}

function depthFor(level: string): number {
  if (level === "city") return 1;
  if (level === "local_community") return 2;
  if (level === "neighborhood") return 3;
  if (level === "sub_neighborhood") return 4;
  return 1;
}

function roleLabel(role: string): string {
  return role
    .split("_")
    .map((word) => `${word.charAt(0).toUpperCase()}${word.slice(1)}`)
    .join(" ");
}

function availabilityTone(status: string): SupportMember["statusTone"] {
  if (status === "available") return "calm";
  if (status === "busy") return "warm";
  return "busy";
}

function trendForSeverity(severity: string): InsightCard["trend"] {
  if (severity === "watch" || severity === "urgent") return "up";
  if (severity === "calm") return "down";
  return "flat";
}

function formatReportedAt(value?: string | null): string {
  if (!value) return "Recently";

  return new Intl.DateTimeFormat("en", {
    month: "short",
    day: "numeric",
    hour: "numeric",
    minute: "2-digit",
  }).format(new Date(value));
}

function groupBy<T>(items: T[], keyFor: (item: T) => string): Map<string, T[]> {
  return items.reduce((groups, item) => {
    const key = keyFor(item);
    const group = groups.get(key) ?? [];
    group.push(item);
    groups.set(key, group);
    return groups;
  }, new Map<string, T[]>());
}
