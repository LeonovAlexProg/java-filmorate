package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    void createUser(User user);

    User readUser(int id);

    void updateUser(User user);

    void deleteUser(User user);
}
