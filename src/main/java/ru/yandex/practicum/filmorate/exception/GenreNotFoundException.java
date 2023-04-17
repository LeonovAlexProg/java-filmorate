package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends RuntimeException {
    private final int id;

    public GenreNotFoundException(String message, int id) {
        super(message);
        this.id = id;
    }
}
