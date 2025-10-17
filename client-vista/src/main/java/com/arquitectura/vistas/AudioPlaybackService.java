package com.arquitectura.vistas;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

@Service
public class AudioPlaybackService {

    public void playAudio(String filePath) {
        try {
            File audioFile = new File(filePath);
            if (audioFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } else {
                System.err.println("Error de reproducción: Archivo no encontrado en " + filePath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error al reproducir audio: " + e.getMessage());
            e.printStackTrace();
        }
    }
}