package com.arquitectura.logica.ports;

import com.arquitectura.entidades.Message;
import com.arquitectura.entidades.User;
import com.arquitectura.entidades.Channel;
import java.util.List;

/**
 * Puerto de entrada que define cómo las capas externas 
 * (como el controlador de red) entregan datos a la lógica de negocio.
 */
public interface INetworkInputPort {
    void procesarListaDeUsuarios(List<User> users);
    void procesarMensajeRecibido(Message message);
    void procesarListaDeCanales(List<Channel> channels);
}