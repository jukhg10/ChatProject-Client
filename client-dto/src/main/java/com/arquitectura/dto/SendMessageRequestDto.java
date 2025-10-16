package com.arquitectura.dto;

public class SendMessageRequestDto {
    private int channelId;
    private String messageType;
    private String content;

    public SendMessageRequestDto(int channelId, String messageType, String content) {
        this.channelId = channelId;
        this.messageType = messageType;
        this.content = content;
    }

    // Getters
    public int getChannelId() { return channelId; }
    public String getMessageType() { return messageType; }
    public String getContent() { return content; }
}