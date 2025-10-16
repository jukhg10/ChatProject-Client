package com.arquitectura.logica;

import com.arquitectura.dto.SendMessageRequestDto;

public class SendMessageRequestFactory {

    public enum MessageType {
        TEXT,
        AUDIO
    }

    public static SendMessageRequestDto createRequest(int channelId, MessageType type, String content) {
        return new SendMessageRequestDto(channelId, type.toString(), content);
    }
}