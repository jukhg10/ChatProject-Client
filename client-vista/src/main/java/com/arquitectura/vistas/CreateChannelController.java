package com.arquitectura.vistas;

import com.arquitectura.controller.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class CreateChannelController {

    @FXML
    private TextField channelNameField;
    @FXML
    private Button createChannelButton;
    @FXML
    private Label errorLabel;

    private final AppController appController;

    public CreateChannelController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void handleCreateChannel() {
        String channelName = channelNameField.getText();
        if (channelName != null && !channelName.trim().isEmpty()) {
            // Llama al método del controlador principal para crear solo el canal de grupo
            appController.crearCanalGrupo(channelName);
            closeWindow();
        } else {
            errorLabel.setText("El nombre del canal no puede estar vacío.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) createChannelButton.getScene().getWindow();
        stage.close();
    }
}