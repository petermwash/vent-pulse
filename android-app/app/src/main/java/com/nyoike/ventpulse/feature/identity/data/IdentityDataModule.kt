package com.nyoike.ventpulse.feature.identity.data

import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentityRepository
import org.koin.dsl.module

val identityDataModule = module {
    single { AnonymousIdentityGenerator() }
    single<AnonymousIdentityRepository> {
        DataStoreAnonymousIdentityRepository(
            sessionPreferences = get(),
            generator = get()
        )
    }
}
