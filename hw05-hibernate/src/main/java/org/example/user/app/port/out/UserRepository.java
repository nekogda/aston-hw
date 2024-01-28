package org.example.user.app.port.out;

import org.example.user.app.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    User save(User user);

    boolean existsByLogin(String login);

    Optional<User> findByLogin(String login);

    User update(User user);
}