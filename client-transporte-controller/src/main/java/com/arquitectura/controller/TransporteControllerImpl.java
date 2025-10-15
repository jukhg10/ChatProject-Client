package com.arquitectura.controller;

// FIX THE IMPORT STATEMENTS
import com.arquitectura.dto.MessageDTO;
import com.arquitectura.dto.UserDTO;
import com.arquitectura.logica.ports.INetworkOutputPort;
import com.arquitectura.transporte.ServerConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TransporteControllerImpl implements INetworkOutputPort {

    private final ServerConnection serverConnection;

    @Autowired
    public TransporteControllerImpl(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    @Override
    public void enviarSolicitudLogin(UserDTO user) {
        String command = "LOGIN;" + user.getUsername() + ";" + user.getPassword();
        serverConnection.sendMessage(command);
    }

    @Override
    public void enviarSolicitudMensaje(MessageDTO message) {
        String command = "MSG;" + message.getChannelId() + ";" + message.getContent();
        serverConnection.sendMessage(command);
    }
}