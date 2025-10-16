package com.arquitectura.dto.events;

import com.arquitectura.dto.ChannelViewDTO;
import org.springframework.context.ApplicationEvent;

public class NewChannelEvent extends ApplicationEvent {
    private final ChannelViewDTO newChannel;

    public NewChannelEvent(Object source, ChannelViewDTO newChannel) {
        super(source);
        this.newChannel = newChannel;
    }

    public ChannelViewDTO getNewChannel() {
        return newChannel;
    }
}