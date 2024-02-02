package org.example.user.rest.ctrl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user.app.domain.model.User;
import org.example.user.app.port.in.UserService;
import org.example.user.infra.Log;
import org.example.user.infra.LogLevel;
import org.example.user.rest.dto.ChangePasswordRequest;
import org.example.user.rest.dto.CreateUserRequest;
import org.example.user.rest.dto.UserResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "user-api")
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(description = "Создание нового пользователя")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Log(LogLevel.DEBUG)
    public void createUser(@RequestBody CreateUserRequest request) {
        userService.create(request.getLogin(), request.getPassword());
    }

    @Operation(description = "Смена пароля пользователя")
    @PutMapping(value = "/{login}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Log(LogLevel.DEBUG)
    public void changePassword(
            @PathVariable String login,
            @RequestBody ChangePasswordRequest request) {
        userService.changePassword(
                login,
                request.getOldPassword(),
                request.getNewPassword());
    }

    @Operation(description = "Получение информации о пользователе")
    @GetMapping("/{login}")
    @Log(LogLevel.DEBUG)
    public UserResponse get(@PathVariable String login) {
        User user = userService.get(login);
        return UserResponse.from(user.getLogin());
    }

    @Operation(description = "Получение информации о пользователях")
    @GetMapping
    @Log(LogLevel.DEBUG)
    public List<UserResponse> get() {
        log.debug("called");
        return userService
                .getAll()
                .stream()
                .map(User::getLogin)
                .map(UserResponse::from)
                .toList();
    }
}