package com.arquitectura.entidades;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // All message types will be in one table
@DiscriminatorColumn(name = "message_type") // This column will store the type (e.g., "TEXT" or "AUDIO")
public abstract class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne // A message belongs to one user
    @JoinColumn(name = "author_id") // The foreign key column in the 'messages' table
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