package com.arquitectura.logica;

import java.util.List;

public interface IClienteFachada {
    void login(String username, String password);
    void logout();
    void solicitarListaUsuarios();
    void solicitarListaCanales();
    void crearCanal(String channelName);
    void crearCanalDirecto(int otherUserId);
    void solicitarHistorialMensajes(int channelId);
    void enviarMensajeTexto(int channelId, String content);
    void invitarUsuario(int channelId, int userIdToInvite);
    void solicitarInvitaciones();
    void responderInvitacion(int channelId, boolean aceptada);
    void enviarMensajeAudio(int channelId, String filePath);
    void descargarArchivo(String relativePath);
}