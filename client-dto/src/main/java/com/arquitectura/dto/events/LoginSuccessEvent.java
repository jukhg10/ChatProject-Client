package com.arquitectura.dto.events;

import org.springframework.context.ApplicationEvent;

public class LoginSuccessEvent extends ApplicationEvent {
    public LoginSuccessEvent(Object source) {
        super(source);
    }
}