package com.arquitectura.controller;

import com.arquitectura.dto.MessageViewDTO; // Importa desde el paquete DTO
import com.arquitectura.logica.IClienteFachada;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final IClienteFachada clienteFachada;

    public ChatController(IClienteFachada clienteFachada) {
        this.clienteFachada = clienteFachada;
    }

    public void sendMessage(int channelId, String content) {
        clienteFachada.sendMessage(channelId, content);
    }

    /**
     * Este método será llamado por la capa de lógica para actualizar la vista.
     * @param message El nuevo mensaje a mostrar.
     */
    public void onMessageReceived(MessageViewDTO message) {
        System.out.println("CONTROLLER: Mostrando nuevo mensaje de: " + message.getAuthorName());
    }
}