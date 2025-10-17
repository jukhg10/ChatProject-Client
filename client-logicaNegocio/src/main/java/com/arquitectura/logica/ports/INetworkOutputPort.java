package com.arquitectura.logica.ports;

import com.arquitectura.dto.MessageViewDTO;
import com.arquitectura.dto.SendMessageRequestDto;
import com.arquitectura.dto.UserDTO;

import java.util.List;

public interface INetworkOutputPort {
    void enviarSolicitudLogin(UserDTO user);
    void enviarSolicitudLogout();
    void enviarSolicitudListaUsuarios();
    void enviarSolicitudListaCanales();
    void enviarSolicitudCrearCanalGrupo(String channelName);
    void enviarSolicitudCrearCanalDirecto(int otherUserId);
    void enviarSolicitudHistorial(int channelId);
    void enviarSolicitudMensajeTexto(SendMessageRequestDto requestDto);
    void enviarSolicitudInvitarUsuario(int channelId, int userIdToInvite);
    void enviarSolicitudInvitaciones();
 void enviarSolicitudResponderInvitacion(int channelId, boolean aceptada);
    void enviarSolicitudMensajeAudio(SendMessageRequestDto requestDto);
    void enviarSolicitudDescargarArchivo(String relativePath);
void procesarHistorialMensajes(int channelId, List<MessageViewDTO> messages);    
    
}