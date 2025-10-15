package com.arquitectura.entidades;

public class TextMessage extends Message {
    private String content;

    public TextMessage() { super(); }

    public TextMessage(User author, boolean isOwnMessage, String content) {
        super(author, isOwnMessage);
        this.content = content;
    }

    // --- Getters y Setters ---
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}