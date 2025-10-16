package com.arquitectura.vistas;

import com.arquitectura.controller.AppController;
import com.arquitectura.dto.UserViewDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateChannelController {

    @FXML
    private TextField channelNameField;
    @FXML
    private ListView<String> usersListView;
    @FXML
    private Button createButton;
    @FXML
    private Button cancelButton;

    private final AppController appController;

    private ObservableList<UserViewDTO> allUsers = FXCollections.observableArrayList();

    public CreateChannelController(AppController appController) {
        this.appController = appController;
    }

    public void setUsers(List<UserViewDTO> users) {
        allUsers.setAll(users);
        List<String> usernames = users.stream().map(UserViewDTO::getUsername).collect(Collectors.toList());
        usersListView.setItems(FXCollections.observableArrayList(usernames));
    }

    @FXML
    private void handleCreateButtonAction() {
        String channelName = channelNameField.getText();
        if (channelName != null && !channelName.isEmpty()) {
            // Corrected method call
            appController.crearCanal(channelName);
            closeWindow();
        } else {
            // Aquí podrías mostrar un error al usuario
            System.out.println("El nombre del canal no puede estar vacío.");
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}