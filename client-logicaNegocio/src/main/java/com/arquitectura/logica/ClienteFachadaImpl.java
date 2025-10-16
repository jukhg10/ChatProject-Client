package com.arquitectura.logica;

import com.arquitectura.dto.events.InvitationListUpdateEvent;
import com.arquitectura.dto.ChannelViewDTO;
import com.arquitectura.dto.MessageDTO;
import com.arquitectura.dto.MessageViewDTO;
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

    @Autowired
    public ClienteFachadaImpl(INetworkOutputPort networkOutputPort, ApplicationEventPublisher eventPublisher, IMessageService messageService) {
        this.networkOutputPort = networkOutputPort;
        this.eventPublisher = eventPublisher;
        this.messageService = messageService; 
    }

    // --- MÉTODOS DE IClienteFachada (Llamadas desde el Controlador) ---
    @Override
    public void login(String username, String password) {
        // Store the user details temporarily to be saved on successful login
        this.currentUser = new User(0, username); // ID will be updated on success
        UserDTO userDTO = new UserDTO(username, password);
        networkOutputPort.enviarSolicitudLogin(userDTO);
    }

    @Override
    public void sendMessage(int channelId, String content) {
        MessageDTO messageDTO = new MessageDTO(channelId, content);
        // Save the message the user sends to the local DB
        messageService.guardarMensajeTexto(content, this.currentUser, true);
        networkOutputPort.enviarSolicitudMensaje(messageDTO);
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
    public void solicitarInvitaciones() {
        networkOutputPort.enviarSolicitudInvitaciones();
    }

    @Override
    public void responderInvitacion(int channelId, boolean aceptada) {
        networkOutputPort.enviarSolicitudResponderInvitacion(channelId, aceptada);
    }
    // --- MÉTODOS DE INetworkInputPort (Llamadas desde el ServerListener) ---
    @Override
    public void procesarLoginExitoso() {
        eventPublisher.publishEvent(new LoginSuccessEvent(this));
    }

    @Override
    public void procesarFalloDeLogin(String mensajeError) {
        this.currentUser = null; // Clear the user on failed login
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
    public void crearCanalGrupo(String channelName) {
        networkOutputPort.enviarSolicitudCrearCanal(channelName);
    }
    @Override
    public void procesarNuevoCanal(ChannelViewDTO channel) {
        System.out.println("LÓGICA: Nuevo canal creado con ID " + channel.getId() + ". Publicando evento...");
        eventPublisher.publishEvent(new NewChannelEvent(this, channel));
    }
    @Override
    public void procesarHistorialMensajes(List<MessageViewDTO> messages) {
        eventPublisher.publishEvent(new MessageHistoryEvent(this, messages));
    }

        @Override
    public void solicitarHistorialMensajes(int channelId) {
        networkOutputPort.enviarSolicitudHistorial(channelId);
    }

    @Override
    public void enviarMensajeTexto(int channelId, String content) {
        SendMessageRequestDto requestDto = SendMessageRequestFactory.createRequest(channelId, MessageType.TEXT, content);
        // Persist our own sent message
        messageService.guardarMensajeTexto(content, this.currentUser, true);
        networkOutputPort.enviarSolicitudMensajeTexto(requestDto);
    }

    @Override
    public void enviarMensajeAudio(int channelId, String filePath) {
        // Persist the sent audio message locally
        if (currentUser != null) {
            messageService.guardarMensajeAudio(filePath, this.currentUser, true);
        }
        
        SendMessageRequestDto requestDto = SendMessageRequestFactory.createRequest(channelId, MessageType.AUDIO, filePath);
        networkOutputPort.enviarSolicitudMensajeAudio(requestDto);
    }
   @Override
    public void procesarMensajeRecibido(Message message) {
        // This is a simplified conversion. You might need to adjust it based on your object structure.
        MessageViewDTO messageDTO = new MessageViewDTO(
            message.getId(),
            ((com.arquitectura.entidades.TextMessage) message).getContent(), // Assuming it's a TextMessage
            message.getAuthor().getUsername(),
            message.getTimestamp(),
            message.isOwnMessage(),
            0 // Placeholder for channelId, as it's not in the Message entity
        );
        eventPublisher.publishEvent(new NewMessageEvent(this, messageDTO));
    }
@Override
    public void procesarListaDeInvitaciones(List<ChannelViewDTO> invitaciones) {
        // Cuando la red nos da la lista de invitaciones, publicamos un evento
        // para que la vista (que está escuchando) se actualice.
        eventPublisher.publishEvent(new InvitationListUpdateEvent(this, invitaciones));
    }
@Override
    public void procesarMensajeRecibido(MessageViewDTO message) {
        // 3. Save the received message to the local database
        if (currentUser != null) {
            boolean isOwnMessage = currentUser.getUsername().equals(message.getAuthorName());
            User author = new User(0, message.getAuthorName()); // Create a temporary author entity
            
            messageService.guardarMensajeTexto(message.getContent(), author, isOwnMessage);
        }
        
        // 4. Publish the event to update the UI
        eventPublisher.publishEvent(new NewMessageEvent(this, message));
    }

    @Override
    public void solicitarChatDirecto(String username) {
        networkOutputPort.enviarSolicitudChatDirecto(username);
    }
    
}