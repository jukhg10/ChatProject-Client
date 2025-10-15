package com.arquitectura.dto;

/**
 * DTO para transportar la información de un canal a la capa de vista.
 */
public class ChannelViewDTO {
    private final int id;
    private final String name;

    public ChannelViewDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ChannelViewDTO{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}