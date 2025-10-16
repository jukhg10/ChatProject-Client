package com.arquitectura.logica;

import com.arquitectura.entidades.Message;
import com.arquitectura.entidades.TextMessage;
import com.arquitectura.entidades.User;
import com.arquitectura.persistence.MessageRepository;
import com.arquitectura.persistence.data.MessageEntity;
import com.arquitectura.persistence.mappers.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements IMessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public Message guardarMensajeTexto(String content, User author, boolean isOwnMessage) {
        // 1. Crear el objeto de dominio puro
        TextMessage domainMessage = new TextMessage(author, isOwnMessage, content);

        // 2. Mapear el objeto de dominio a una entidad de persistencia
        MessageEntity entityToSave = MessageMapper.toEntity(domainMessage);

        // 3. Guardar la entidad usando el repositorio
        MessageEntity savedEntity = messageRepository.save(entityToSave);

        // 4. Mapear el resultado de vuelta a un objeto de dominio puro y devolverlo
        return MessageMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> obtenerHistorialDeMensajes(int userId) {
        // 1. Obtener la lista de entidades de persistencia desde el repositorio
        List<MessageEntity> entities = messageRepository.findByAuthorId(userId);

        // 2. Usar un stream para mapear cada entidad de vuelta a un objeto de dominio puro
        return entities.stream()
                       .map(MessageMapper::toDomain)
                       .collect(Collectors.toList());
    }
}