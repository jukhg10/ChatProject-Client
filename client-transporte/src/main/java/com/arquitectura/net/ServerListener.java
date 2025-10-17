package com.arquitectura.net;
import com.arquitectura.dto.MessageViewDTO;
import com.arquitectura.dto.ChannelViewDTO;
import com.arquitectura.dto.MessageDTO;
import com.arquitectura.dto.UserViewDTO;
import com.arquitectura.dto.events.MessageHistoryEvent;
import com.arquitectura.logica.ports.INetworkInputPort;
import com.arquitectura.net.util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import org.springframework.stereotype.Component;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher; 
import com.arquitectura.dto.events.MessageHistoryEvent;
import javax.swing.SwingUtilities;

@Component
public class ServerListener implements Runnable {

    private BufferedReader in;
    private volatile boolean running = true;
    private INetworkInputPort networkInputPort;
    private final Gson gson;
    private final ApplicationEventPublisher eventPublisher;

    public ServerListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;

        // 2. FIX: Build the Gson object and assign it to the field.
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        this.gson = gsonBuilder.create();
    }

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

        String[] parts = response.split(";", 4);
        String status = parts[0].toUpperCase();
        String command = (parts.length > 1) ? parts[1].toUpperCase() : "";
        if ("OK".equals(status) && ("OBTENER_USUARIOS".equals(command) || "OBTENER_MIS_CANALES".equals(command))) {
        System.out.println("🔍 SERVER DATA: Raw data for " + command + " -> " + (parts.length > 2 ? parts[2] : "NO DATA"));
    }
        if ("OK".equals(status)) {
            switch (command) {
                case "LOGIN_SUCCESS":
                    if (parts.length >= 4) {
                        int userId = Integer.parseInt(parts[2]);
                        String username = parts[3];
                        networkInputPort.procesarLoginExitoso(userId, username);
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
                    case "ENVIAR_MENSAJE_TEXTO":
                // No se necesita hacer nada, solo es una confirmación.
                break;
                case "OBTENER_MIS_CANALES":
                    String jsonCanales = (parts.length > 2) ? parts[2] : "[]";
                    Type channelListType = new TypeToken<ArrayList<ChannelViewDTO>>(){}.getType();
                    List<ChannelViewDTO> channels = gson.fromJson(jsonCanales, channelListType);
                    networkInputPort.procesarListaDeCanales(channels);
                    break;

                
                case "DESCARGAR_ARCHIVO":
    if (parts.length >= 4) {
        try {
            String fileName = parts[2];
            String base64Data = parts[3];

            // 1. Decodificar la información Base64 a bytes
            byte[] audioBytes = java.util.Base64.getDecoder().decode(base64Data);

            // 2. Guardar el archivo en una carpeta temporal del sistema
            java.io.File tempDir = new java.io.File(System.getProperty("java.io.tmpdir"), "chat_audio");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            java.io.File tempFile = new java.io.File(tempDir, fileName);
            java.nio.file.Files.write(tempFile.toPath(), audioBytes);

            System.out.println("Audio descargado en: " + tempFile.getAbsolutePath());

            // 3. Publicar un evento para que la UI sepa que el archivo está listo
            eventPublisher.publishEvent(new com.arquitectura.dto.events.FileDownloadEvent(this, tempFile.getAbsolutePath()));

        } catch (java.io.IOException e) {
            System.err.println("Fallo al guardar el archivo de audio descargado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    break;
    case "ENVIAR_MENSAJE_AUDIO":
    // No se necesita hacer nada, solo es una confirmación de que llegó.
    // Esto evita el mensaje de "Comando exitoso desconocido".
    break;
                case "GET_HISTORY":
                    // El formato del servidor es: OK;GET_HISTORY;{channelId};{jsonHistory}
                    if (parts.length >= 4) {
                        int channelId = Integer.parseInt(parts[2]); // El channelId es la parte 3
                        String jsonMessages = parts[3]; // El JSON es la parte 4

                        Type messageListType = new TypeToken<ArrayList<MessageViewDTO>>(){}.getType();
                        List<MessageViewDTO> messages = gson.fromJson(jsonMessages, messageListType);
                        
                        // En lugar de llamar a networkInputPort directamente, publicamos un evento
                        // para desacoplar las capas.
                        eventPublisher.publishEvent(new MessageHistoryEvent(this, channelId, messages != null ? messages : new ArrayList<>()));
                    }
                    break;

                case "CREAR_CANAL_GRUPO": 
                if (parts.length >= 4) { // We need at least 4 parts: OK, COMMAND, ID, NAME
                    int channelId = Integer.parseInt(parts[2]);
                    String channelName = parts[3];
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
        } else if ("EVENTO".equals(status)) {
             String eventType = (parts.length > 1) ? parts[1].toUpperCase() : "";
             switch(eventType) {
                case "NUEVO_MENSAJE":
                    String jsonData = (parts.length > 2) ? parts[2] : "{}"; // Extract the JSON data from the parts array
                    MessageViewDTO newMessage = gson.fromJson(jsonData, MessageViewDTO.class);
                    if (newMessage != null) {
                        networkInputPort.procesarMensajeRecibido(newMessage);
                    }
                    break;
                case "NUEVA_INVITACION":
                    String jsonInvitacion = (parts.length > 2) ? parts[2] : "{}";
                    ChannelViewDTO newInvitation = gson.fromJson(jsonInvitacion, ChannelViewDTO.class);
                    if (newInvitation != null) {
                        // We can reuse the channel list update logic for a single new channel
                        networkInputPort.procesarListaDeInvitaciones(new ArrayList<>(List.of(newInvitation)));
                    }
                    break;

             }
        }else if ("ERROR".equals(status)) {
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