package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    public void createUser(User user);

    public User readUser(int id);

    public void updateUser(User user);

    public void deleteUser(User user);
}
