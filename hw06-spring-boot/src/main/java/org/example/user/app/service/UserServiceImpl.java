package org.example.user.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user.app.domain.model.User;
import org.example.user.app.domain.service.HashManager;
import org.example.user.app.exception.UserAppAccessDeniedException;
import org.example.user.app.exception.UserAppNotFoundException;
import org.example.user.app.exception.UserAppValidationException;
import org.example.user.app.port.in.UserService;
import org.example.user.app.port.out.UserRepository;
import org.example.user.infra.Log;
import org.example.user.infra.LogLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final HashManager hashManager;
    private final UserRepository repository;

    @Override
    @Transactional
    @Log
    public User create(String login, String password) {
        Validator.from(validateLogin(login))
                .and(validatePassword(password))
                .run();

        User newUser = User.from(login, hashManager.toHash(password));

        if (repository.existsByLogin(login)) {
            throw new UserAppValidationException("login occupied");
        }
        return repository.save(newUser);
    }

    @Override
    @Transactional
    @Log
    public void changePassword(String login, String oldPassword, String newPassword) {
        Validator.from(validateLogin(login))
                .and(validatePassword(oldPassword, "oldPassword"))
                .and(validatePassword(newPassword, "newPassword"))
                .run();

        User user = repository.findByLogin(login)
                .orElseThrow(() -> new UserAppNotFoundException("user not found"));

        if (userPasswordIsMatched(oldPassword, user.getPassword())) {
            user.setPassword(hashManager.toHash(newPassword));
        } else {
            throw new UserAppAccessDeniedException("old password doesn't match");
        }
    }

    private boolean userPasswordIsMatched(String oldPassword, String currentPassword) {
        return Objects.equals(currentPassword, hashManager.toHash(oldPassword));
    }

    @Override
    @Log
    public void logIn(String login, String password) {
        Validator.from(validateLogin(login))
                .and(validatePassword(password))
                .run();

        User user = repository.findByLogin(login)
                .orElseThrow(() -> new UserAppAccessDeniedException("unknown user or password"));

        if (!userPasswordIsMatched(password, user.getPassword())) {
            throw new UserAppAccessDeniedException("unknown user or password");
        }
    }

    @Override
    @Log(LogLevel.DEBUG)
    public User get(String login) {
        Validator.from(validateLogin(login)).run();

        return repository.findByLogin(login)
                .orElseThrow(() -> new UserAppNotFoundException("user not found"));
    }

    @Override
    @Log(LogLevel.DEBUG)
    public List<User> getAll() {
        return repository.findAll();
    }

    private Supplier<String> validateLogin(String login) {
        return () -> {
            if (login != null && login.matches("[a-zA-Z]{1,10}")) {
                return null;
            }
            return "login";
        };
    }

    private Supplier<String> validatePassword(String password) {
        return validatePassword(password, "password");
    }

    private Supplier<String> validatePassword(String password, String fieldName) {
        return () -> {
            if (password != null && password.length() < 100 && !password.isBlank()) {
                return null;
            }
            return fieldName;
        };
    }

    private static class Validator {
        private final List<Supplier<String>> messageSuppliers = new ArrayList<>();

        public static Validator from(Supplier<String> fieldValidator) {
            return new Validator().and(fieldValidator);
        }

        public Validator and(Supplier<String> fieldValidator) {
            messageSuppliers.add(fieldValidator);
            return this;
        }

        public void run() {
            List<String> violations = messageSuppliers
                    .stream()
                    .map(Supplier::get)
                    .filter(Objects::nonNull)
                    .toList();

            if (!violations.isEmpty()) {
                throw new UserAppValidationException(
                        "validation error: " + String.join(", ", violations));
            }
        }
    }
}