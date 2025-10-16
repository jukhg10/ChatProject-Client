package com.arquitectura.persistence.data;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TEXT")
public class TextMessageEntity extends MessageEntity {
    private String content;

    // --- Getters and Setters ---
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}