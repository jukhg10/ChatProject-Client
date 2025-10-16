package com.arquitectura.dto.events;

import com.arquitectura.dto.ChannelViewDTO;
import org.springframework.context.ApplicationEvent;
import java.util.List;

public class InvitationListUpdateEvent extends ApplicationEvent {
    private final List<ChannelViewDTO> invitations;

    public InvitationListUpdateEvent(Object source, List<ChannelViewDTO> invitations) {
        super(source);
        this.invitations = invitations;
    }

    public List<ChannelViewDTO> getInvitations() {
        return invitations;
    }
}