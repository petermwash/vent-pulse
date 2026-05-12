package com.nyoike.ventpulse.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyoike.ventpulse.core.data.session.SessionPreferences
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val sessionPreferences: SessionPreferences
) : ViewModel() {

    fun completeOnboarding(onComplete: () -> Unit) {
        viewModelScope.launch {
            sessionPreferences.markOnboardingSeen()
            onComplete()
        }
    }
}
