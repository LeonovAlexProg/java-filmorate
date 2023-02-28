package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserExistsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    Integer id = 0;
    Map<Integer, User> users = new HashMap<>();

    @PostMapping("/users")
    public User postUser(@RequestBody User user) throws UserExistsException, ValidationException {
        Validator.validateUser(user);
        if (!users.containsValue(user) && user.getId() == 0) {
            id++;
            user.setId(id);
            users.put(id, user);
            return user;
        } else {
            log.debug("Пользователь уже существует");
            return findUser(user);
        }
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) throws ValidationException, UserNotFoundException {
        Validator.validateUser(user);
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
        } else {
            log.debug("Пользователь не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> getAllFilms() throws UserNotFoundException {
        if (!users.isEmpty()) {
            return new ArrayList<>(users.values());
        } else {
            throw new UserNotFoundException("Список фильмов пуст");
        }
    }

    private User findUser(User user) {
        for (User currentUser : users.values()) {
            if (currentUser.equals(user))
                user = currentUser;
        }
        return user;
    }
}
