package com.arquitectura.logica.ports;

import com.arquitectura.dto.UserDTO; 
import com.arquitectura.dto.MessageDTO;
import com.arquitectura.dto.SendMessageRequestDto; 

public interface INetworkOutputPort {
    void enviarSolicitudLogin(UserDTO user);
    void enviarSolicitudMensaje(MessageDTO message);
    void enviarSolicitudListaUsuarios();
    void enviarSolicitudListaCanales();
    void enviarSolicitudCrearCanal(String channelName);
    void enviarSolicitudChatDirecto(String username);
    void enviarSolicitudHistorial(int channelId);
    void enviarSolicitudMensajeTexto(SendMessageRequestDto requestDto);
    void enviarSolicitudInvitaciones();
    void enviarSolicitudResponderInvitacion(int channelId, boolean aceptada);
    void enviarSolicitudMensajeAudio(SendMessageRequestDto requestDto);
}