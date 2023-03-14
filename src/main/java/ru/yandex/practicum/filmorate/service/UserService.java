package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User postUser(User user) {
        inMemoryUserStorage.createUser(user);
        return user;
    }

    public User putUser(User user) {
        inMemoryUserStorage.updateUser(user);
        return user;
    }

    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public User getUser(int id) {
        return inMemoryUserStorage.readUser(id);
    }
}
