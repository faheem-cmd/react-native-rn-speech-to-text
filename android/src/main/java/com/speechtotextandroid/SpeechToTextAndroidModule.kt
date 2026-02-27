package com.speechtotextandroid

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.facebook.react.bridge.*

class SpeechToTextAndroidModule(
    private val reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    private var speechRecognizer: SpeechRecognizer? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    private var isListening = false
    private var continuousMode = false

    override fun getName(): String = "SpeechToTextAndroid"

    private fun createRecognizerIfNeeded() {
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(reactContext)
            speechRecognizer?.setRecognitionListener(
                SpeechRecognitionListener(reactContext, this)
            )
        }
    }

    @ReactMethod
    fun setContinuousMode(value: Boolean) {
        continuousMode = value
    }

    @ReactMethod
    fun startListening() {
        mainHandler.post {

            if (!SpeechRecognizer.isRecognitionAvailable(reactContext)) return@post
            if (isListening) return@post

            createRecognizerIfNeeded()

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)

            speechRecognizer?.startListening(intent)
            isListening = true
        }
    }

    @ReactMethod
    fun stopListening() {
        mainHandler.post {
            continuousMode = false   // 🔥 Stop continuous mode
            speechRecognizer?.stopListening()
            isListening = false
        }
    }

    // 🔥 Called when speech ends
    fun stopListeningInternal() {
        mainHandler.post {
            speechRecognizer?.stopListening()
            isListening = false

            // 🔥 Restart automatically if continuous mode enabled
            if (continuousMode) {
                mainHandler.postDelayed({
                    startListening()
                }, 500) // small delay like Google
            }
        }
    }

    fun resetState() {
        isListening = false
    }

    override fun onCatalystInstanceDestroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }
}
