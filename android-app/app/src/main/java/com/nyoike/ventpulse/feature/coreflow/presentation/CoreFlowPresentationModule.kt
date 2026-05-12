package com.nyoike.ventpulse.feature.coreflow.presentation

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val coreFlowPresentationModule = module {
    viewModelOf(::CoreFlowViewModel)
}
