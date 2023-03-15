package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class LikeExistsException extends RuntimeException {
    public LikeExistsException(String message) {
        super(message);
    }
}
