package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.FirebaseService
import com.example.data.VoiceAnnouncer
import com.example.model.EngineMode
import com.example.ui.components.BeastEngineSwitcher
import com.example.ui.education.EducationEngineScreen
import com.example.ui.theme.BeastThemePreset
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.TheBeastTheme
import com.example.ui.trading.TradingTerminalScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var voiceAnnouncer: VoiceAnnouncer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        voiceAnnouncer = VoiceAnnouncer(this)

        setContent {
            var currentEngineMode by remember { mutableStateOf(EngineMode.TRADING) }
            var currentThemePreset by remember { mutableStateOf(BeastThemePreset.CYBER_BLUE) }
            val coroutineScope = rememberCoroutineScope()

            // Firebase background sign-in / verification
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    FirebaseService.signInAnonymouslyOrCheck()
                }
            }

            TheBeastTheme(preset = currentThemePreset) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DarkBackground,
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        BeastEngineSwitcher(
                            currentMode = currentEngineMode,
                            onModeChange = { mode ->
                                currentEngineMode = mode
                                if (mode == EngineMode.EDUCATION) {
                                    voiceAnnouncer?.speak("Education engine loaded. 42 curriculum questions ready.")
                                } else {
                                    voiceAnnouncer?.speak("EA Nexus trading terminal ready.")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(DarkBackground)
                    ) {
                        when (currentEngineMode) {
                            EngineMode.EDUCATION -> {
                                EducationEngineScreen(voiceAnnouncer = voiceAnnouncer)
                            }
                            EngineMode.TRADING -> {
                                TradingTerminalScreen(
                                    currentThemePreset = currentThemePreset,
                                    onThemeChange = { newPreset ->
                                        currentThemePreset = newPreset
                                    },
                                    voiceAnnouncer = voiceAnnouncer
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceAnnouncer?.shutdown()
        voiceAnnouncer = null
    }
}
