package com.arquitectura.vistas;

import com.arquitectura.controller.AppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.ArrayList; // <-- ADD THIS IMPORT
import java.util.List;    // <-- ADD THIS IMPORT

@Component
public class CreateChannelController {

    @FXML private TextField channelNameField;
    @FXML private TextField searchUserField;
    @FXML private Button searchUserButton;
    @FXML private ListView<String> invitedUsersListView;
    @FXML private Button createChannelButton;
    @FXML private Label errorLabel;

    private final AppController appController;
    private final ObservableList<String> invitedUsers = FXCollections.observableArrayList();

    public CreateChannelController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void initialize() {
        invitedUsersListView.setItems(invitedUsers);
    }

    @FXML
    private void handleSearchUser() {
        String username = searchUserField.getText();
        if (username != null && !username.isEmpty() && !invitedUsers.contains(username)) {
            invitedUsers.add(username);
            searchUserField.clear();
        }
    }

    @FXML
    private void handleCreateChannel() {
        String channelName = channelNameField.getText();
        if (channelName == null || channelName.isEmpty()) {
            errorLabel.setText("El nombre del canal es obligatorio.");
            return;
        }

        // Convert the ObservableList to a regular List to pass to the controller
        List<String> invitedUsersList = new ArrayList<>(invitedUsers);
        appController.crearCanalGrupo(channelName, invitedUsersList);

        // Close the dialog window
        Stage stage = (Stage) createChannelButton.getScene().getWindow();
        stage.close();
    }
}