package com.arquitectura.logica;

import com.arquitectura.entidades.Message;
import com.arquitectura.entidades.User;

import java.util.List;

public interface IMessageService {

    /**
     * Saves a new text message to the local database.
     * @param content The text of the message.
     * @param author The user who sent the message.
     * @param isOwnMessage True if the current user sent the message, false otherwise.
     * @return The saved message entity.
     */
    Message guardarMensajeTexto(String content, User author, boolean isOwnMessage);

    /**
     * Saves a new audio message to the local database.
     * @param audioUrl The path or URL to the audio file.
     * @param author The user who sent the message.
     * @param isOwnMessage True if the current user sent the message.
     * @return The saved message entity.
     */
    Message guardarMensajeAudio(String audioUrl, User author, boolean isOwnMessage);


    /**
     * Retrieves all messages for a specific user from the local database.
     * @param userId The ID of the user whose messages we want to retrieve.
     * @return A list of messages.
     */
    List<Message> obtenerHistorialDeMensajes(int userId);
}