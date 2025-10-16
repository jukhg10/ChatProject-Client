package com.arquitectura.transporte;

import com.arquitectura.dto.SendMessageRequestDto;
import com.arquitectura.dto.MessageDTO;
import com.arquitectura.dto.UserDTO;
import com.arquitectura.logica.ports.INetworkOutputPort;
import org.springframework.stereotype.Component;
import com.arquitectura.dto.SendMessageRequestDto;
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
        // Establecemos un tiempo de espera de 5 segundos para la conexión
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Conectado al servidor en " + serverAddress + ":" + port);
    }
    
    // Este método es clave: devuelve el BufferedReader para que el ServerListener pueda leerlo.
    public BufferedReader getInputStream() {
        return in;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    // --- Implementación de INetworkOutputPort ---

    @Override
    public void enviarSolicitudLogin(UserDTO user) {
        // Formateamos el mensaje según el protocolo definido en el servidor.
        sendMessage("LOGIN;" + user.getUsername() + ";" + user.getPassword());
    }

    @Override
    public void enviarSolicitudListaUsuarios() {
        sendMessage("OBTENER_USUARIOS");
    }
    @Override
    public void enviarSolicitudListaCanales() {
        // Asumimos que el servidor entenderá este comando.
        // Si el comando es diferente, solo necesitas cambiar este string.
        sendMessage("OBTENER_MIS_CANALES");
    }
    @Override
    public void enviarSolicitudMensaje(MessageDTO message) {
        sendMessage("MSG;" + message.getChannelId() + ";" + message.getContent());
    }
    @Override
    public void enviarSolicitudCrearCanal(String channelName) {
        // Formato del mensaje según el RequestDispatcher del servidor
        sendMessage("CREAR_CANAL_GRUPO;" + channelName);
    }
    @Override
    public void enviarSolicitudChatDirecto(String username) {
        // Este comando es hipotético. Deberás asegurarte de que el servidor
        // tenga una lógica para manejar "CREAR_CANAL_DIRECTO" o un comando similar.
        sendMessage("CREAR_CANAL_DIRECTO;" + username);
    }
    @Override
    public void enviarSolicitudHistorial(int channelId) {
        sendMessage("GET_HISTORY;" + channelId);
    }

    @Override
    public void enviarSolicitudMensajeTexto(SendMessageRequestDto requestDto) {
        sendMessage("ENVIAR_MENSAJE_TEXTO;" + requestDto.getChannelId() + ";" + requestDto.getContent());
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
    
    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            in.close();
            out.close();
            socket.close();
            System.out.println("Desconectado del servidor.");
        }
    }
}