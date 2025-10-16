package com.arquitectura.net;
import com.arquitectura.dto.MessageViewDTO;
import com.arquitectura.dto.ChannelViewDTO;
import com.arquitectura.dto.UserViewDTO;
import com.arquitectura.logica.ports.INetworkInputPort;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServerListener implements Runnable {

    private BufferedReader in;
    private volatile boolean running = true;
    private INetworkInputPort networkInputPort;
    private final Gson gson = new Gson(); // Definimos Gson una sola vez

    public ServerListener() {}

    public void setInputStream(BufferedReader in) {
        this.in = in;
    }
    
    public void setNetworkInputPort(INetworkInputPort networkInputPort) {
        this.networkInputPort = networkInputPort;
    }

    @Override
    public void run() {
        try {
            String serverResponse;
            while (running && (serverResponse = in.readLine()) != null) {
                System.out.println("DESDE EL SERVIDOR: " + serverResponse);
                
                final String finalResponse = serverResponse;
                Platform.runLater(() -> parseAndDelegate(finalResponse));
            }
        } catch (IOException e) {
            if (running) {
                Platform.runLater(() -> networkInputPort.procesarFalloDeLogin("Se perdió la conexión con el servidor."));
            }
        }
    }
    
    private void parseAndDelegate(String response) {
        if (networkInputPort == null) return;

        String[] parts = response.split(";", 3);
        String status = parts[0].toUpperCase();
        String command = (parts.length > 1) ? parts[1].toUpperCase() : "";

        if ("OK".equals(status)) {
            switch (command) {
                case "LOGIN_SUCCESS":
                    networkInputPort.procesarLoginExitoso();
                    break;
                case "NUEVO_MENSAJE":
                String jsonData = (parts.length > 2) ? parts[2] : "{}"; // Extract the JSON data from the parts array
                MessageViewDTO newMessage = gson.fromJson(jsonData, MessageViewDTO.class);
                if (newMessage != null) {
                    networkInputPort.procesarMensajeRecibido(newMessage);
                }
                break;
                case "OBTENER_USUARIOS":
                    String jsonUsuarios = (parts.length > 2) ? parts[2] : "[]";
                    Type userListType = new TypeToken<ArrayList<UserViewDTO>>(){}.getType();
                    List<UserViewDTO> users = gson.fromJson(jsonUsuarios, userListType);
                    networkInputPort.procesarListaDeUsuarios(users);
                    break;

                case "OBTENER_MIS_INVITACIONES":
                    String jsonInvitaciones = (parts.length > 2) ? parts[2] : "[]";
                    Type invitationListType = new TypeToken<ArrayList<ChannelViewDTO>>(){}.getType();
                    List<ChannelViewDTO> invitaciones = gson.fromJson(jsonInvitaciones, invitationListType);
                    networkInputPort.procesarListaDeInvitaciones(invitaciones);
                    break;
                    
                case "OBTENER_CANALES":
                    String jsonCanales = (parts.length > 2) ? parts[2] : "[]";
                    Type channelListType = new TypeToken<ArrayList<ChannelViewDTO>>(){}.getType();
                    List<ChannelViewDTO> channels = gson.fromJson(jsonCanales, channelListType);
                    networkInputPort.procesarListaDeCanales(channels);
                    break;
                
                case "GET_HISTORY": // <-- NUEVO CASO
                String jsonMessages = (parts.length > 2) ? parts[2] : "[]";
                // Usamos MessageViewDTO porque es compatible con la estructura JSON que envía el servidor
                Type messageListType = new TypeToken<ArrayList<MessageViewDTO>>(){}.getType();
                List<MessageViewDTO> messages = gson.fromJson(jsonMessages, messageListType);
                networkInputPort.procesarHistorialMensajes(messages != null ? messages : new ArrayList<>());
                break;

                case "CREAR_CANAL_GRUPO": 
                if (parts.length > 2) {
                    String[] channelData = parts[2].split(";", 2);
                    int channelId = Integer.parseInt(channelData[0]);
                    String channelName = channelData[1];
                    ChannelViewDTO newChannel = new ChannelViewDTO(channelId, channelName);
                    networkInputPort.procesarNuevoCanal(newChannel);
                }
                break;
                case "CREAR_CANAL_DIRECTO": 
                if (parts.length > 2) {
                    // Formato esperado: {id};{nombre}
                    String[] channelData = parts[2].split(";", 2);
                    if (channelData.length == 2) {
                        int channelId = Integer.parseInt(channelData[0]);
                        String channelName = channelData[1];
                        ChannelViewDTO newChannel = new ChannelViewDTO(channelId, channelName);
                        networkInputPort.procesarNuevoCanal(newChannel);
                    }
                }
                break;
                default:
                    System.out.println("Comando exitoso desconocido: " + command);
            }
        } else if ("ERROR".equals(status)) {
            String errorMessage = (parts.length > 2) ? parts[2] : "Error desconocido del servidor.";
            networkInputPort.procesarFalloDeLogin(errorMessage);
        }
    }

    public void stop() {
        running = false;
        try {
            if (in != null) in.close();
        } catch (IOException e) {
            // Ignorar al cerrar
        }
    }
}