package com.nyoike.ventpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nyoike.ventpulse.app.VentPulseApp
import com.nyoike.ventpulse.ui.theme.VentPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VentPulseTheme {
                VentPulseApp()
            }
        }
    }
}
