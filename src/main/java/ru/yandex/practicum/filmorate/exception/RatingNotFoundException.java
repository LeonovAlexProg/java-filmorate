package ru.yandex.practicum.filmorate.exception;

public class RatingNotFoundException extends RuntimeException{
    private final int id;

    public RatingNotFoundException(String message, int id) {
        super(message);
        this.id = id;
    }
}
