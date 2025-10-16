package com.arquitectura.persistence;

import com.arquitectura.persistence.data.MessageEntity; // <-- IMPORT MessageEntity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
// The repository works with MessageEntity, not the pure Message object
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    // Spring Data JPA understands this method name:
    // Find where the 'author' field's 'id' property matches
    List<MessageEntity> findByAuthorId(int authorId);
}