# react-native-rn-speech-to-text

react-native-rn-speech-to-text is a React Native library that enables real-time voice-to-text conversion on Android devices using the native SpeechRecognizer API. It provides simple methods to start and stop listening, along with event listeners for speech results, errors, and recognition state changes.

## Installation

```sh
npm install react-native-rn-speech-to-text
```

## Note: For Lower Android Versions (Below Android 12)

Add the following inside your AndroidManifest.xml:

```sh
<manifest ...>
    <queries>
        <intent>
            <action android:name="android.speech.RecognitionService" />
        </intent>
    </queries>
</manifest>
```

## Usage

```js
import React, { useEffect, useState } from 'react';
import {
  View,
  Button,
  Text,
  PermissionsAndroid,
  Platform,
  StyleSheet,
} from 'react-native';

import {
  startListening,
  stopListening,
  addSpeechResultListener,
  addSpeechErrorListener,
  addSpeechStartListener,
} from 'react-native-rn-speech-to-text';

export default function App() {
  const [text, setText] = useState('');
  const [status, setStatus] = useState('Idle');

  // 🔐 Request Microphone Permission
  const requestMicPermission = async (): Promise<boolean> => {
    if (Platform.OS === 'android') {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.RECORD_AUDIO
      );

      return granted === PermissionsAndroid.RESULTS.GRANTED;
    }
    return false;
  };

  useEffect(() => {
    const resultSub = addSpeechResultListener((speechText) => {
      setText(speechText);
      setStatus('Result received');
    });

    const errorSub = addSpeechErrorListener((error) => {
      setStatus(`Error: ${error}`);
    });

    const startSub = addSpeechStartListener(() => {
      setStatus('Listening...');
    });

    return () => {
      resultSub.remove();
      errorSub.remove();
      startSub.remove();
    };
  }, []);

  const handleStart = async () => {
    const hasPermission = await requestMicPermission();
    if (!hasPermission) {
      setStatus('Microphone permission denied');
      return;
    }

    startListening();
  };

  return (
    <View style={styles.container}>
      <Text style={styles.status}>{status}</Text>
      <Text style={styles.result}>{text}</Text>

      <Button title="Start Listening" onPress={handleStart} />
      <View style={{ height: 10 }} />
      <Button title="Stop Listening" onPress={stopListening} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    padding: 20,
  },
  status: {
    fontSize: 16,
    marginBottom: 10,
  },
  result: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 20,
  },
});
```

## Contributing

- [Development workflow](CONTRIBUTING.md#development-workflow)
- [Sending a pull request](CONTRIBUTING.md#sending-a-pull-request)
- [Code of conduct](CODE_OF_CONDUCT.md)

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
u
# react-native-rn-speech-to-text
