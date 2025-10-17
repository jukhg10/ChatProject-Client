package com.arquitectura.logica;

import com.arquitectura.entidades.Message;
import com.arquitectura.entidades.AudioMessage;
import com.arquitectura.entidades.TextMessage;
import com.arquitectura.entidades.User;
import com.arquitectura.persistence.MessageRepository;
import com.arquitectura.persistence.UserRepository; // Import UserRepository
import com.arquitectura.persistence.data.MessageEntity;
import com.arquitectura.persistence.data.UserEntity; // Import UserEntity
import com.arquitectura.persistence.mappers.MessageMapper;
import com.arquitectura.persistence.mappers.UserMapper; // Import UserMapper
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements IMessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) { // Inject UserRepository
        this.messageRepository = messageRepository;
        this.userRepository = userRepository; // Initialize UserRepository
    }

    @Override
    @Transactional
    public Message guardarMensajeTexto(String content, User author, boolean isOwnMessage) {
        // 1. Ensure the user exists in the database
        UserEntity authorEntity = userRepository.findById(author.getId()).orElseGet(() -> {
            UserEntity newUser = UserMapper.toEntity(author);
            return userRepository.save(newUser);
        });

        // 2. Create the domain object
        TextMessage domainMessage = new TextMessage(author, isOwnMessage, content);

        // 3. Map to a persistence entity and set the author
        MessageEntity entityToSave = MessageMapper.toEntity(domainMessage);
        entityToSave.setAuthor(authorEntity);

        // 4. Save the message entity
        MessageEntity savedEntity = messageRepository.save(entityToSave);

        // 5. Map back to a domain object and return
        return MessageMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public Message guardarMensajeAudio(String audioUrl, User author, boolean isOwnMessage) {
        // 1. Ensure the user exists in the database
        UserEntity authorEntity = userRepository.findById(author.getId()).orElseGet(() -> {
            UserEntity newUser = UserMapper.toEntity(author);
            return userRepository.save(newUser);
        });

        // 2. Create the domain object for an audio message
        AudioMessage domainMessage = new AudioMessage(author, isOwnMessage, audioUrl);

        // 3. Map to a persistence entity and set the author
        MessageEntity entityToSave = MessageMapper.toEntity(domainMessage);
        entityToSave.setAuthor(authorEntity);

        // 4. Save the message entity
        MessageEntity savedEntity = messageRepository.save(entityToSave);

        // 5. Map back to a domain object and return
        return MessageMapper.toDomain(savedEntity);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Message> obtenerHistorialDeMensajes(int userId) {
        List<MessageEntity> entities = messageRepository.findByAuthorId(userId);
        return entities.stream()
                       .map(MessageMapper::toDomain)
                       .collect(Collectors.toList());
    }
}