package org.example.user.app.exception;

public class UserAppDataException extends UserAppException {
    public UserAppDataException(String message) {
        super(message);
    }

    public UserAppDataException(String message, Throwable cause) {
        super(message, cause);
    }
}