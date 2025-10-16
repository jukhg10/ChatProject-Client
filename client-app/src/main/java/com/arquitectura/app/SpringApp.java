package com.arquitectura.app;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.arquitectura")
public class SpringApp {

    public static void main(String[] args) {
        // This simply starts the JavaFX application
        Application.launch(MainApplication.class, args);
    }
}