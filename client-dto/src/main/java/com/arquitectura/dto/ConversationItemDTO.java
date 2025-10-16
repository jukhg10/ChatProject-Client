package com.arquitectura.dto;

public class ConversationItemDTO {
    private final int channelId; // AÑADIDO
    private String imageUrl;
    private String conversationName;
    private String lastMessage;

    // Constructor actualizado para incluir el ID
    public ConversationItemDTO(int channelId, String imageUrl, String conversationName, String lastMessage) {
        this.channelId = channelId;
        this.imageUrl = imageUrl;
        this.conversationName = conversationName;
        this.lastMessage = lastMessage;
    }

    // Getters
    public int getChannelId() { return channelId; } // AÑADIDO
    public String getImageUrl() { return imageUrl; }
    public String getConversationName() { return conversationName; }
    public String getLastMessage() { return lastMessage; }
}