package com.arquitectura.logica;

import com.arquitectura.dto.ChannelViewDTO;
import com.arquitectura.dto.MessageDTO;
import com.arquitectura.dto.UserDTO;
import com.arquitectura.dto.UserViewDTO;
import com.arquitectura.dto.events.ChannelListUpdateEvent;
import com.arquitectura.dto.events.LoginFailureEvent;
import com.arquitectura.dto.events.LoginSuccessEvent;
import com.arquitectura.dto.events.NewChannelEvent;
import com.arquitectura.dto.events.UserListUpdateEvent;
import com.arquitectura.entidades.Message;
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

    @Autowired
    public ClienteFachadaImpl(INetworkOutputPort networkOutputPort, ApplicationEventPublisher eventPublisher) {
        this.networkOutputPort = networkOutputPort;
        this.eventPublisher = eventPublisher;
    }

    // --- MÉTODOS DE IClienteFachada (Llamadas desde el Controlador) ---
    @Override
    public void login(String username, String password) {
        UserDTO userDTO = new UserDTO(username, password);
        networkOutputPort.enviarSolicitudLogin(userDTO);
    }

    @Override
    public void sendMessage(int channelId, String content) {
        MessageDTO messageDTO = new MessageDTO(channelId, content);
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


    // --- MÉTODOS DE INetworkInputPort (Llamadas desde el ServerListener) ---
    @Override
    public void procesarLoginExitoso() {
        eventPublisher.publishEvent(new LoginSuccessEvent(this));
    }

    @Override
    public void procesarFalloDeLogin(String mensajeError) {
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
    public void solicitarChatDirecto(String username) {
        networkOutputPort.enviarSolicitudChatDirecto(username);
    }
    @Override
    public void procesarMensajeRecibido(Message message) {
        // Lógica para procesar un mensaje entrante (se implementará más adelante)
    }
}