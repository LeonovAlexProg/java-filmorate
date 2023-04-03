package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserFriendExistsException;
import ru.yandex.practicum.filmorate.exception.UserFriendNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDaoImpl") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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

//    public User addUserFriend(int userId, int friendId) {
//        User user = userStorage.readUser(userId);
//        User friend = userStorage.readUser(friendId);
//
//        if (!user.getFriends().add(friend.getId()) & !friend.getFriends().add(user.getId())) {
//            throw new UserFriendExistsException(String.format("Users are already friends, UserId = %d, friendId = %d",
//                    userId, friendId));
//        }
//
//        return user;
//    }
//
//    public void deleteUserFriend(int userId, int friendId) {
//        User user = userStorage.readUser(userId);
//        User friend = userStorage.readUser(friendId);
//
//        if (!user.getFriends().remove(friend.getId()) & !friend.getFriends().remove(user.getId())) {
//            throw new UserFriendNotFoundException(String.format("No such friend, userId = %d, friendId = %d",
//                    userId, friendId));
//        }
//    }
//
//    public List<User> getUserFriends(int userId) {
//        User user = userStorage.readUser(userId);
//
//        return userStorage.getAllUsers().stream()
//                .filter(u -> user.getFriends().contains(u.getId()))
//                .collect(Collectors.toList());
//    }
//
//    public List<User> getCommonFriends(int userId, int friendId) {
//        User user = userStorage.readUser(userId);
//        User friend = userStorage.readUser(friendId);
//
//
//        List<User> commonFriends = new ArrayList<>();
//
//        if (user.getFriends() != null && friend.getFriends() != null) {
//            commonFriends = user.getFriends().stream()
//                    .filter(id -> friend.getFriends().contains(id))
//                    .map(userStorage::readUser)
//                    .collect(Collectors.toList());
//        }
//
//        return commonFriends;
//    }
}
