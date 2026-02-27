package com.speechtotextandroid

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.modules.core.DeviceEventManagerModule

class SpeechRecognitionListener(
    private val reactContext: ReactApplicationContext,
    private val module: SpeechToTextAndroidModule
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

    override fun onBeginningOfSpeech() {}

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {
        module.stopListeningInternal()
    }

    override fun onError(error: Int) {
        sendEvent("onSpeechError", error.toString())
        module.resetState()
    }

    override fun onResults(results: Bundle?) {
        val matches =
            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        sendEvent("onSpeechResult", matches?.get(0))

        module.stopListeningInternal()
    }

    override fun onPartialResults(partialResults: Bundle?) {}

    override fun onEvent(eventType: Int, params: Bundle?) {}
}
