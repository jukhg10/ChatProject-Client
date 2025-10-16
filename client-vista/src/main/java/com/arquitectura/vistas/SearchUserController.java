package com.arquitectura.vistas;

import com.arquitectura.controller.AppController;
import com.arquitectura.dto.UserViewDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SearchUserController {

    @FXML
    private ListView<String> userListView;
    @FXML
    private Button startChatButton;

    private final AppController appController;
    private List<UserViewDTO> userList; // Store the full user list

    public SearchUserController(AppController appController) {
        this.appController = appController;
    }

    public void setUsers(List<UserViewDTO> users) {
        this.userList = users;
        userListView.setItems(FXCollections.observableArrayList(
                users.stream().map(UserViewDTO::getUsername).toList()
        ));
    }

    @FXML
    private void handleStartChatButtonAction() {
        String selectedUsername = userListView.getSelectionModel().getSelectedItem();
        if (selectedUsername != null) {
            // Find the user ID corresponding to the selected username
            Optional<UserViewDTO> selectedUser = userList.stream()
                    .filter(user -> user.getUsername().equals(selectedUsername))
                    .findFirst();

            selectedUser.ifPresent(userViewDTO -> {
                appController.iniciarChatDirecto(userViewDTO.getId());
                closeWindow();
            });
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) startChatButton.getScene().getWindow();
        stage.close();
    }
}