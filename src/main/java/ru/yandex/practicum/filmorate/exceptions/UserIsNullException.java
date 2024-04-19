package ru.yandex.practicum.filmorate.exceptions;

public class UserIsNullException extends RuntimeException{
    public UserIsNullException(String message) {
        super(message);
    }
}
