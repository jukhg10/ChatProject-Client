package com.arquitectura.dto;

/**
 * DTO specifically for displaying user information in the view.
 * It only contains the data the UI needs.
 */
public class UserViewDTO {
    private final int id;
    private final String username;

    public UserViewDTO(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "UserViewDTO{" +
               "id=" + id +
               ", username='" + username + '\'' +
               '}';
    }
}