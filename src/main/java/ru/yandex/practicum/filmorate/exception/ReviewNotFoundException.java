package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewNotFoundException extends RuntimeException{
    private final int id;

    public ReviewNotFoundException(String message, int id) {
        super(message);
        this.id = id;
    }
}