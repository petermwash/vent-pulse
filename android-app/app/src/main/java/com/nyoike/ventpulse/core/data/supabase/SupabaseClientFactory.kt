package com.nyoike.ventpulse.core.data.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClientFactory {
    fun create(config: SupabaseConfig): SupabaseClient? {
        if (!config.isConfigured) return null

        return createSupabaseClient(
            supabaseUrl = config.url,
            supabaseKey = config.publishableKey
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
        }
    }
}

data class SupabaseClientProvider(
    val client: SupabaseClient?
)
