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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.arquitectura.dto.events.NewMessageEvent;
import com.arquitectura.dto.events.FileDownloadEvent;

import java.io.IOException;
import javafx.stage.FileChooser;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.sound.sampled.LineUnavailableException;

@Component
public class MainWindowController {

    // --- FXML Components ---
    @FXML private ListView<ConversationItemDTO> channelListView;
    @FXML private ListView<String> userListView;
    @FXML private TextFlow chatArea;
    @FXML private TextField messageField;
    @FXML private Button sendButton;
    @FXML private Button createChannelButton; // Botón para crear canal
    @FXML private Button searchUserButton;
    @FXML private Button invitationsButton;
    @FXML private Button recordButton;
@FXML private Button inviteUserButton;
    private final AppController appController;
    private final ConfigurableApplicationContext springContext;
    private final AudioRecordingService audioService;
    private final AudioPlaybackService playbackService;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private ConversationItemDTO currentConversation;
    private List<UserViewDTO> cachedUserList;
    
    // Se inyecta el contexto de Spring para poder cargar nuevas vistas FXML
public MainWindowController(AppController appController, ConfigurableApplicationContext springContext, AudioRecordingService audioService, AudioPlaybackService playbackService) {        this.appController = appController;
        this.springContext = springContext;
        this.audioService = audioService;
        this.playbackService = playbackService;
    }

    @FXML
private void initialize() {
    System.out.println("✅ INITIALIZE: MainWindowController is initializing."); // <-- ADD THIS
    displaySystemMessage("¡Bienvenido al Chat!\n");
    channelListView.setCellFactory(listView -> new ConversationCell());
    inviteUserButton.setDisable(true);
    channelListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            this.currentConversation = newSelection;
            chatArea.getChildren().clear();
displaySystemMessage("Cargando historial para: " + newSelection.getConversationName() + "...\n");
            appController.solicitarHistorialMensajes(newSelection.getChannelId());
            inviteUserButton.setDisable(false);
        }
        else {
            inviteUserButton.setDisable(true); // DISABLE the button if no channel is selected
        }
    });
    System.out.println("🚀 INITIALIZE: Requesting channel list..."); // <-- ADD THIS
    appController.solicitarListaCanales();
    
}
    
@FXML
    private void handleInviteUserButtonAction() {
        if (currentConversation == null) { return; }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistas/InviteUser.fxml"));
            fxmlLoader.setControllerFactory(springContext::getBean);
            Parent parent = fxmlLoader.load();

            InviteUserController controller = fxmlLoader.getController();
            controller.setChannelInfo(currentConversation.getChannelId(), currentConversation.getConversationName());
            
            // ADD THIS LINE: Pass the cached list to the new controller
            controller.loadUsers(this.cachedUserList);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Invitar Usuario al Canal");
            stage.setScene(new Scene(parent));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        // ADD THIS LINE to save the user list when it arrives
        this.cachedUserList = event.getUsers(); 
        
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
            displaySystemMessage("Selecciona una conversación para grabar un audio.\n");
            return;
        }

        if (audioService.isRecording()) {
            // --- Lógica para DETENER la grabación ---
            audioService.stopRecording();
            File recordedFile = audioService.getAudioFile();

            if (recordedFile != null && recordedFile.exists()) {
                appController.enviarMensajeAudio(currentConversation.getChannelId(), recordedFile.getAbsolutePath());
                displaySystemMessage("Enviando audio: " + recordedFile.getName() + "\n");
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
    displaySystemMessage("Error al iniciar la grabación: " + e.getMessage() + "\n"); // <-- ASÍ DEBE QUEDAR
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
            appendMessage(newMessage); // Llama al nuevo método
        }
    });
}
    @EventListener
public void onNewChannelCreated(NewChannelEvent event) {
    Platform.runLater(() -> {
        ChannelViewDTO newChannel = event.getNewChannel();
        ConversationItemDTO newItem = new ConversationItemDTO(
            newChannel.getChannelId(), // <-- FIX: Was getId()
            "default",
            newChannel.getChannelName(), // <-- FIX: Was getName()
            "Canal recién creado."
        );
        channelListView.getItems().add(0, newItem);
    });
}
    
    @EventListener
public void onChannelListUpdate(ChannelListUpdateEvent event) {
    Platform.runLater(() -> {
        List<ConversationItemDTO> conversations = event.getChannels().stream()
            .map(channel -> new ConversationItemDTO(
                channel.getChannelId(), // <-- FIX: Was getId()
                "default",
                channel.getChannelName(), // <-- FIX: Was getName()
                "Último mensaje..."
            ))
            .collect(Collectors.toList());
        channelListView.setItems(FXCollections.observableArrayList(conversations));
    });
}
@EventListener
public void onFileDownloaded(FileDownloadEvent event) {
    Platform.runLater(() -> {
        playbackService.playAudio(event.getFilePath());
    });
}
    @EventListener
public void onHistoryReceived(MessageHistoryEvent event) {
    Platform.runLater(() -> {
        chatArea.getChildren().clear();
        for (com.arquitectura.dto.MessageViewDTO msg : event.getMessages()) {
            appendMessage(msg); // <-- Deja solo una llamada
        }
    });
}
   
    
   private void appendMessage(MessageViewDTO msg) {
    String formattedTime = msg.getTimestamp().format(timeFormatter);
    String prefix = String.format("[%s] %s: ", formattedTime, msg.getAuthorName());
    
    Text prefixText = new Text(prefix);
    chatArea.getChildren().add(prefixText);

    // Revisa si es un mensaje de audio para crear un enlace
    if (msg.getContent().startsWith("Audio: ")) {
        String fileName = msg.getContent().substring("Audio: ".length());
        Hyperlink audioLink = new Hyperlink("▶️ Escuchar " + fileName);
        audioLink.setOnAction(e -> {
            displaySystemMessage("Descargando " + fileName + "...\n");
            appController.descargarYReproducirAudio(fileName);
        });
        chatArea.getChildren().add(audioLink);
    } else {
        Text contentText = new Text(msg.getContent());
        chatArea.getChildren().add(contentText);
    }
    
    chatArea.getChildren().add(new Text("\n"));
} 
private void displaySystemMessage(String message) {
    Text systemText = new Text(message);
    systemText.setStyle("-fx-fill: grey; -fx-font-style: italic;");
    chatArea.getChildren().add(systemText);
}
}