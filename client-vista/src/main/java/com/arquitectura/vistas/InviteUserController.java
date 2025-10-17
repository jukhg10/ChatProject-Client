package com.arquitectura.vistas;

import com.arquitectura.controller.AppController;
import com.arquitectura.dto.UserViewDTO;
import com.arquitectura.dto.events.UserListUpdateEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class InviteUserController {

    @FXML private Label channelNameLabel;
    @FXML private ListView<UserViewDTO> usersListView;
    @FXML private Button inviteButton;
    @FXML private Label statusLabel;

    private final AppController appController;
    private int currentChannelId;

    public InviteUserController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    public void initialize() {
        // Use a custom cell factory to properly display the username in the ListView
        usersListView.setCellFactory(param -> new ListCell<UserViewDTO>() {
            @Override
            protected void updateItem(UserViewDTO user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null || user.getUsername() == null) {
                    setText(null);
                } else {
                    setText(user.getUsername());
                }
            }
        });
    }

    /**
     * Receives channel information from the main window and requests the user list.
     */
    public void setChannelInfo(int channelId, String channelName) {
        this.currentChannelId = channelId;
        channelNameLabel.setText("Invitar a '" + channelName + "'");
        
    }
public void loadUsers(List<UserViewDTO> users) {
        if (users != null) {
            usersListView.setItems(FXCollections.observableArrayList(users));
        }
    }
    /**
     * Listens for the user list event and populates the ListView.
     */
    
    @FXML
    private void handleInviteButtonAction() {
        UserViewDTO selectedUser = usersListView.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            statusLabel.setText("Por favor, selecciona un usuario.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        // Call the AppController to send the invitation request
        appController.invitarUsuario(currentChannelId, selectedUser.getUserId());
        
        statusLabel.setText("Invitación enviada a " + selectedUser.getUsername());
        statusLabel.setTextFill(javafx.scene.paint.Color.GREEN);

        // Optionally, close the window after a short delay or disable the button
        inviteButton.setDisable(true);
    }
}