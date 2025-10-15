package com.arquitectura.entidades;

public class AudioMessage extends Message {
    private String audioUrl;

    public AudioMessage() { super(); }

    public AudioMessage(User author, boolean isOwnMessage, String audioUrl) {
        super(author, isOwnMessage);
        this.audioUrl = audioUrl;
    }

    // --- Getters y Setters ---
    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }
}