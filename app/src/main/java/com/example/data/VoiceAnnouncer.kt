package com.example.data

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class VoiceAnnouncer(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    var isEnabled: Boolean = true
    var speechRate: Float = 1.05f
    var speechPitch: Float = 0.95f

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            tts?.setSpeechRate(speechRate)
            tts?.setPitch(speechPitch)
            isInitialized = true
        }
    }

    fun speak(text: String) {
        if (!isEnabled || !isInitialized) return
        tts?.setSpeechRate(speechRate)
        tts?.setPitch(speechPitch)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "beast_voice_announcement")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
