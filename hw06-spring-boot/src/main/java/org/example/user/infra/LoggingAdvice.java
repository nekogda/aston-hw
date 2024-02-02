package org.example.user.infra;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@Slf4j
public class LoggingAdvice {

    private final static String MSG = "%s called with args: %s";

    @Before("@annotation(a)")
    public void logBefore(JoinPoint jp, Log a) {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Consumer<String> logMethod = switch (a.value()) {
            case ERROR -> log::error;
            case DEBUG -> log::debug;
            case INFO -> log::info;
            case TRACE -> log::trace;
        };
        logMethod.accept(String.format(MSG, signature.toShortString(),
                formatArgs(signature.getParameterNames(), jp.getArgs())));
    }

    private String formatArgs(String[] parameterNames, Object[] args) {
        return IntStream
                .range(0, parameterNames.length)
                .mapToObj(i -> parameterNames[i] + "=" + args[i])
                .collect(Collectors.joining(", "));
    }
}