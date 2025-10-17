package com.arquitectura.vistas;

import com.arquitectura.controller.AppController;
import com.arquitectura.dto.UserViewDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private TextField searchUserField;
    @FXML
    private Button searchUserButton;
    @FXML
    private ListView<String> invitedUsersListView;
    @FXML
    private Button createChannelButton;
    @FXML
    private Label errorLabel;

    private final AppController appController;

    private ObservableList<UserViewDTO> allUsers = FXCollections.observableArrayList();
    private ObservableList<String> invitedUsers = FXCollections.observableArrayList();

    public CreateChannelController(AppController appController) {
        this.appController = appController;
    }

    public void setUsers(List<UserViewDTO> users) {
        allUsers.setAll(users);
    }

    @FXML
    private void handleSearchUser() {
        String searchText = searchUserField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            return;
        }
        
        List<String> foundUsers = allUsers.stream()
                .map(UserViewDTO::getUsername)
                .filter(username -> username.toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        invitedUsersListView.setItems(FXCollections.observableArrayList(foundUsers));
    }

    @FXML
    private void handleCreateChannel() {
        String channelName = channelNameField.getText();
        if (channelName != null && !channelName.isEmpty()) {
            appController.crearCanal(channelName);
            closeWindow();
        } else {
            errorLabel.setText("El nombre del canal no puede estar vacío.");
            System.out.println("El nombre del canal no puede estar vacío.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) createChannelButton.getScene().getWindow();
        stage.close();
    }
}