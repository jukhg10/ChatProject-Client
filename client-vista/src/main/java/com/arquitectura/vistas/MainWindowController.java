package com.arquitectura.vistas;

import com.arquitectura.controller.AppController;
import com.arquitectura.dto.ChannelViewDTO;
import com.arquitectura.dto.ConversationItemDTO;
import com.arquitectura.dto.MessageViewDTO;
import com.arquitectura.dto.UserViewDTO;
import com.arquitectura.dto.events.ChannelListUpdateEvent;
import com.arquitectura.dto.events.MessageHistoryEvent;
import com.arquitectura.dto.events.NewChannelEvent;
import com.arquitectura.dto.events.UserListUpdateEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import com.arquitectura.dto.events.NewMessageEvent;

import java.io.IOException;
import javafx.stage.FileChooser;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.sound.sampled.LineUnavailableException;

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
    @FXML private Button invitationsButton;
    @FXML private Button recordButton;

    private final AppController appController;
    private final ConfigurableApplicationContext springContext;
    private final AudioRecordingService audioService;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private ConversationItemDTO currentConversation;
    
    // Se inyecta el contexto de Spring para poder cargar nuevas vistas FXML
    public MainWindowController(AppController appController, ConfigurableApplicationContext springContext, AudioRecordingService audioService) {
        this.appController = appController;
        this.springContext = springContext;
        this.audioService = audioService; // Asigna el servicio
    }

    @FXML
private void initialize() {
    System.out.println("✅ INITIALIZE: MainWindowController is initializing."); // <-- ADD THIS
    chatArea.appendText("¡Bienvenido al Chat!\n");
    channelListView.setCellFactory(listView -> new ConversationCell());
    channelListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            this.currentConversation = newSelection;
            chatArea.clear();
            chatArea.appendText("Cargando historial para: " + newSelection.getConversationName() + "...\n");
            appController.solicitarHistorialMensajes(newSelection.getChannelId());
        }
    });
    System.out.println("🚀 INITIALIZE: Requesting channel list..."); // <-- ADD THIS
    appController.solicitarListaCanales();
}
    

    @FXML
private void handleSendButtonAction(ActionEvent event) {
    String message = messageField.getText();
    if (message != null && !message.isEmpty() && currentConversation != null) { // <-- ASEGURARSE DE QUE HAY UNA CONVERSACIÓN ACTIVA
        // Pasa el ID del canal actual y el contenido del mensaje
        appController.enviarMensaje(currentConversation.getChannelId(), message);
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
    // V-- ADD THIS LINE --V
    System.out.println("🎉 EVENT: onUserListUpdate received with " + event.getUsers().size() + " users.");
    Platform.runLater(() -> {
        List<String> usernames = event.getUsers().stream()
                                      .map(UserViewDTO::getUsername)
                                      .collect(Collectors.toList());
        userListView.setItems(FXCollections.observableArrayList(usernames));
    });
}
    @FXML
    private void handleInvitationsButtonAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistas/InvitationsWindow.fxml"));
            fxmlLoader.setControllerFactory(springContext::getBean);
            Parent parent = fxmlLoader.load();

            // Obtenemos el controlador de la ventana que acabamos de cargar
            InvitationsWindowController controller = fxmlLoader.getController();
            controller.loadInvitations(); // Le pedimos que cargue los datos

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Mis Invitaciones");
            stage.setScene(new Scene(parent));
            stage.showAndWait();

            // Opcional: Refrescar la lista de canales por si se aceptó una nueva invitación
            appController.solicitarListaCanales();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleRecordButtonAction() {
        if (currentConversation == null) {
            chatArea.appendText("Selecciona una conversación para grabar un audio.\n");
            return;
        }

        if (audioService.isRecording()) {
            // --- Lógica para DETENER la grabación ---
            audioService.stopRecording();
            File recordedFile = audioService.getAudioFile();

            if (recordedFile != null && recordedFile.exists()) {
                appController.enviarMensajeAudio(currentConversation.getChannelId(), recordedFile.getAbsolutePath());
                chatArea.appendText("Enviando audio: " + recordedFile.getName() + "\n");
            }

            // Restaurar el botón a su estado original
            recordButton.setText("🎤 Grabar");
            recordButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;");

        } else {
            // --- Lógica para INICIAR la grabación ---
            try {
                audioService.startRecording();

                // Cambiar la apariencia del botón para indicar que está grabando
                recordButton.setText("■ Detener");
                recordButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");

            } catch (LineUnavailableException | IOException e) {
                chatArea.appendText("Error al iniciar la grabación: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }
    @EventListener
    public void onNewMessageReceived(NewMessageEvent event) {
        Platform.runLater(() -> {
            MessageViewDTO newMessage = event.getMessage();
            // Solo añade el mensaje si corresponde a la conversación actual
            if (currentConversation != null && newMessage.getChannelId() == currentConversation.getChannelId()) {
                String formattedTime = newMessage.getTimestamp().format(timeFormatter);
                String formattedMessage = String.format("[%s] %s: %s\n",
                        formattedTime,
                        newMessage.getAuthorName(),
                        newMessage.getContent());
                chatArea.appendText(formattedMessage);
            }
        });
    }
    @EventListener
    public void onNewChannelCreated(NewChannelEvent event) {
        Platform.runLater(() -> {
            ChannelViewDTO newChannel = event.getNewChannel();
            ConversationItemDTO newItem = new ConversationItemDTO(
            newChannel.getId(), // Add the channel ID
            "default",
            newChannel.getName(),
            "Canal recién creado."
        );
            
            // Añade el nuevo canal al principio de la lista de conversaciones
            channelListView.getItems().add(0, newItem);
        });
    }
    
    @EventListener
public void onChannelListUpdate(ChannelListUpdateEvent event) {
    // V-- ADD THIS LINE --V
    System.out.println("🎉 EVENT: onChannelListUpdate received with " + event.getChannels().size() + " channels.");
    Platform.runLater(() -> {
        List<ConversationItemDTO> conversations = event.getChannels().stream()
        .map(channel -> new ConversationItemDTO(
            channel.getId(),
            "default",
            channel.getName(),
            "Último mensaje..."
        ))
        .collect(Collectors.toList());
        
        channelListView.setItems(FXCollections.observableArrayList(conversations));
    });
}
    @EventListener
    public void onHistoryReceived(MessageHistoryEvent event) {
        Platform.runLater(() -> {
            chatArea.clear(); // Limpia el área de chat
            for (com.arquitectura.dto.MessageViewDTO msg : event.getMessages()) {
                String formattedTime = msg.getTimestamp().format(timeFormatter);
                String formattedMessage = String.format("[%s] %s: %s\n",
                        formattedTime,
                        msg.getAuthorName(),
                        msg.getContent());
                chatArea.appendText(formattedMessage);
            }
        });
    }
    public void displayNewMessage(String message) {
        chatArea.appendText(message + "\n");
    }
}