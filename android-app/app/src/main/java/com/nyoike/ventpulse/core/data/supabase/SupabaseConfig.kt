package com.nyoike.ventpulse.core.data.supabase

data class SupabaseConfig(
    val url: String,
    val publishableKey: String
) {
    val isConfigured: Boolean
        get() = url.isNotBlank() && publishableKey.isNotBlank()
}
