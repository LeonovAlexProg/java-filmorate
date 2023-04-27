package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final FeedStorage feedStorage;

    @Qualifier("userDaoImpl")
    public User postUser(User user) {
        userStorage.createUser(user);
        return user;
    }

    public User putUser(User user) {
        userStorage.updateUser(user);
        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(int id) {
        return userStorage.readUser(id);
    }

    public User addUserFriend(int userId, int friendId) {
        userStorage.addUserFriend(userId, friendId);
        feedStorage.updateFeed("FRIEND", "ADD", userId, friendId, Instant.now());
        return getUser(userId);
    }

    public void deleteUserFriend(int userId, int friendId) {
        userStorage.deleteUserFriend(userId, friendId);
        feedStorage.updateFeed("FRIEND", "REMOVE", userId, friendId, Instant.now());
    }

    public List<User> getUserFriends(int userId) {
        return userStorage.getUserFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        return userStorage.getCommonFriends(userId, otherId);
    }

    public List<Film> getUserRecommendations(int userId) {
        return filmStorage.getFilmsRecommendation(userId);
    }

    public void deleteUserByID(int userId) {
        userStorage.deleteUserByID(userId);
    }
}
