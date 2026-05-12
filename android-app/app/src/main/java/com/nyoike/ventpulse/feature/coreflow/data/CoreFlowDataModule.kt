package com.nyoike.ventpulse.feature.coreflow.data

import com.nyoike.ventpulse.feature.coreflow.domain.CoreFlowRepository
import org.koin.dsl.module

val coreFlowDataModule = module {
    single<CoreFlowRepository> {
        OfflineFirstCoreFlowRepository(
            communityDao = get(),
            coreFlowDao = get(),
            supabaseClientProvider = get()
        )
    }
}
