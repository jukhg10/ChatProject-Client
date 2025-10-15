package com.arquitectura.logica.ports;

import com.arquitectura.dto.UserDTO; 
import com.arquitectura.dto.MessageDTO; 

public interface INetworkOutputPort {
    void enviarSolicitudLogin(UserDTO user);
    void enviarSolicitudMensaje(MessageDTO message);
}