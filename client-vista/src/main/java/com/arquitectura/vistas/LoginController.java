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
    private TextField serverIpField;
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
        String ip = serverIpField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (ip.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("Todos los campos son obligatorios.");
            return;
        }
        
        // Deshabilita el botón para evitar múltiples clics
        loginButton.setDisable(true);
        errorLabel.setText("Conectando...");
        
        try {
            // Llama al AppController para manejar la lógica de conexión y login
            appController.connectAndLogin(ip, 12345, username, password); // Usamos un puerto quemado por ahora
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
        // La transición a la siguiente ventana la manejará el AppController
    }
}