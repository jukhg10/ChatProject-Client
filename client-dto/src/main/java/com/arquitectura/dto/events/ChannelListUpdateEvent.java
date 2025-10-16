package com.arquitectura.dto.events;

import com.arquitectura.dto.ChannelViewDTO;
import org.springframework.context.ApplicationEvent;
import java.util.List;

public class ChannelListUpdateEvent extends ApplicationEvent {
    private final List<ChannelViewDTO> channels;

    public ChannelListUpdateEvent(Object source, List<ChannelViewDTO> channels) {
        super(source);
        this.channels = channels;
    }

    public List<ChannelViewDTO> getChannels() {
        return channels;
    }
}