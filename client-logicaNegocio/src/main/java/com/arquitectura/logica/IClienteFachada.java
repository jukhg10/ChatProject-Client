package com.arquitectura.logica;

public interface IClienteFachada {
    
    void login(String username, String password);
    
    void sendMessage(int channelId, String content);
    
}