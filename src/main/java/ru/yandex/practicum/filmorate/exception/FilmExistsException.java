package ru.yandex.practicum.filmorate.exception;

public class FilmExistsException extends RuntimeException {
    public FilmExistsException(String message) {
        super(message);
    }
}
