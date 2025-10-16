package com.arquitectura.persistence.mappers;

import com.arquitectura.entidades.Channel;
import com.arquitectura.persistence.data.ChannelEntity;

public class ChannelMapper {

    // Converts a persistence entity to a pure domain object
    public static Channel toDomain(ChannelEntity entity) {
        if (entity == null) return null;
        Channel domain = new Channel();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        return domain;
    }

    // Converts a pure domain object to a persistence entity
    public static ChannelEntity toEntity(Channel domain) {
        if (domain == null) return null;
        ChannelEntity entity = new ChannelEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        return entity;
    }
}