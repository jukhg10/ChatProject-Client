package com.arquitectura.dto; // <-- FIX THIS LINE

public class MessageDTO {
    private int channelId;
    private String content;

    public MessageDTO(int channelId, String content) {
        this.channelId = channelId;
        this.content = content;
    }

    // --- Getters and Setters ---
    public int getChannelId() { return channelId; }
    public void setChannelId(int channelId) { this.channelId = channelId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}