package com.nyoike.ventpulse.feature.identity.presentation

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val identityPresentationModule = module {
    viewModelOf(::IdentityBootstrapViewModel)
}
