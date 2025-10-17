package com.arquitectura.persistence.mappers;

import com.arquitectura.entidades.User;
import com.arquitectura.persistence.data.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(User domain) {
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getUsername());
    }
}