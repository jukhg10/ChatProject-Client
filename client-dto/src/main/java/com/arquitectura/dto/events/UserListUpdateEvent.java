package com.arquitectura.dto.events;

import com.arquitectura.dto.UserViewDTO;
import org.springframework.context.ApplicationEvent;
import java.util.List;

public class UserListUpdateEvent extends ApplicationEvent {
    private final List<UserViewDTO> users;

    public UserListUpdateEvent(Object source, List<UserViewDTO> users) {
        super(source);
        this.users = users;
    }

    public List<UserViewDTO> getUsers() {
        return users;
    }
}