package com.arquitectura.transporte;

import com.arquitectura.dto.SendMessageRequestDto;
import com.arquitectura.dto.UserDTO;
import com.arquitectura.logica.ports.INetworkOutputPort;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Component
public class ServerConnection implements INetworkOutputPort {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ServerConnection() {}

    public void connect(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Conectado al servidor en " + serverAddress + ":" + port);
    }
    
    public BufferedReader getInputStream() {
        return in;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    @Override
    public void enviarSolicitudLogin(UserDTO user) {
        sendMessage("LOGIN;" + user.getUsername() + ";" + user.getPassword());
    }

    @Override
    public void enviarSolicitudLogout() {
        sendMessage("LOGOUT");
    }

    @Override
    public void enviarSolicitudListaUsuarios() {
        sendMessage("OBTENER_USUARIOS");
    }

    @Override
    public void enviarSolicitudListaCanales() {
        sendMessage("OBTENER_MIS_CANALES");
    }

    @Override
    public void enviarSolicitudCrearCanalGrupo(String channelName) {
        sendMessage("CREAR_CANAL_GRUPO;" + channelName);
    }

    @Override
    public void enviarSolicitudCrearCanalDirecto(int otherUserId) {
        sendMessage("CREAR_CANAL_DIRECTO;" + otherUserId);
    }

    @Override
    public void enviarSolicitudHistorial(int channelId) {
        sendMessage("GET_HISTORY;" + channelId);
    }
    @Override
public void procesarHistorialMensajes(int channelId, java.util.List<com.arquitectura.dto.MessageViewDTO> messages) {
    // NOTE: This method is required to satisfy the INetworkOutputPort interface.
    // The actual logic for handling the message history is likely in another
    // part of your application (like the INetworkInputPort).
    // We can leave this method body empty to fix the build.
}
    @Override
    public void enviarSolicitudMensajeTexto(SendMessageRequestDto requestDto) {
        sendMessage("ENVIAR_MENSAJE_TEXTO;" + requestDto.getChannelId() + ";" + requestDto.getContent());
    }   

    @Override
    public void enviarSolicitudInvitarUsuario(int channelId, int userIdToInvite) {
        sendMessage("INVITAR_USUARIO;" + channelId + ";" + userIdToInvite);
    }

    @Override
    public void enviarSolicitudInvitaciones() {
        sendMessage("OBTENER_MIS_INVITACIONES");
    }

    @Override
    public void enviarSolicitudResponderInvitacion(int channelId, boolean aceptada) {
        String respuesta = aceptada ? "ACEPTAR" : "RECHAZAR";
        sendMessage("RESPONDER_INVITACION;" + channelId + ";" + respuesta);
    }

    @Override
    public void enviarSolicitudMensajeAudio(SendMessageRequestDto requestDto) {
        sendMessage("ENVIAR_MENSAJE_AUDIO;" + requestDto.getChannelId() + ";" + requestDto.getContent());
    }
    
    @Override
    public void enviarSolicitudDescargarArchivo(String relativePath) {
        sendMessage("DESCARGAR_ARCHIVO;" + relativePath);
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            in.close();
            out.close();
            socket.close();
            System.out.println("Desconectado del servidor.");
        }
    }
}