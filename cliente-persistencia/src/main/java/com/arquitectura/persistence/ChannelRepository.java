package com.arquitectura.persistence;

import com.arquitectura.persistence.data.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity, Integer> {
    // JpaRepository provides all the basic CRUD methods (save, findById, etc.)
}