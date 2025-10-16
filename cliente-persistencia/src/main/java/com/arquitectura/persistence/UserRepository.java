package com.arquitectura.persistence;

import com.arquitectura.persistence.data.UserEntity; // <-- IMPORT UserEntity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// The repository now works with UserEntity, not the pure User object
public interface UserRepository extends JpaRepository<UserEntity, Integer> { 

    // This method name automatically creates a "find by username" query
    Optional<UserEntity> findByUsername(String username);
}