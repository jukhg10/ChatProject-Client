package com.arquitectura.controller;

import com.arquitectura.dto.UserViewDTO; // Correct import from the DTO module
import com.arquitectura.logica.IClienteFachada;
import java.util.List;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private final IClienteFachada clienteFachada;

    public UserController(IClienteFachada clienteFachada) {
        this.clienteFachada = clienteFachada;
    }

    public void login(String username, String password) {
        clienteFachada.login(username, password);
    }

    /**
     * This method will be called by the logic layer to update the view.
     * It now correctly uses a DTO, not an entity.
     * @param users The list of users to be displayed.
     */
    public void onUserListUpdated(List<UserViewDTO> users) {
        System.out.println("CONTROLLER: Updating the view with " + users.size() + " users.");
        // In a real UI, you would update a JList or another component here.
    }
}