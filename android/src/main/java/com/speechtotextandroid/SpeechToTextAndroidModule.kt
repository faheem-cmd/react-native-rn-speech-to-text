package com.speechtotextandroid

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

class SpeechToTextAndroidModule(
  private val reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

  private var speechRecognizer: SpeechRecognizer? = null
  private val mainHandler = Handler(Looper.getMainLooper())
  private var isListening = false

  override fun getName(): String = "SpeechToTextAndroid"

  private fun sendEvent(event: String, data: String?) {
    if (!reactContext.hasActiveCatalystInstance()) return

    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(event, data)
  }

  private fun createRecognizerIfNeeded() {
    if (speechRecognizer == null) {
      speechRecognizer = SpeechRecognizer.createSpeechRecognizer(reactContext)

      speechRecognizer?.setRecognitionListener(
        object : android.speech.RecognitionListener {

          override fun onReadyForSpeech(params: android.os.Bundle?) {
            sendEvent("onSpeechStart", "Listening...")
          }

          override fun onResults(results: android.os.Bundle?) {
            val matches =
              results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            sendEvent("onSpeechResult", matches?.get(0))
          }

          override fun onError(error: Int) {
            sendEvent("onSpeechError", error.toString())
          }

          override fun onBeginningOfSpeech() {}
          override fun onRmsChanged(rmsdB: Float) {}
          override fun onBufferReceived(buffer: ByteArray?) {}
          override fun onEndOfSpeech() {}
          override fun onPartialResults(partialResults: android.os.Bundle?) {}
          override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
        }
      )
    }
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

      speechRecognizer?.startListening(intent)
      isListening = true
    }
  }

  @ReactMethod
  fun stopListening() {
    mainHandler.post {
      speechRecognizer?.stopListening()
      isListening = false
    }
  }

  override fun onCatalystInstanceDestroy() {
    speechRecognizer?.destroy()
    speechRecognizer = null
  }
}