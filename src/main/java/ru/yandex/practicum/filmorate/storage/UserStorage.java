package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void createUser(User user);

    User readUser(int id);

    void updateUser(User user);

    void deleteUserByID(int id);

    void addUserFriend(int userId, int friendId);

    List<User> getUserFriends(int userId);

    List<User> getCommonFriends(int userId, int friendId);

    boolean deleteUserFriend(int userId, int friendId);

    List<User> getAllUsers();
}
