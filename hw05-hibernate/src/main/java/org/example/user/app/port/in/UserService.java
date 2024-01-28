package org.example.user.app.port.in;

import org.example.user.app.domain.model.User;

import java.util.List;

public interface UserService {

    User create(String login, String password);

    void changePassword(String login, String oldPassword, String newPassword);

    void logIn(String login, String password);

    User get(String login);

    List<User> getAll();
}