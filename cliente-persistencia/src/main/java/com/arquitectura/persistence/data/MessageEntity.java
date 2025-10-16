package com.arquitectura.persistence.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "message_type")
public abstract class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;
    
    private LocalDateTime timestamp;
    private boolean isOwnMessage;

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public UserEntity getAuthor() { return author; }
    public void setAuthor(UserEntity author) { this.author = author; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public boolean isOwnMessage() { return isOwnMessage; }
    public void setOwnMessage(boolean ownMessage) { isOwnMessage = ownMessage; }
}