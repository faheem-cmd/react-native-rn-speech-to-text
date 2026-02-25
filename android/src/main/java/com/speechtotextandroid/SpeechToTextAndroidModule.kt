package com.speechtotextandroid

import com.facebook.react.bridge.ReactApplicationContext

class SpeechToTextAndroidModule(reactContext: ReactApplicationContext) :
  NativeSpeechToTextAndroidSpec(reactContext) {

  override fun multiply(a: Double, b: Double): Double {
    return a * b
  }

  companion object {
    const val NAME = NativeSpeechToTextAndroidSpec.NAME
  }
}
