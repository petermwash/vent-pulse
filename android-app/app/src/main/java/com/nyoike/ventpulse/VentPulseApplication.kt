package com.nyoike.ventpulse

import android.app.Application
import com.nyoike.ventpulse.core.di.appModule
import com.nyoike.ventpulse.feature.communitypulse.data.communityPulseDataModule
import com.nyoike.ventpulse.feature.communitypulse.presentation.communityPulsePresentationModule
import com.nyoike.ventpulse.feature.coreflow.data.coreFlowDataModule
import com.nyoike.ventpulse.feature.coreflow.presentation.coreFlowPresentationModule
import com.nyoike.ventpulse.feature.identity.data.identityDataModule
import com.nyoike.ventpulse.feature.identity.presentation.identityPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VentPulseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VentPulseApplication)
            modules(
                appModule,
                communityPulseDataModule,
                communityPulsePresentationModule,
                coreFlowDataModule,
                coreFlowPresentationModule,
                identityDataModule,
                identityPresentationModule
            )
        }
    }
}
