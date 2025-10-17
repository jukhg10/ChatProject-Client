package com.arquitectura.vistas;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component // Le decimos a Spring que gestione esta clase
public class AudioRecordingService {

    // Formato de audio (16kHz, 16-bit, mono, little-endian) compatible con Vosk
    private static final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true, false);

    private TargetDataLine microphone;
    private File audioFile;
    private volatile boolean isRecording = false;

    public void startRecording() throws LineUnavailableException, IOException {
        if (isRecording) {
            return; // Ya está grabando
        }

        // 1. Define la información de la línea de audio desde el micrófono
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, FORMAT);
        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("El formato de audio 16kHz, 16-bit mono no es soportado.");
        }

        // 2. Obtiene la línea del micrófono
        microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(FORMAT);
        microphone.start(); // Empieza a capturar audio

        // 3. Crea un archivo temporal donde se guardará la grabación
        audioFile = File.createTempFile("tempaudio", ".wav");

        // 4. Inicia un nuevo hilo para que la grabación no bloquee la interfaz de usuario
        Thread recordingThread = new Thread(() -> {
            isRecording = true;
            AudioInputStream audioStream = new AudioInputStream(microphone);
            try {
                AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        recordingThread.setDaemon(true); // El hilo no impedirá que la app se cierre
        recordingThread.start();
        System.out.println("Grabación iniciada...");
    }

    public void stopRecording() {
        if (!isRecording) {
            return;
        }
        
        // Detiene la captura y libera el micrófono
        microphone.stop();
        microphone.close();
        isRecording = false;
        System.out.println("Grabación detenida. Archivo guardado en: " + audioFile.getAbsolutePath());
    }

    public boolean isRecording() {
        return isRecording;
    }

    public File getAudioFile() {
        return audioFile;
    }
}