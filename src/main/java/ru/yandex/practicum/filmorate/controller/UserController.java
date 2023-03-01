package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private Integer id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) {
        Validator.validateUser(user);
        if (!users.containsValue(user) && user.getId() == 0) {
            id++;
            user.setId(id);
            users.put(id, user);
            return user;
        } else {
            User existingUser = findUser(user);
            log.debug("Пользователь {} уже существует под индексом {}", existingUser.getName(), existingUser.getId());
            return findUser(existingUser);
        }
    }

    @PutMapping("/users")
    public User putUser(@Valid @RequestBody User user) {
        Validator.validateUser(user);
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
        } else {
            log.debug("Пользователь {} не найден", user.getName());
            throw new UserNotFoundException("Пользователь не найден");
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> getAllFilms() {
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
