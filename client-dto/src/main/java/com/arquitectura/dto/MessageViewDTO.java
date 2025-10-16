package com.arquitectura.dto;

import java.time.LocalDateTime;

/**
 * DTO para transportar la información de un mensaje a la capa de vista.
 */
public class MessageViewDTO {
    private final int id;
    private final String content;
    private final String authorName;
    private final LocalDateTime timestamp;
    private final boolean isOwnMessage;
    private final int channelId; // Add this field

    public MessageViewDTO(int id, String content, String authorName, LocalDateTime timestamp, boolean isOwnMessage, int channelId) {
        this.id = id;
        this.content = content;
        this.authorName = authorName;
        this.timestamp = timestamp;
        this.isOwnMessage = isOwnMessage;
        this.channelId = channelId; // Add this to the constructor
    }

    // Getters
    public int getId() { return id; }
    public String getContent() { return content; }
    public String getAuthorName() { return authorName; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isOwnMessage() { return isOwnMessage; }
    public int getChannelId() { return channelId; } // Add this getter
}