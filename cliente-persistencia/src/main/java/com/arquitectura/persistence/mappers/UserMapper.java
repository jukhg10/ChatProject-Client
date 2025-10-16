package com.arquitectura.persistence.mappers;

import com.arquitectura.entidades.User;
import com.arquitectura.persistence.data.UserEntity;

public class UserMapper {

    // Converts a persistence entity to a pure domain object
    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return new User(entity.getId(), entity.getUsername());
    }

    // Converts a pure domain object to a persistence entity
    public static UserEntity toEntity(User domain) {
        if (domain == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        return entity;
    }
}