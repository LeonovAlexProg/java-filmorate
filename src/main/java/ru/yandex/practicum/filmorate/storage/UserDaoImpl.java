package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("userDaoImpl")
public class UserDaoImpl implements UserStorage{

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUser(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlQuery, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            if (user.getName() != null)
                ps.setString(3, user.getName());
            else
                ps.setNull(3, Types.NULL);
            ps.setObject(4, user.getBirthday());

            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());
    }

    @Override
    public User readUser(int id) {
        String sqlQuery = "SELECT user_id, email, name, login, birthday FROM users " +
                "WHERE user_id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException exc) {
            log.debug("User id {} not found", id);
            throw new UserNotFoundException("User not found", id);
        }
    }

    @Override
    public void updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?," +
                "login = ?," +
                "name = ?," +
                "birthday = ?" +
                "WHERE user_id = ?";

        int rowsAffected = jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());

        if (rowsAffected == 0) {
            log.debug("User id {} not found", user.getId());
            throw new UserNotFoundException("User not found", user.getId());
        }
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT user_id, email, name, login, birthday FROM users";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public void addUserFriend(int userId, int friendId) {
        User user = readUser(userId);
        User friend = readUser(friendId);

        boolean callbackRequest = false;

        String sqlQueryGetStatus = "SELECT applied FROM user_friends " +
                "WHERE user_id = ? AND friend_id = ?";
        String sqlQueryInsertRequest = "INSERT INTO user_friends (user_id, friend_id, applied) " +
                "VALUES (?, ?, ?)";
        String sqlQueryUpdateRequest = "UPDATE user_friends SET applied = true " +
                "WHERE user_id = ? AND friend_id = ?";

        try {
            callbackRequest = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQueryGetStatus, boolean.class,
                    friendId, userId));
        } catch (EmptyResultDataAccessException exc) {
            log.debug("Встречный запрос от пользователя {} отсутствует", userId);
        }

        if (callbackRequest) {
            jdbcTemplate.update(sqlQueryUpdateRequest, userId, friendId);
            jdbcTemplate.update(sqlQueryUpdateRequest, friendId, userId);
        } else {
            jdbcTemplate.update(sqlQueryInsertRequest, userId, friendId, false);
        }
    }

    @Override
    public boolean deleteUserFriend(int userId, int friendId) {
        User user = readUser(userId);
        User friend = readUser(friendId);

        String sqlQuery = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";

        return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
    }

    @Override
    public List<User> getUserFriends(int userId) {
        String sqlQuery = "SELECT user_id, email, name, login, birthday " +
                "FROM users " +
                "WHERE user_id IN (SELECT friend_id FROM user_friends WHERE user_id = ?);";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        User user = readUser(userId);
        User friend = readUser(friendId);

        String sqlQuery = "SELECT friend_id FROM user_friends WHERE user_id = ?";

        List<Integer> firstUserFriends = jdbcTemplate.queryForList(sqlQuery, Integer.class, userId);
        List<Integer> secondUserFriends = jdbcTemplate.queryForList(sqlQuery, Integer.class, friendId);

        if (firstUserFriends.size() > 0 && secondUserFriends.size() > 0) {
            return firstUserFriends.stream()
                    .filter(secondUserFriends::contains)
                    .map(this::readUser)
                    .collect(Collectors.toList());
        }
        return new ArrayList<User>();
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {

        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
