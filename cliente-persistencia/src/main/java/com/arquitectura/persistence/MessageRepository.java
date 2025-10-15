package com.arquitectura.persistence;

import com.arquitectura.entidades.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    // This method will find all messages belonging to a specific author
    List<Message> findByAuthorId(int authorId);

}