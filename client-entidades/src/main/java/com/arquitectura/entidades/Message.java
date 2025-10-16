package com.arquitectura.entidades;

import java.time.LocalDateTime;

// No jakarta.persistence imports
public abstract class Message {

    private int id;
    private User author;
    private LocalDateTime timestamp;
    private boolean isOwnMessage;

    public Message() {}

    public Message(User author, boolean isOwnMessage) {
        this.author = author;
        this.timestamp = LocalDateTime.now();
        this.isOwnMessage = isOwnMessage;
    }

    // --- Getters y Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public boolean isOwnMessage() { return isOwnMessage; }
    public void setOwnMessage(boolean ownMessage) { isOwnMessage = ownMessage; }
}