package com.arquitectura.vistas;

import com.arquitectura.controller.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;

    private final AppController appController;

    public LoginController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Todos los campos son obligatorios.");
            return;
        }
        
        loginButton.setDisable(true);
        errorLabel.setText("Conectando...");
        
        try {
            // The call to connectAndLogin no longer needs the IP and port
            appController.connectAndLogin(username, password);
        } catch (Exception e) {
            showError("Error de conexión: " + e.getMessage());
            loginButton.setDisable(false);
        }
    }

    public void showError(String message) {
        errorLabel.setText(message);
        loginButton.setDisable(false);
    }

    public void showLoginSuccess() {
        errorLabel.setText("¡Conexión exitosa!");
    }
}