import { createClient } from "@supabase/supabase-js";

import { getSupabasePublicEnv, hasSupabasePublicEnv } from "@/lib/supabase/env";

export function createSupabaseServerClient() {
  const { url, publishableKey } = getSupabasePublicEnv();

  return createClient(url, publishableKey, {
    auth: {
      autoRefreshToken: false,
      detectSessionInUrl: false,
      persistSession: false,
    },
    global: {
      fetch,
    },
  });
}

export { hasSupabasePublicEnv };
