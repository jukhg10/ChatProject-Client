package com.arquitectura.dto;

/**
 * DTO specifically for displaying user information in the view.
 * It only contains the data the UI needs.
 */
public class UserViewDTO {
    // FIX: Rename 'id' to 'userId' for clarity and consistency
    private final int userId; 
    private final String username;

    public UserViewDTO(int userId, String username) { // <-- Update parameter
        this.userId = userId; // <-- Update assignment
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "UserViewDTO{" +
               "userId=" + userId + // <-- Update for logging
               ", username='" + username + '\'' +
               '}';
    }
}