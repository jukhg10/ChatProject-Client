package com.arquitectura.dto.events;

import com.arquitectura.dto.MessageViewDTO;
import org.springframework.context.ApplicationEvent;

public class NewMessageEvent extends ApplicationEvent {
    private final MessageViewDTO message;

    public NewMessageEvent(Object source, MessageViewDTO message) {
        super(source);
        this.message = message;
    }

    public MessageViewDTO getMessage() {
        return message;
    }
}