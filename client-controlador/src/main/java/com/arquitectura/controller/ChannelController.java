package com.arquitectura.controller;

import com.arquitectura.dto.ChannelViewDTO; // Importa desde el paquete DTO
import com.arquitectura.logica.IClienteFachada;
import java.util.List;
import org.springframework.stereotype.Controller;

@Controller
public class ChannelController {

    private final IClienteFachada clienteFachada;

    public ChannelController(IClienteFachada clienteFachada) {
        this.clienteFachada = clienteFachada;
    }

    public void createChannel(String name) {
        // Lógica para crear el canal (pendiente en la fachada)
        System.out.println("Funcionalidad para crear canal pendiente en la fachada.");
    }

    /**
     * Este método será llamado por la capa de lógica para actualizar la vista.
     * @param channels La lista de canales a mostrar.
     */
    public void onChannelListUpdated(List<ChannelViewDTO> channels) {
        System.out.println("CONTROLLER: Actualizando vista con " + channels.size() + " canales.");
    }
}