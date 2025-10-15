package com.arquitectura.logica.observers;

import com.arquitectura.entidades.User;
import java.util.List;

// Interfaz que implementarán los controladores interesados en cambios de usuarios.
public interface IUserObserver {
    void onUserListUpdated(List<User> users);
}