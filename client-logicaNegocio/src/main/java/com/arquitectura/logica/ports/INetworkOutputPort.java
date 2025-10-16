package com.arquitectura.logica.ports;

import com.arquitectura.dto.UserDTO; 
import com.arquitectura.dto.MessageDTO; 

public interface INetworkOutputPort {
    void enviarSolicitudLogin(UserDTO user);
    void enviarSolicitudMensaje(MessageDTO message);
    void enviarSolicitudListaUsuarios();
    void enviarSolicitudListaCanales();
    void enviarSolicitudCrearCanal(String channelName);
    void enviarSolicitudChatDirecto(String username);
}