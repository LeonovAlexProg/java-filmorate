package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserFriendNotFoundException extends RuntimeException {
    private final String param;

    public UserFriendNotFoundException(String message, String param) {
        super(message);
        this.param = param;
    }
}
