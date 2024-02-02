package org.example.user.rest.ctrl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user.app.port.in.UserService;
import org.example.user.infra.Log;
import org.example.user.infra.LogLevel;
import org.example.user.rest.dto.LogInRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "login-api")
@RequestMapping("/")
@AllArgsConstructor
@Slf4j
public class LogInController {

    private final UserService userService;

    @Operation(description = "Вход")
    @PostMapping("/login")
    @Log(LogLevel.DEBUG)
    public void logIn(@RequestBody LogInRequest request) {
        userService.logIn(
                request.getLogin(),
                request.getPassword());
    }
}