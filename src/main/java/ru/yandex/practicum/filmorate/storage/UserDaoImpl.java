package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.time.LocalDate;
import java.util.List;

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
        String sqlQuery = "SELECT u.user_id, u.email, u.name, u.login, u.birthday, " +
                "f.friend_id, f.applied " +
                "FROM users u LEFT JOIN user_friends f ON u.user_id = f.user_id WHERE u.user_id = ?";

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
        String sqlQuery = "SELECT u.user_id, u.email, u.name, u.login, u.birthday, " +
                "f.friend_id, f.applied " +
                "FROM users u LEFT JOIN user_friends f ON u.user_id = f.user_id";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    public Integer addUserFriend(int userId,int  friendId) {
        String sqlQuery = "INSERT INTO user_friends "
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {

        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .friend(resultSet.getInt("friend_id"), resultSet.getBoolean("applied"))
                .build();
    }
}
