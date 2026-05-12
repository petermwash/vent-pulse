package com.nyoike.ventpulse.core.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.nyoike.ventpulse.BuildConfig
import com.nyoike.ventpulse.core.data.database.VentPulseDatabase
import com.nyoike.ventpulse.core.data.session.SessionPreferences
import com.nyoike.ventpulse.core.data.supabase.SupabaseClientProvider
import com.nyoike.ventpulse.core.data.supabase.SupabaseClientFactory
import com.nyoike.ventpulse.core.data.supabase.SupabaseConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        SupabaseConfig(
            url = BuildConfig.SUPABASE_URL,
            publishableKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
        )
    }
    single { SupabaseClientProvider(SupabaseClientFactory.create(get())) }
    single {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile("ventpulse_session")
        }
    }
    single { SessionPreferences(get()) }
    single {
        Room.databaseBuilder(
            androidContext(),
            VentPulseDatabase::class.java,
            "ventpulse.db"
        ).build()
    }
    single { get<VentPulseDatabase>().communityDao() }
}
