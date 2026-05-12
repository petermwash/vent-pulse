package com.nyoike.ventpulse.feature.matching.presentation

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val matchingPresentationModule = module {
    viewModelOf(::MatchingViewModel)
}
