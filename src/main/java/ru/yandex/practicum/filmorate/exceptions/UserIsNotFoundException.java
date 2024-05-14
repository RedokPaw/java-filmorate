package ru.yandex.practicum.filmorate.exceptions;

public class UserIsNotFoundException extends RuntimeException {
    public UserIsNotFoundException(String message) {
        super(message);
    }
}
