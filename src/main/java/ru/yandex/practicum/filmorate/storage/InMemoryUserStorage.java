package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmExistsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Integer id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public void createUser(User user) {
        if (!users.containsValue(user) && user.getId() == 0) {
            id++;
            user.setId(id);
            users.put(id, user);
        } else {
            throw new FilmExistsException("Film already exists", user.getId(), user.getName());
        }
    }

    @Override
    public User readUser(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new UserNotFoundException("User is not found", id);
        }
    }

    @Override
    public void updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
        } else {
            throw new UserNotFoundException("User is not found", user.getId());
        }
    }

    @Override
    public void deleteUser(User user) {
        if (users.remove(user.getId()) == null) {
            throw new UserNotFoundException("User is not found", user.getId());
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
