export type NavItem = {
  id: string;
  label: string;
};

export type CommunityOption = {
  id: string;
  name: string;
  parentLabel: string;
  pathLabel: string;
  subtitle: string;
  signalTone: string;
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

export const navItems: NavItem[] = [
  { id: "atmosphere", label: "Atmosphere" },
  { id: "communities", label: "Communities" },
  { id: "insights", label: "Insights" },
  { id: "support", label: "Support" },
  { id: "settings", label: "Settings" },
];

export const communityOptions: CommunityOption[] = [
  {
    id: "nairobi",
    name: "Nairobi",
    parentLabel: "Kenya",
    pathLabel: "Nairobi, Kenya",
    subtitle: "Live emotional atmosphere",
    signalTone: "A broad calm baseline with pressure pockets around movement and evening fatigue.",
  },
  {
    id: "westlands",
    name: "Westlands",
    parentLabel: "Nairobi, Kenya",
    pathLabel: "Westlands, Nairobi",
    subtitle: "Neighborhood atmosphere",
    signalTone: "More energy, faster drift, and a visible commuter frustration pattern.",
  },
  {
    id: "brookside",
    name: "Brookside",
    parentLabel: "Westlands, Nairobi",
    pathLabel: "Brookside, Westlands",
    subtitle: "Micro-community atmosphere",
    signalTone: "Calm remains dominant, but social fatigue rises after dusk and commute windows.",
  },
  {
    id: "mombasa",
    name: "Mombasa",
    parentLabel: "Kenya",
    pathLabel: "Mombasa, Kenya",
    subtitle: "Coastal atmosphere",
    signalTone: "Joy and calm remain elevated with softer, slower movement through the evening.",
  },
  {
    id: "kisumu",
    name: "Kisumu",
    parentLabel: "Kenya",
    pathLabel: "Kisumu, Kenya",
    subtitle: "Regional atmosphere",
    signalTone: "Stable support signals with mild loneliness spikes across quieter zones.",
  },
];

export const bubbleData: BubbleDatum[] = [
  {
    id: "calm-primary",
    label: "Calm",
    value: 342,
    x: 0.43,
    y: 0.33,
    size: 0.18,
    colorVar: "--emotion-calm",
    glow: "rgba(142, 227, 196, 0.42)",
    velocityX: 0.15,
    velocityY: -0.1,
  },
  {
    id: "joy-primary",
    label: "Joy",
    value: 234,
    x: 0.66,
    y: 0.15,
    size: 0.13,
    colorVar: "--emotion-joy",
    glow: "rgba(255, 209, 102, 0.42)",
    velocityX: -0.08,
    velocityY: 0.12,
  },
  {
    id: "joy-secondary",
    label: "Joy",
    value: 198,
    x: 0.82,
    y: 0.24,
    size: 0.15,
    colorVar: "--emotion-joy",
    glow: "rgba(255, 209, 102, 0.38)",
    velocityX: 0.06,
    velocityY: -0.06,
  },
  {
    id: "anxiety-main",
    label: "Anxiety",
    value: 289,
    x: 0.73,
    y: 0.55,
    size: 0.18,
    colorVar: "--emotion-anxiety",
    glow: "rgba(200, 182, 255, 0.38)",
    velocityX: 0.1,
    velocityY: 0.08,
  },
  {
    id: "frustration",
    label: "Frustration",
    value: 167,
    x: 0.18,
    y: 0.7,
    size: 0.14,
    colorVar: "--emotion-frustration",
    glow: "rgba(255, 155, 133, 0.36)",
    velocityX: 0.03,
    velocityY: 0.16,
  },
  {
    id: "calm-secondary",
    label: "Calm",
    value: 221,
    x: 0.52,
    y: 0.84,
    size: 0.15,
    colorVar: "--emotion-calm",
    glow: "rgba(142, 227, 196, 0.32)",
    velocityX: -0.12,
    velocityY: -0.05,
  },
  {
    id: "sadness",
    label: "Sadness",
    value: 145,
    x: 0.06,
    y: 0.78,
    size: 0.12,
    colorVar: "--emotion-sadness",
    glow: "rgba(160, 169, 255, 0.28)",
    velocityX: 0.05,
    velocityY: -0.04,
    opacity: 0.7,
  },
  {
    id: "loneliness",
    label: "Loneliness",
    value: 98,
    x: 0.8,
    y: 0.82,
    size: 0.1,
    colorVar: "--emotion-loneliness",
    glow: "rgba(181, 192, 208, 0.2)",
    velocityX: -0.04,
    velocityY: 0.02,
    opacity: 0.76,
  },
  {
    id: "anxiety-soft",
    label: "Anxiety",
    value: 156,
    x: 0.28,
    y: 0.92,
    size: 0.1,
    colorVar: "--emotion-anxiety",
    glow: "rgba(200, 182, 255, 0.18)",
    velocityX: 0.02,
    velocityY: 0.03,
    opacity: 0.48,
  },
];

export const insightCards: InsightCard[] = [
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
];

export const aiObservations: Observation[] = [
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
];

export const supportMembers: SupportMember[] = [
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
    statusTone: "calm",
  },
];

export const weeklyFlow: WeeklyFlowSeries[] = [
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

export const weekLabels = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
