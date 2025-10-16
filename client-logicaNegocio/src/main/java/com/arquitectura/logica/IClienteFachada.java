package com.arquitectura.logica;

public interface IClienteFachada {
    
    void login(String username, String password);
    void solicitarListaCanales();
    void sendMessage(int channelId, String content);
    void crearCanalGrupo(String channelName);
    void solicitarListaUsuarios();
    void solicitarChatDirecto(String username);
    void solicitarHistorialMensajes(int channelId);
    void enviarMensajeTexto(int channelId, String content);
    void solicitarInvitaciones();
    void responderInvitacion(int channelId, boolean aceptada);
    void enviarMensajeAudio(int channelId, String filePath);
}