package ru.yandex.practicum.filmorate.exception;

public class UserExistsException extends Exception{
    public UserExistsException(String message) {
        super(message);
    }
}
