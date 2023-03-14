package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FilmExistsException extends RuntimeException {
    private final int id;
    private final String name;

    public FilmExistsException(String message, int id, String name) {
        super(message);
        this.id = id;
        this.name = name;
    }
}
