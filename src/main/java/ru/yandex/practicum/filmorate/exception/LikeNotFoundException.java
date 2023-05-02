package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException(String message) {
        super(message);
    }
}
