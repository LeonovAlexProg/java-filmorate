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
        if (Validator.validateUser(user)) {
            if (users.containsValue(user) && users.containsKey(user.getId())) {
                log.debug("Пользователь {} уже существует", user.getName());
                throw new UserExistsException("Пользователь  уже существует");
            } else {
                users.put(user.getId(), user);
                log.debug("Пользователь {} был добавлен под индексом {}", user.getName(), user.getId());
                return user;
            }
        } else {
            log.debug("Неправильный формат данных пользователя");
            throw new ValidationException();
        }
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) throws ValidationException {
        if (Validator.validateUser(user)) {
            if (users.containsKey(user.getId())) {
                log.debug("Пользователь под индексом {} был заменён", user.getId());
                users.replace(user.getId(), user);
            } else {
                log.debug("Пользователь {} был добавлен под индексом {}", user.getName(), user.getId());
                users.put(user.getId(), user);
            }
            return user;
        } else {
            log.debug("Неправильный формат данных пользователя");
            throw new ValidationException();
        }
    }

    @GetMapping("/users")
    public List<User> getAllFilms() throws UserNotFoundException {
        if (!users.isEmpty()) {
            return new ArrayList<>(users.values());
        } else {
            throw new UserNotFoundException("Список фильмов пуст");
        }
    }
}
