package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserExistsException extends RuntimeException {
    private final int id;
    private final String name;

    public UserExistsException(String message, int id, String name) {
        super(message);
        this.id = id;
        this.name = name;
    }
}
