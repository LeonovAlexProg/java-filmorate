package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Validator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) {
        Validator.validateUser(user);
        return userService.postUser(user);
    }

    @PutMapping("/users")
    public User putUser(@Valid @RequestBody User user) {
        Validator.validateUser(user);
        return userService.putUser(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addUserFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addUserFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteUserFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteUserFriend(id, friendId);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUserByID(@PathVariable int userId) {
        userService.deleteUserByID(userId);
    }
    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable(name = "otherId") int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/users/{id}/recommendations")
    public List<Film> getUserFilmRecommendations(@PathVariable int id) {
        return userService.getUserRecommendations(id);
    }
}
