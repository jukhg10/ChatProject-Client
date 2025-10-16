package com.arquitectura.entidades;

// No jakarta.persistence imports
public class Channel {

    private int id;
    private String name;

    public Channel() {}

    // --- Getters y Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}