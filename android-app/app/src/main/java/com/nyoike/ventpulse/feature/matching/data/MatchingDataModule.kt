package com.nyoike.ventpulse.feature.matching.data

import com.nyoike.ventpulse.feature.matching.domain.MatchingRepository
import org.koin.dsl.module

val matchingDataModule = module {
    single<MatchingRepository> {
        SupabaseMatchingRepository(supabaseClientProvider = get())
    }
}
