import { DashboardShell } from "@/components/dashboard/dashboard-shell";
import { loadDashboardData } from "@/components/dashboard/dashboard-supabase-data";

export const dynamic = "force-dynamic";

export default async function Home() {
  const dashboardData = await loadDashboardData();

  return (
    <DashboardShell
      communities={dashboardData.communities}
      dataMode={dashboardData.mode}
      dataWarnings={dashboardData.warnings}
    />
  );
}
