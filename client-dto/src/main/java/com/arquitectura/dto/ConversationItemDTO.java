package com.arquitectura.dto;

public class ConversationItemDTO {
    private String imageUrl;
    private String conversationName;
    private String lastMessage;

    public ConversationItemDTO(String imageUrl, String conversationName, String lastMessage) {
        this.imageUrl = imageUrl;
        this.conversationName = conversationName;
        this.lastMessage = lastMessage;
    }

    // Getters
    public String getImageUrl() { return imageUrl; }
    public String getConversationName() { return conversationName; }
    public String getLastMessage() { return lastMessage; }
}