import { DashboardShell } from "@/components/dashboard/dashboard-shell";
import {
  createSupabaseServerClient,
  hasSupabasePublicEnv,
} from "@/lib/supabase/server";

export default async function Home() {
  const supabaseReady = hasSupabasePublicEnv();
  const supabase = supabaseReady ? createSupabaseServerClient() : null;

  void supabase;

  return <DashboardShell supabaseReady={supabaseReady} />;
}
