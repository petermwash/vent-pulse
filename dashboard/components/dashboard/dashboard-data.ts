export type NavItem = {
  id: string;
  label: string;
};

export type CommunityOption = {
  id: string;
  name: string;
  parentLabel: string;
  headingLabel: string;
  subtitle: string;
  signalTone: string;
  depth: number;
  databaseId?: string;
};

export type BubbleDatum = {
  id: string;
  label: string;
  value: number;
  x: number;
  y: number;
  size: number;
  colorVar: string;
  glow: string;
  velocityX: number;
  velocityY: number;
  opacity?: number;
};

export type InsightCard = {
  id: string;
  title: string;
  body: string;
  trend: "up" | "down" | "flat";
};

export type Observation = {
  id: string;
  title: string;
  body: string;
};

export type SupportMember = {
  id: string;
  name: string;
  role: string;
  statusTone: "calm" | "busy" | "warm";
};

export type WeeklyFlowSeries = {
  id: string;
  label: string;
  colorVar: string;
  values: number[];
};

export type FlaggedVent = {
  id: string;
  body: string;
  mood: string;
  reportCount: number;
  latestReportedAt: string;
  moderationStatus: "visible" | "under_review" | "hidden";
};

export type CommunityExperience = CommunityOption & {
  insights: InsightCard[];
  observations: Observation[];
  supportMembers: SupportMember[];
  weeklyFlow: WeeklyFlowSeries[];
  bubbles: BubbleDatum[];
  flaggedVents: FlaggedVent[];
};

export const navItems: NavItem[] = [
  { id: "atmosphere", label: "Atmosphere" },
  { id: "communities", label: "Communities" },
  { id: "insights", label: "Insights" },
  { id: "support", label: "Support" },
  { id: "settings", label: "Settings" },
];

const sharedSupportMembers: SupportMember[] = [
  {
    id: "amina",
    name: "Dr. Amina Odhiambo",
    role: "Clinical Psychologist",
    statusTone: "calm",
  },
  {
    id: "james",
    name: "James Kamau",
    role: "Community Counselor",
    statusTone: "calm",
  },
  {
    id: "sarah",
    name: "Sarah Njeri",
    role: "Peer Support Specialist",
    statusTone: "warm",
  },
  {
    id: "david",
    name: "David Otieno",
    role: "Crisis Intervention",
    statusTone: "busy",
  },
];

const sharedWeeklyFlow: WeeklyFlowSeries[] = [
  {
    id: "joy",
    label: "Joy",
    colorVar: "--emotion-joy",
    values: [44, 48, 43, 45, 55, 60, 58],
  },
  {
    id: "calm",
    label: "Calm",
    colorVar: "--emotion-calm",
    values: [58, 54, 57, 61, 53, 59, 63],
  },
  {
    id: "anxiety",
    label: "Anxiety",
    colorVar: "--emotion-anxiety",
    values: [36, 34, 35, 38, 37, 40, 39],
  },
  {
    id: "frustration",
    label: "Frustration",
    colorVar: "--emotion-frustration",
    values: [28, 30, 29, 32, 31, 33, 34],
  },
];

const brooksideWeeklyFlow: WeeklyFlowSeries[] = [
  {
    id: "joy",
    label: "Joy",
    colorVar: "--emotion-joy",
    values: [28, 30, 27, 29, 34, 38, 36],
  },
  {
    id: "calm",
    label: "Calm",
    colorVar: "--emotion-calm",
    values: [42, 39, 41, 44, 40, 45, 47],
  },
  {
    id: "anxiety",
    label: "Anxiety",
    colorVar: "--emotion-anxiety",
    values: [18, 17, 16, 19, 18, 21, 20],
  },
  {
    id: "frustration",
    label: "Frustration",
    colorVar: "--emotion-frustration",
    values: [14, 15, 16, 17, 15, 18, 17],
  },
];

const nairobiBubbles: BubbleDatum[] = [
  {
    id: "calm-primary",
    label: "Calm",
    value: 342,
    x: 0.37,
    y: 0.31,
    size: 0.18,
    colorVar: "--emotion-calm",
    glow: "rgba(142, 227, 196, 0.42)",
    velocityX: 0.14,
    velocityY: -0.1,
  },
  {
    id: "joy-primary",
    label: "Joy",
    value: 234,
    x: 0.49,
    y: 0.18,
    size: 0.14,
    colorVar: "--emotion-joy",
    glow: "rgba(255, 209, 102, 0.44)",
    velocityX: -0.08,
    velocityY: 0.11,
  },
  {
    id: "joy-secondary",
    label: "Joy",
    value: 198,
    x: 0.56,
    y: 0.26,
    size: 0.15,
    colorVar: "--emotion-joy",
    glow: "rgba(255, 209, 102, 0.4)",
    velocityX: 0.05,
    velocityY: -0.04,
  },
  {
    id: "anxiety-main",
    label: "Anxiety",
    value: 289,
    x: 0.5,
    y: 0.53,
    size: 0.18,
    colorVar: "--emotion-anxiety",
    glow: "rgba(200, 182, 255, 0.38)",
    velocityX: 0.08,
    velocityY: 0.05,
  },
  {
    id: "frustration",
    label: "Frustration",
    value: 167,
    x: 0.3,
    y: 0.69,
    size: 0.13,
    colorVar: "--emotion-frustration",
    glow: "rgba(255, 155, 133, 0.34)",
    velocityX: 0.03,
    velocityY: 0.14,
  },
  {
    id: "calm-secondary",
    label: "Calm",
    value: 221,
    x: 0.39,
    y: 0.72,
    size: 0.15,
    colorVar: "--emotion-calm",
    glow: "rgba(142, 227, 196, 0.3)",
    velocityX: -0.1,
    velocityY: -0.04,
  },
  {
    id: "sadness",
    label: "Sadness",
    value: 145,
    x: 0.16,
    y: 0.73,
    size: 0.11,
    colorVar: "--emotion-sadness",
    glow: "rgba(160, 169, 255, 0.22)",
    velocityX: 0.04,
    velocityY: -0.03,
    opacity: 0.62,
  },
  {
    id: "loneliness",
    label: "Loneliness",
    value: 98,
    x: 0.51,
    y: 0.72,
    size: 0.1,
    colorVar: "--emotion-loneliness",
    glow: "rgba(181, 192, 208, 0.2)",
    velocityX: -0.03,
    velocityY: 0.02,
    opacity: 0.76,
  },
  {
    id: "anxiety-soft",
    label: "Anxiety",
    value: 156,
    x: 0.24,
    y: 0.76,
    size: 0.1,
    colorVar: "--emotion-anxiety",
    glow: "rgba(200, 182, 255, 0.18)",
    velocityX: 0.02,
    velocityY: 0.02,
    opacity: 0.52,
  },
];

const westlandsBubbles: BubbleDatum[] = [
  {
    id: "calm-primary",
    label: "Calm",
    value: 266,
    x: 0.36,
    y: 0.34,
    size: 0.18,
    colorVar: "--emotion-calm",
    glow: "rgba(142, 227, 196, 0.42)",
    velocityX: 0.16,
    velocityY: -0.11,
  },
  {
    id: "joy-primary",
    label: "Joy",
    value: 202,
    x: 0.49,
    y: 0.19,
    size: 0.14,
    colorVar: "--emotion-joy",
    glow: "rgba(255, 209, 102, 0.42)",
    velocityX: -0.05,
    velocityY: 0.1,
  },
  {
    id: "anxiety-main",
    label: "Anxiety",
    value: 224,
    x: 0.5,
    y: 0.56,
    size: 0.16,
    colorVar: "--emotion-anxiety",
    glow: "rgba(200, 182, 255, 0.34)",
    velocityX: 0.09,
    velocityY: 0.05,
  },
  {
    id: "frustration",
    label: "Frustration",
    value: 143,
    x: 0.16,
    y: 0.68,
    size: 0.12,
    colorVar: "--emotion-frustration",
    glow: "rgba(255, 155, 133, 0.28)",
    velocityX: 0.03,
    velocityY: 0.13,
  },
  {
    id: "calm-secondary",
    label: "Calm",
    value: 186,
    x: 0.39,
    y: 0.71,
    size: 0.14,
    colorVar: "--emotion-calm",
    glow: "rgba(142, 227, 196, 0.28)",
    velocityX: -0.08,
    velocityY: -0.03,
  },
  {
    id: "loneliness",
    label: "Loneliness",
    value: 71,
    x: 0.51,
    y: 0.72,
    size: 0.09,
    colorVar: "--emotion-loneliness",
    glow: "rgba(181, 192, 208, 0.18)",
    velocityX: -0.02,
    velocityY: 0.02,
    opacity: 0.74,
  },
];

const brooksideBubbles: BubbleDatum[] = [
  {
    id: "calm-primary",
    label: "Calm",
    value: 189,
    x: 0.36,
    y: 0.39,
    size: 0.18,
    colorVar: "--emotion-calm",
    glow: "rgba(142, 227, 196, 0.42)",
    velocityX: 0.12,
    velocityY: -0.08,
  },
  {
    id: "joy-primary",
    label: "Joy",
    value: 156,
    x: 0.48,
    y: 0.2,
    size: 0.16,
    colorVar: "--emotion-joy",
    glow: "rgba(255, 209, 102, 0.42)",
    velocityX: -0.05,
    velocityY: 0.09,
  },
  {
    id: "calm-secondary",
    label: "Calm",
    value: 118,
    x: 0.16,
    y: 0.62,
    size: 0.14,
    colorVar: "--emotion-calm",
    glow: "rgba(142, 227, 196, 0.22)",
    velocityX: 0.06,
    velocityY: -0.03,
    opacity: 0.6,
  },
  {
    id: "anxiety-main",
    label: "Anxiety",
    value: 87,
    x: 0.44,
    y: 0.66,
    size: 0.1,
    colorVar: "--emotion-anxiety",
    glow: "rgba(200, 182, 255, 0.18)",
    velocityX: 0.04,
    velocityY: 0.03,
    opacity: 0.52,
  },
  {
    id: "loneliness",
    label: "Loneliness",
    value: 45,
    x: 0.5,
    y: 0.72,
    size: 0.1,
    colorVar: "--emotion-loneliness",
    glow: "rgba(181, 192, 208, 0.18)",
    velocityX: -0.03,
    velocityY: 0.02,
    opacity: 0.82,
  },
  {
    id: "sadness",
    label: "Sadness",
    value: 72,
    x: 0.16,
    y: 0.76,
    size: 0.1,
    colorVar: "--emotion-sadness",
    glow: "rgba(160, 169, 255, 0.18)",
    velocityX: 0.03,
    velocityY: -0.03,
    opacity: 0.54,
  },
];

export const communityExperiences: CommunityExperience[] = [
  {
    id: "nairobi",
    name: "Nairobi",
    parentLabel: "Kenya",
    headingLabel: "Nairobi, Kenya",
    subtitle: "Live emotional atmosphere",
    signalTone:
      "Broad calm remains dominant while mobility-linked stress gathers around evening movement corridors.",
    depth: 1,
    bubbles: nairobiBubbles,
    insights: [
      {
        id: "westlands",
        title: "Westlands Neighborhood",
        body: "Emotional atmosphere remains calm this week. Community members report feeling stable and connected.",
        trend: "flat",
      },
      {
        id: "transport",
        title: "Transportation Concerns",
        body: "Increasing frustration detected around transportation delays and traffic congestion during peak hours.",
        trend: "up",
      },
      {
        id: "weekend",
        title: "Weekend Community Events",
        body: "Joy levels elevated during weekend social gatherings and family activities across the region.",
        trend: "down",
      },
    ],
    observations: [
      {
        id: "observation-1",
        title: "Pressure is diffuse, not acute",
        body: "Current signals suggest broad emotional steadiness with a few mobility-linked friction points rather than a single high-risk hotspot.",
      },
      {
        id: "observation-2",
        title: "Calm networks are carrying resilience",
        body: "Peer support language appears to be buffering stress spikes, especially in smaller neighborhood clusters.",
      },
    ],
    supportMembers: sharedSupportMembers,
    weeklyFlow: sharedWeeklyFlow,
    flaggedVents: [
      {
        id: "demo-flagged-transport",
        body: "Traffic has been unbearable this week and people are starting to snap at each other.",
        mood: "frustration",
        reportCount: 3,
        latestReportedAt: "Today, 6:20 PM",
        moderationStatus: "under_review",
      },
    ],
  },
  {
    id: "westlands",
    name: "Westlands",
    parentLabel: "Nairobi, Kenya",
    headingLabel: "Westlands, Nairobi",
    subtitle: "Neighborhood atmosphere",
    signalTone:
      "Connection stays high, but commuter friction rises earlier here than the broader city baseline.",
    depth: 2,
    bubbles: westlandsBubbles,
    insights: [
      {
        id: "late-commute",
        title: "Evening Commute Friction",
        body: "Short frustration spikes continue to cluster around routes linking office corridors to residential blocks.",
        trend: "up",
      },
      {
        id: "social-buffer",
        title: "Strong Social Buffer",
        body: "Weekend and after-work meetups continue to restore calm quickly after short-lived pressure waves.",
        trend: "flat",
      },
      {
        id: "night-rhythm",
        title: "Night Rhythm Settles Earlier",
        body: "Community conversations show a faster return to calm after 9 PM than the citywide average.",
        trend: "down",
      },
    ],
    observations: [
      {
        id: "observation-1",
        title: "Stress remains mobile",
        body: "Signals suggest that pressure is tied to movement patterns and clears once residents return to familiar support networks.",
      },
      {
        id: "observation-2",
        title: "Recovery remains local",
        body: "Small cluster conversations are acting as a stabilizing force across the neighborhood.",
      },
    ],
    supportMembers: sharedSupportMembers,
    weeklyFlow: sharedWeeklyFlow,
    flaggedVents: [
      {
        id: "demo-flagged-westlands",
        body: "A tense commute thread is drawing repeated reports for harsh replies.",
        mood: "frustration",
        reportCount: 2,
        latestReportedAt: "Today, 5:45 PM",
        moderationStatus: "under_review",
      },
    ],
  },
  {
    id: "brookside",
    name: "Brookside",
    parentLabel: "Westlands, Nairobi",
    headingLabel: "Brookside, Westlands, Nairobi",
    subtitle: "Live emotional atmosphere",
    signalTone:
      "Calm remains the neighborhood anchor, with softer joy peaks and gentler drift than the wider city.",
    depth: 3,
    bubbles: brooksideBubbles,
    insights: [
      {
        id: "street-calm",
        title: "Localized Calm Holds",
        body: "Brookside conversations remain steady, with smaller emotional swings than the wider Westlands view.",
        trend: "flat",
      },
      {
        id: "support-uptake",
        title: "Support Uptake Is Immediate",
        body: "Residents appear to respond quickly to outreach, shortening the duration of anxiety pockets after stressful events.",
        trend: "down",
      },
      {
        id: "late-night-fatigue",
        title: "Late-Night Fatigue Persists",
        body: "A thin band of loneliness emerges later in the day, especially in quieter residential stretches.",
        trend: "up",
      },
    ],
    observations: [
      {
        id: "observation-1",
        title: "Recovery is visible in small clusters",
        body: "Signals show emotional recovery improving after targeted support outreach and peer check-ins within Brookside.",
      },
      {
        id: "observation-2",
        title: "Pressure remains manageable",
        body: "Observed stress stays soft and local rather than spreading outward into broader neighborhood sentiment.",
      },
    ],
    supportMembers: sharedSupportMembers.map((member) =>
      member.id === "james"
        ? { ...member, statusTone: "busy" }
        : member.id === "david"
          ? { ...member, statusTone: "busy" }
          : member,
    ),
    weeklyFlow: brooksideWeeklyFlow,
    flaggedVents: [
      {
        id: "demo-flagged-brookside",
        body: "A late-night loneliness post was reported so a moderator can check tone and safety.",
        mood: "loneliness",
        reportCount: 2,
        latestReportedAt: "Yesterday, 9:12 PM",
        moderationStatus: "under_review",
      },
    ],
  },
  {
    id: "mombasa",
    name: "Mombasa",
    parentLabel: "Kenya",
    headingLabel: "Mombasa, Kenya",
    subtitle: "Coastal atmosphere",
    signalTone:
      "Joy and calm remain elevated with slower evening drift and smaller pressure pockets across the coast.",
    depth: 1,
    bubbles: westlandsBubbles,
    insights: [
      {
        id: "coast-calm",
        title: "Coastal Calm Persists",
        body: "Calm and joy remain elevated through the evening, with fewer abrupt emotional shifts than inland regions.",
        trend: "flat",
      },
    ],
    observations: [
      {
        id: "observation-1",
        title: "Recovery remains steady",
        body: "Low-intensity support conversations continue to sustain a calm regional baseline.",
      },
    ],
    supportMembers: sharedSupportMembers,
    weeklyFlow: sharedWeeklyFlow,
    flaggedVents: [],
  },
  {
    id: "kisumu",
    name: "Kisumu",
    parentLabel: "Kenya",
    headingLabel: "Kisumu, Kenya",
    subtitle: "Regional atmosphere",
    signalTone:
      "Stable support signals continue, with modest loneliness pockets emerging across quieter zones.",
    depth: 1,
    bubbles: westlandsBubbles,
    insights: [
      {
        id: "quiet-zones",
        title: "Quiet Zones Need Gentle Outreach",
        body: "Muted loneliness signals appear in low-traffic areas, though they remain dispersed and manageable.",
        trend: "up",
      },
    ],
    observations: [
      {
        id: "observation-1",
        title: "Support language remains consistent",
        body: "Peer support phrasing appears frequently and is likely dampening isolation spikes.",
      },
    ],
    supportMembers: sharedSupportMembers,
    weeklyFlow: sharedWeeklyFlow,
    flaggedVents: [],
  },
  {
    id: "nakuru",
    name: "Nakuru",
    parentLabel: "Kenya",
    headingLabel: "Nakuru, Kenya",
    subtitle: "Regional atmosphere",
    signalTone:
      "The emotional field is balanced overall, with gentle oscillation between calm and fatigue through the week.",
    depth: 1,
    bubbles: westlandsBubbles,
    insights: [
      {
        id: "balanced-week",
        title: "Balanced Weekly Pattern",
        body: "No sharp pressure wave is visible; the region is moving through a comparatively even emotional rhythm.",
        trend: "flat",
      },
    ],
    observations: [
      {
        id: "observation-1",
        title: "Signals stay broad rather than concentrated",
        body: "Patterns indicate distributed emotional movement with no single neighborhood dominating the field.",
      },
    ],
    supportMembers: sharedSupportMembers,
    weeklyFlow: sharedWeeklyFlow,
    flaggedVents: [],
  },
];

export const communityOptions: CommunityOption[] = communityExperiences.map((community) => ({
  id: community.id,
  name: community.name,
  parentLabel: community.parentLabel,
  headingLabel: community.headingLabel,
  subtitle: community.subtitle,
  signalTone: community.signalTone,
  depth: community.depth,
  databaseId: community.databaseId,
}));

export const communityExperienceById = Object.fromEntries(
  communityExperiences.map((community) => [community.id, community]),
) as Record<string, CommunityExperience>;

export const defaultCommunityId = "nairobi";

export const weekLabels = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
