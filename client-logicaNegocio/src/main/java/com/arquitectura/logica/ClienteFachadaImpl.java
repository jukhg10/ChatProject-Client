package com.arquitectura.logica;

import com.arquitectura.dto.events.InvitationListUpdateEvent;
import com.arquitectura.dto.ChannelViewDTO;
import com.arquitectura.dto.MessageDTO;
import com.arquitectura.dto.MessageViewDTO;
import com.arquitectura.logica.IUserService;
import com.arquitectura.dto.SendMessageRequestDto;
import com.arquitectura.dto.UserDTO;
import com.arquitectura.dto.UserViewDTO;
import com.arquitectura.entidades.User;
import com.arquitectura.dto.events.ChannelListUpdateEvent;
import com.arquitectura.dto.events.LoginFailureEvent;
import com.arquitectura.dto.events.LoginSuccessEvent;
import com.arquitectura.dto.events.MessageHistoryEvent;
import com.arquitectura.dto.events.NewChannelEvent;
import com.arquitectura.dto.events.NewMessageEvent;
import com.arquitectura.dto.events.UserListUpdateEvent;
import com.arquitectura.entidades.Message;
import com.arquitectura.entidades.TextMessage;
import com.arquitectura.logica.SendMessageRequestFactory.MessageType;
import com.arquitectura.logica.ports.INetworkInputPort;
import com.arquitectura.logica.ports.INetworkOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteFachadaImpl implements IClienteFachada, INetworkInputPort {

    private final INetworkOutputPort networkOutputPort;
    private final ApplicationEventPublisher eventPublisher;
    private final IMessageService messageService;
    private User currentUser;
    private final IUserService userService;

    @Autowired
public ClienteFachadaImpl(INetworkOutputPort networkOutputPort, ApplicationEventPublisher eventPublisher, IMessageService messageService, IUserService userService) {
    this.networkOutputPort = networkOutputPort;
    this.eventPublisher = eventPublisher;
    this.messageService = messageService;
    this.userService = userService;
}

    @Override
    public void login(String username, String password) {
        UserDTO userDTO = new UserDTO(username, password);
        networkOutputPort.enviarSolicitudLogin(userDTO);
    }

    @Override
    public void logout() {
        networkOutputPort.enviarSolicitudLogout();
    }

    @Override
    public void solicitarListaUsuarios() {
        networkOutputPort.enviarSolicitudListaUsuarios();
    }

    @Override
    public void solicitarListaCanales() {
        networkOutputPort.enviarSolicitudListaCanales();
    }

    @Override
    public void crearCanalGrupo(String channelName) {
        networkOutputPort.enviarSolicitudCrearCanalGrupo(channelName);
    }

    @Override
    public void solicitarHistorialMensajes(int channelId) {
        networkOutputPort.enviarSolicitudHistorial(channelId);
    }

   @Override
    public void enviarMensajeTexto(int channelId, String content) {
        // 1. Save the message locally and get the domain object back
        Message savedMessage = messageService.guardarMensajeTexto(content, this.currentUser, true);

        // 2. Convert the domain object to a DTO for the UI
        UserViewDTO authorDto = new UserViewDTO(this.currentUser.getId(), this.currentUser.getUsername());
        MessageViewDTO messageDTO = new MessageViewDTO(
            savedMessage.getId(),
            ((TextMessage) savedMessage).getContent(),
            authorDto,
            savedMessage.getTimestamp(),
            true, // It's our own message
            channelId
        );

        // 3. Publish the event to update the UI immediately
        eventPublisher.publishEvent(new NewMessageEvent(this, messageDTO));

        // 4. Send the message to the server
        SendMessageRequestDto requestDto = SendMessageRequestFactory.createRequest(channelId, MessageType.TEXT, content);
        networkOutputPort.enviarSolicitudMensajeTexto(requestDto);
    }

    @Override
    public void invitarUsuario(int channelId, int userIdToInvite) {
        networkOutputPort.enviarSolicitudInvitarUsuario(channelId, userIdToInvite);
    }

    @Override
    public void solicitarInvitaciones() {
        networkOutputPort.enviarSolicitudInvitaciones();
    }

    @Override
    public void responderInvitacion(int channelId, boolean aceptada) {
        networkOutputPort.enviarSolicitudResponderInvitacion(channelId, aceptada);
    }

  
    @Override
public void enviarMensajeAudio(int channelId, String filePath) {
    // Local saving logic
    if (currentUser != null) {
        Message savedMessage = messageService.guardarMensajeAudio(filePath, this.currentUser, true);
        UserViewDTO authorDto = new UserViewDTO(this.currentUser.getId(), this.currentUser.getUsername());
        MessageViewDTO messageDTO = new MessageViewDTO(
            savedMessage.getId(),
            "Audio: " + new java.io.File(filePath).getName(),
            authorDto,
            savedMessage.getTimestamp(),
            true,
            channelId
        );
        eventPublisher.publishEvent(new NewMessageEvent(this, messageDTO));
    }

    // --- NEW LOGIC TO SEND FILE DATA ---
    try {
        // 1. Get the file name
        java.io.File audioFile = new java.io.File(filePath);
        String fileName = audioFile.getName();

        // 2. Read the file's bytes
        byte[] audioBytes = java.nio.file.Files.readAllBytes(audioFile.toPath());

        // 3. Encode the bytes into a Base64 string
        String encodedAudio = java.util.Base64.getEncoder().encodeToString(audioBytes);

        // 4. Create a payload with the file name and the data
        String payload = fileName + ";" + encodedAudio;

        // 5. Send it to the server
        SendMessageRequestDto requestDto = SendMessageRequestFactory.createRequest(channelId, MessageType.AUDIO, payload);
        networkOutputPort.enviarSolicitudMensajeAudio(requestDto);

    } catch (java.io.IOException e) {
        System.err.println("Error reading audio file for sending: " + e.getMessage());
        e.printStackTrace();
        // Optionally, display an error to the user in the UI
    }
}
    @Override
    public void descargarArchivo(String relativePath) {
        networkOutputPort.enviarSolicitudDescargarArchivo(relativePath);
    }

   @Override
public void procesarLoginExitoso(int userId, String username) {
    this.currentUser = new User(userId, username);
    userService.guardarUsuario(this.currentUser); // Add this line
    eventPublisher.publishEvent(new LoginSuccessEvent(this));
}

    @Override
    public void procesarFalloDeLogin(String mensajeError) {
        this.currentUser = null;
        eventPublisher.publishEvent(new LoginFailureEvent(this, mensajeError));
    }

    @Override
    public void procesarListaDeUsuarios(List<UserViewDTO> users) {
        eventPublisher.publishEvent(new UserListUpdateEvent(this, users));
    }

    @Override
    public void procesarListaDeCanales(List<ChannelViewDTO> channels) {
        eventPublisher.publishEvent(new ChannelListUpdateEvent(this, channels));
    }

    @Override
public void procesarNuevoCanal(ChannelViewDTO channel) {
    // FIX: Change getId() to getChannelId() in this print statement
    System.out.println("LÓGICA: Nuevo canal creado con ID " + channel.getChannelId() + ". Publicando evento...");
    
    eventPublisher.publishEvent(new NewChannelEvent(this, channel));
}

    

    @Override
    public void procesarMensajeRecibido(Message message) {
        User authorEntity = message.getAuthor();
        UserViewDTO authorDto = new UserViewDTO(authorEntity.getId(), authorEntity.getUsername());

        MessageViewDTO messageDTO = new MessageViewDTO(
            message.getId(),
            ((com.arquitectura.entidades.TextMessage) message).getContent(),
            authorDto,
            message.getTimestamp(),
            message.isOwnMessage(),
            0
        );
        eventPublisher.publishEvent(new NewMessageEvent(this, messageDTO));
    }
    
    @Override
    public void procesarListaDeInvitaciones(List<ChannelViewDTO> invitaciones) {
        eventPublisher.publishEvent(new InvitationListUpdateEvent(this, invitaciones));
    }
@Override
public void procesarHistorialMensajes(int channelId, List<MessageViewDTO> messages) {
    eventPublisher.publishEvent(new MessageHistoryEvent(this, channelId, messages));
}
    @Override
    public void procesarMensajeRecibido(MessageViewDTO message) {
        if (currentUser != null) {
            boolean isOwnMessage = currentUser.getId() == message.getAuthor().getUserId();
            
            // Only process messages that are NOT from the current user.
            // Own messages are already handled when sent.
            if (!isOwnMessage) {
                User author = new User(message.getAuthor().getUserId(), message.getAuthor().getUsername());
                messageService.guardarMensajeTexto(message.getContent(), author, false);
                eventPublisher.publishEvent(new NewMessageEvent(this, message));
            }
        }
    }

}