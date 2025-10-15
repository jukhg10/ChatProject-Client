package com.arquitectura.logica;

import com.arquitectura.entidades.Message;
import com.arquitectura.entidades.TextMessage;
import com.arquitectura.entidades.User;
import com.arquitectura.persistence.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Marks this as a Spring service component
public class MessageServiceImpl implements IMessageService {

    private final MessageRepository messageRepository;

    // Spring will automatically inject the MessageRepository we created earlier
    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional // Ensures the database operation is handled safely
    public Message guardarMensajeTexto(String content, User author, boolean isOwnMessage) {
        TextMessage newMessage = new TextMessage(author, isOwnMessage, content);
        return messageRepository.save(newMessage);
    }

    @Override
    @Transactional(readOnly = true) // Optimizes the transaction for read-only operations
    public List<Message> obtenerHistorialDeMensajes(int userId) {
        return messageRepository.findByAuthorId(userId);
    }
}