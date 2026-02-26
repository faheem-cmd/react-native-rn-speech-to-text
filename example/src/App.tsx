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
    const resultSub = addSpeechResultListener((speechText: any) => {
      setText(speechText);
      setStatus('Result received');
    });

    const errorSub = addSpeechErrorListener((error: any) => {});

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
