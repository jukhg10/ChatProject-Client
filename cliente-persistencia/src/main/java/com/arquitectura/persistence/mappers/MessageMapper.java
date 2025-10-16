package com.arquitectura.persistence.mappers;

import com.arquitectura.entidades.Message;
import com.arquitectura.entidades.TextMessage;
import com.arquitectura.entidades.AudioMessage;
import com.arquitectura.persistence.data.MessageEntity;
import com.arquitectura.persistence.data.TextMessageEntity;
import com.arquitectura.persistence.data.AudioMessageEntity;

public class MessageMapper {

    public static Message toDomain(MessageEntity entity) {
        if (entity == null) return null;

        Message domainMessage;
        if (entity instanceof TextMessageEntity) {
            TextMessage textDomain = new TextMessage();
            textDomain.setContent(((TextMessageEntity) entity).getContent());
            domainMessage = textDomain;
        } else if (entity instanceof AudioMessageEntity) {
            AudioMessage audioDomain = new AudioMessage();
            audioDomain.setAudioUrl(((AudioMessageEntity) entity).getAudioUrl());
            domainMessage = audioDomain;
        } else {
            throw new IllegalArgumentException("Unknown MessageEntity type");
        }

        domainMessage.setId(entity.getId());
        domainMessage.setAuthor(UserMapper.toDomain(entity.getAuthor()));
        domainMessage.setTimestamp(entity.getTimestamp());
        domainMessage.setOwnMessage(entity.isOwnMessage());
        
        return domainMessage;
    }

    public static MessageEntity toEntity(Message domain) {
        if (domain == null) return null;

        MessageEntity entity;
        if (domain instanceof TextMessage) {
            TextMessageEntity textEntity = new TextMessageEntity();
            textEntity.setContent(((TextMessage) domain).getContent());
            entity = textEntity;
        } else if (domain instanceof AudioMessage) {
            AudioMessageEntity audioEntity = new AudioMessageEntity();
            audioEntity.setAudioUrl(((AudioMessage) domain).getAudioUrl());
            entity = audioEntity;
        } else {
            throw new IllegalArgumentException("Unknown Message domain type");
        }

        entity.setId(domain.getId());
        entity.setAuthor(UserMapper.toEntity(domain.getAuthor()));
        entity.setTimestamp(domain.getTimestamp());
        entity.setOwnMessage(domain.isOwnMessage());
        
        return entity;
    }
}