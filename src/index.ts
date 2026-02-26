import { NativeModules, NativeEventEmitter } from 'react-native';
import type { EmitterSubscription } from 'react-native';

interface SpeechToTextAndroidNativeModule {
  startListening(): void;
  stopListening(): void;

  // Required for NativeEventEmitter typing
  addListener(eventName: string): void;
  removeListeners(count: number): void;
}

const NativeModule =
  NativeModules.SpeechToTextAndroid as SpeechToTextAndroidNativeModule;

if (!NativeModule) {
  throw new Error('SpeechToTextAndroid native module is not linked properly.');
}

const eventEmitter = new NativeEventEmitter(NativeModules.SpeechToTextAndroid);

export const startListening = (): void => {
  NativeModule.startListening();
};

export const stopListening = (): void => {
  NativeModule.stopListening();
};

export const addSpeechResultListener = (
  callback: (text: string) => void
): EmitterSubscription =>
  eventEmitter.addListener('onSpeechResult', (data: unknown) =>
    callback(String(data))
  );

export const addSpeechErrorListener = (
  callback: (error: string) => void
): EmitterSubscription =>
  eventEmitter.addListener('onSpeechError', (data: unknown) =>
    callback(String(data))
  );

export const addSpeechStartListener = (
  callback: (msg: string) => void
): EmitterSubscription =>
  eventEmitter.addListener('onSpeechStart', (data: unknown) =>
    callback(String(data))
  );
