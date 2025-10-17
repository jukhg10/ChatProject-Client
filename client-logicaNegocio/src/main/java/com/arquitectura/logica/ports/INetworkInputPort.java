package com.arquitectura.logica.ports;

import com.arquitectura.dto.ChannelViewDTO; // <-- IMPORT CAMBIADO
import com.arquitectura.dto.MessageViewDTO;
import com.arquitectura.dto.UserViewDTO;
import com.arquitectura.entidades.Message;
import java.util.List;

/**
 * Puerto de entrada que define cómo las capas externas 
 * (como el controlador de red) entregan datos a la lógica de negocio.
 */
public interface INetworkInputPort {
    void procesarListaDeUsuarios(List<UserViewDTO> users); // <-- TIPO CAMBIADO
    void procesarMensajeRecibido(Message message);
    void procesarListaDeCanales(List<ChannelViewDTO> channels); // <-- TIPO CAMBIADO
    void procesarNuevoCanal(ChannelViewDTO channel);
    void procesarMensajeRecibido(MessageViewDTO message);
    void procesarListaDeInvitaciones(List<ChannelViewDTO> invitaciones);
    // Métodos para el flujo de login
    void procesarLoginExitoso(int userId, String username);
    void procesarFalloDeLogin(String mensajeError);
    void procesarHistorialMensajes(int channelId, List<MessageViewDTO> messages);    

}