package com.arquitectura.dto;

import java.time.LocalDateTime;

public class MessageViewDTO {
    private final int id;
    private final String content;
    // This is the critical change
    private final UserViewDTO author; 
    private final LocalDateTime timestamp;
    private final boolean isOwnMessage;
    private final int channelId;

    public MessageViewDTO(int id, String content, UserViewDTO author, LocalDateTime timestamp, boolean isOwnMessage, int channelId) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.isOwnMessage = isOwnMessage;
        this.channelId = channelId;
    }

    // Getters
    public int getId() { return id; }
    public String getContent() { return content; }
    // Update the getter
    public UserViewDTO getAuthor() { return author; } 
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isOwnMessage() { return isOwnMessage; }
    public int getChannelId() { return channelId; }
    
    // You'll now get the name like this: getAuthor().getUsername()
    public String getAuthorName() {
        return author != null ? author.getUsername() : "Unknown";
    }
}