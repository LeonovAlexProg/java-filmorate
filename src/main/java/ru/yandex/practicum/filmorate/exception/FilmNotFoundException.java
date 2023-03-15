package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmNotFoundException extends RuntimeException{
    private final int id;

    public FilmNotFoundException(String message, int id) {
        super(message);
        this.id = id;
    }
}
