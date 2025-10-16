package com.arquitectura.dto.events;

import com.arquitectura.dto.MessageViewDTO;
import org.springframework.context.ApplicationEvent;
import java.util.List;

public class MessageHistoryEvent extends ApplicationEvent {
    private final List<MessageViewDTO> messages;

    public MessageHistoryEvent(Object source, List<MessageViewDTO> messages) {
        super(source);
        this.messages = messages;
    }

    public List<MessageViewDTO> getMessages() {
        return messages;
    }
}