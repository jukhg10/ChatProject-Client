package com.arquitectura.dto.events;

import org.springframework.context.ApplicationEvent;

public class FileDownloadEvent extends ApplicationEvent {
    private final String filePath;

    public FileDownloadEvent(Object source, String filePath) {
        super(source);
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}