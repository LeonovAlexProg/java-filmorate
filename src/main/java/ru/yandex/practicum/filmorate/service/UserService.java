package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserFriendExistsException;
import ru.yandex.practicum.filmorate.exception.UserFriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public User addUserFriend(int userId, int friendId) {
        User user = inMemoryUserStorage.readUser(userId);
        User friend = inMemoryUserStorage.readUser(friendId);

        if (!user.getFriends().add(friend.getId()) & !friend.getFriends().add(user.getId())) {
            throw new UserFriendExistsException("Users are already at each other friendlist");
        }

        return user;
    }

    public void deleteUserFriend(int userId, int friendId) {
        User user = inMemoryUserStorage.readUser(userId);
        User friend = inMemoryUserStorage.readUser(friendId);

        if (!user.getFriends().remove(friend.getId())) {
            throw new UserFriendNotFoundException("No such friend", String.format("user id = %d, friend id = %d",
                    userId, friendId));
        }
    }

    public List<User> getUserFriends(int userId) {
        User user = inMemoryUserStorage.readUser(userId);

        List<User> userFriends = inMemoryUserStorage.getAllUsers().stream()
                .filter(u -> user.getFriends().contains(u.getId()))
                .collect(Collectors.toList());

        if (userFriends.isEmpty()) {
            throw new UserFriendNotFoundException("Friendlist is empty", String.format("user id = %d", userId));
        }

        return userFriends;
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        User user = inMemoryUserStorage.readUser(userId);
        User friend = inMemoryUserStorage.readUser(friendId);


        List<User> commonFriends = new ArrayList<>();

        if (user.getFriends() != null && friend.getFriends() != null) {
            commonFriends = user.getFriends().stream()
                    .filter(id -> friend.getFriends().contains(id))
                    .map(inMemoryUserStorage::readUser)
                    .collect(Collectors.toList());
        }

        return commonFriends;
    }
}
