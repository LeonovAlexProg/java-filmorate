package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class UserFriendNotFoundException extends RuntimeException {
    public UserFriendNotFoundException(String message) {
        super(message);
    }
}
