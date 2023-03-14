package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFriendExistsException extends RuntimeException{

    public UserFriendExistsException(String message) {
        super(message);
    }
}
