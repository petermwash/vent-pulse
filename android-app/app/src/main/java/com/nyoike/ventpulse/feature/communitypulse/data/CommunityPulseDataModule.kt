package com.nyoike.ventpulse.feature.communitypulse.data

import com.nyoike.ventpulse.feature.communitypulse.domain.CommunityPulseRepository
import org.koin.dsl.module

val communityPulseDataModule = module {
    single<CommunityPulseRepository> {
        SupabaseCommunityPulseRepository(supabaseClientProvider = get())
    }
}
