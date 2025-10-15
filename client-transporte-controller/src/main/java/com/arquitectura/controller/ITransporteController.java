package com.arquitectura.controller;

// FIX THE IMPORT STATEMENTS
import com.arquitectura.dto.UserDTO;
import com.arquitectura.dto.MessageDTO;

// This is the interface that usará la Lógica de Negocio
public interface ITransporteController {

    void enviarComandoLogin(UserDTO user);
    
    void enviarComandoSendMessage(MessageDTO message);
}