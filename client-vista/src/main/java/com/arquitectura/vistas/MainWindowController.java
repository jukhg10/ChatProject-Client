package com.arquitectura.vistas;

import com.arquitectura.controller.AppController;
import com.arquitectura.dto.ChannelViewDTO;
import com.arquitectura.dto.ConversationItemDTO;
import com.arquitectura.dto.UserViewDTO;
import com.arquitectura.dto.events.ChannelListUpdateEvent;
import com.arquitectura.dto.events.NewChannelEvent;
import com.arquitectura.dto.events.UserListUpdateEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MainWindowController {

    // --- FXML Components ---
    @FXML private ListView<ConversationItemDTO> channelListView;
    @FXML private ListView<String> userListView;
    @FXML private TextArea chatArea;
    @FXML private TextField messageField;
    @FXML private Button sendButton;
    @FXML private Button createChannelButton; // Botón para crear canal
    @FXML private Button searchUserButton;

    private final AppController appController;
    private final ConfigurableApplicationContext springContext;

    // Se inyecta el contexto de Spring para poder cargar nuevas vistas FXML
    public MainWindowController(AppController appController, ConfigurableApplicationContext springContext) {
        this.appController = appController;
        this.springContext = springContext;
    }

    @FXML
    private void initialize() {
        chatArea.appendText("¡Bienvenido al Chat!\n");
        // Configura la celda personalizada para la lista de conversaciones
        channelListView.setCellFactory(listView -> new ConversationCell());
    }

    @FXML
    private void handleSendButtonAction(ActionEvent event) {
        String message = messageField.getText();
        if (message != null && !message.isEmpty()) {
            appController.sendMessage(1, message); // Asume canal 1 por ahora
            messageField.clear();
        }
    }

    @FXML
    private void handleCreateChannelButton() {
        try {
            // Crea un cargador para el FXML de la nueva ventana
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistas/CreateChannel.fxml"));
            // Fundamental: le dice al cargador que use Spring para crear el controlador
            fxmlLoader.setControllerFactory(springContext::getBean);
            Parent parent = fxmlLoader.load();

            // Configura y muestra la nueva ventana como un diálogo modal
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Crear Nuevo Canal");
            stage.setScene(new Scene(parent));
            stage.showAndWait(); // Muestra la ventana y espera a que se cierre
        } catch (IOException e) {
            e.printStackTrace();
            // Aquí podrías mostrar una alerta de error al usuario
        }
    }
    @FXML
private void handleSearchUserButton() {
    try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistas/SearchUser.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent parent = fxmlLoader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Buscar Usuario");
        stage.setScene(new Scene(parent));
        stage.showAndWait();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @EventListener
    public void onUserListUpdate(UserListUpdateEvent event) {
        Platform.runLater(() -> {
            List<String> usernames = event.getUsers().stream()
                                          .map(UserViewDTO::getUsername)
                                          .collect(Collectors.toList());
            userListView.setItems(FXCollections.observableArrayList(usernames));
        });
    }
    @EventListener
    public void onNewChannelCreated(NewChannelEvent event) {
        Platform.runLater(() -> {
            ChannelViewDTO newChannel = event.getNewChannel();
            ConversationItemDTO newItem = new ConversationItemDTO(
                "default", // Usamos la imagen por defecto
                newChannel.getName(),
                "Canal recién creado." // Mensaje placeholder
            );
            
            // Añade el nuevo canal al principio de la lista de conversaciones
            channelListView.getItems().add(0, newItem);
        });
    }
    @EventListener
    public void onChannelListUpdate(ChannelListUpdateEvent event) {
        Platform.runLater(() -> {
            List<ConversationItemDTO> conversations = event.getChannels().stream()
                .map(channel -> new ConversationItemDTO(
                    "default", // Temporalmente
                    channel.getName(),
                    "Último mensaje..." // Temporalmente
                ))
                .collect(Collectors.toList());
            
            channelListView.setItems(FXCollections.observableArrayList(conversations));
        });
    }

    public void displayNewMessage(String message) {
        chatArea.appendText(message + "\n");
    }
}