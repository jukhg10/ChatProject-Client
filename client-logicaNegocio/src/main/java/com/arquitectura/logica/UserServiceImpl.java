package com.arquitectura.logica;

import com.arquitectura.entidades.User;
import com.arquitectura.persistence.UserRepository;
import com.arquitectura.persistence.data.UserEntity;
import com.arquitectura.persistence.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User guardarUsuario(User user) {
        if (userRepository.findById(user.getId()).isEmpty()) {
            UserEntity userEntity = UserMapper.toEntity(user);
            userRepository.save(userEntity);
        }
        return user;
    }
}