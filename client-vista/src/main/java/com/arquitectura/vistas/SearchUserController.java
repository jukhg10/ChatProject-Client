package com.arquitectura.vistas;

import com.arquitectura.controller.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class SearchUserController {

    @FXML
    private TextField usernameField;
    @FXML
    private Button searchButton;
    @FXML
    private Label errorLabel;

    private final AppController appController;

    public SearchUserController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void handleSearchAction() {
        String username = usernameField.getText();
        if (username == null || username.trim().isEmpty()) {
            errorLabel.setText("El nombre de usuario no puede estar vacío.");
            return;
        }

        // Llama al AppController para que gestione la creación del chat directo
        appController.iniciarChatDirecto(username);

        // Cierra la ventana de búsqueda
        Stage stage = (Stage) searchButton.getScene().getWindow();
        stage.close();
    }
}