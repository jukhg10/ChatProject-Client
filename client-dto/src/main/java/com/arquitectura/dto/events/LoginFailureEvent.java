package com.arquitectura.dto.events;

import org.springframework.context.ApplicationEvent;

public class LoginFailureEvent extends ApplicationEvent {
    private final String message;

    public LoginFailureEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}