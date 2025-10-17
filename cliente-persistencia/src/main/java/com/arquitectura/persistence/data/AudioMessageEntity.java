package com.arquitectura.persistence.data;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AUDIO")
public class AudioMessageEntity extends MessageEntity {
    private String audioUrl;

    // --- Getters and Setters ---
    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }
    
}