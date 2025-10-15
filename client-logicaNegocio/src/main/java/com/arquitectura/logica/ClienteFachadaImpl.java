package com.arquitectura.logica;

import com.arquitectura.dto.MessageDTO;
import com.arquitectura.dto.UserDTO;
import com.arquitectura.entidades.Channel; // Import Channel
import com.arquitectura.entidades.Message; // Import Message
import com.arquitectura.entidades.User;    // Import User
import com.arquitectura.logica.ports.INetworkInputPort;
import com.arquitectura.logica.ports.INetworkOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List; // Import List

@Service
// Make the facade implement both the input and output ports
public class ClienteFachadaImpl implements IClienteFachada, INetworkInputPort {

    private final INetworkOutputPort networkOutputPort;

    @Autowired
    public ClienteFachadaImpl(INetworkOutputPort networkOutputPort) {
        this.networkOutputPort = networkOutputPort;
    }

    // --- IClienteFachada Methods (Output to network) ---

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

    // --- INetworkInputPort Methods (Input from network) ---

    @Override
    public void procesarListaDeUsuarios(List<User> users) {
        System.out.println("LOGIC: Received user list with " + users.size() + " users.");
        // Later, this will notify the UI controllers.
    }

    @Override
    public void procesarMensajeRecibido(Message message) {
        System.out.println("LOGIC: Received new message from author: " + message.getAuthor().getUsername());
        // Later, this will save the message to the local DB and notify the UI.
    }

    @Override
    public void procesarListaDeCanales(List<Channel> channels) {
        System.out.println("LOGIC: Received channel list with " + channels.size() + " channels.");
        // Later, this will notify the UI controllers.
    }
}