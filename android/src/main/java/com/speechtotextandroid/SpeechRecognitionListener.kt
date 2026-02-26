package com.speechtotextandroid

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.modules.core.DeviceEventManagerModule

class SpeechRecognitionListener(
    private val reactContext: ReactApplicationContext
) : RecognitionListener {

    private fun sendEvent(event: String, data: String?) {
        if (!reactContext.hasActiveCatalystInstance()) return

        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(event, data)
    }

    override fun onReadyForSpeech(params: Bundle?) {
        sendEvent("onSpeechStart", "Listening...")
    }

    override fun onBeginningOfSpeech() {
        sendEvent("onSpeechBeginning", null)
    }

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {
        sendEvent("onSpeechEnd", null)
    }

    override fun onError(error: Int) {
        sendEvent("onSpeechError", error.toString())
    }

    override fun onResults(results: Bundle?) {
        val matches =
            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        sendEvent("onSpeechResult", matches?.get(0))
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val matches =
            partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        sendEvent("onSpeechPartialResult", matches?.get(0))
    }

    override fun onEvent(eventType: Int, params: Bundle?) {}
}