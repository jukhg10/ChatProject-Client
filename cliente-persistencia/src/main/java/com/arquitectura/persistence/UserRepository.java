package com.arquitectura.persistence;

import com.arquitectura.entidades.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Tells Spring that this is a repository bean
public interface UserRepository extends JpaRepository<User, Integer> {

    // Spring Data JPA will automatically create a query based on the method name
    // This will find a user by their username
    Optional<User> findByUsername(String username);
}