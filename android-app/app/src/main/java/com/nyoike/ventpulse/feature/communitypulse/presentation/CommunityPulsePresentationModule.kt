package com.nyoike.ventpulse.feature.communitypulse.presentation

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val communityPulsePresentationModule = module {
    viewModelOf(::CommunityPulseViewModel)
}
