import { createClient } from "@supabase/supabase-js";

import { getSupabasePublicEnv, hasSupabasePublicEnv } from "@/lib/supabase/env";

export function createSupabaseBrowserClient() {
  const { url, publishableKey } = getSupabasePublicEnv();

  return createClient(url, publishableKey, {
    auth: {
      autoRefreshToken: false,
      detectSessionInUrl: false,
      persistSession: false,
    },
  });
}

export function canCreateSupabaseBrowserClient(): boolean {
  return hasSupabasePublicEnv();
}
