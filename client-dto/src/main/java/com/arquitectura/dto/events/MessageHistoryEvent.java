// En: client-dto/src/main/java/com/arquitectura/dto/events/MessageHistoryEvent.java

package com.arquitectura.dto.events;

import com.arquitectura.dto.MessageViewDTO;
import org.springframework.context.ApplicationEvent;
import java.util.List;

public class MessageHistoryEvent extends ApplicationEvent {
    private final int channelId; // <-- Añadir campo para el ID del canal
    private final List<MessageViewDTO> messages;

    // Modificar el constructor para aceptar el channelId
    public MessageHistoryEvent(Object source, int channelId, List<MessageViewDTO> messages) {
        super(source);
        this.channelId = channelId;
        this.messages = messages;
    }

    // Añadir el getter para el channelId
    public int getChannelId() {
        return channelId;
    }

    public List<MessageViewDTO> getMessages() {
        return messages;
    }
}