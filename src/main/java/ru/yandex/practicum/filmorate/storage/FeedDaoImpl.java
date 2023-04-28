package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Feed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedDaoImpl implements FeedStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Feed> getFeedList(int id) {
        String sqlQuery = "SELECT event_id, event_type, operation, publication_time, user_id, friend_id, " +
                "film_id, review_id FROM public.events WHERE user_id = ?";
        //добавить ревью_id
        String sqlValid = "SELECT COUNT(*) FROM public.users WHERE user_id = ?";
        Integer row = this.jdbcTemplate.queryForObject(sqlValid, Integer.class, id);
        if (row != 1) {
            log.debug("User id {} not found", id);
            throw new UserNotFoundException("Пользователь с таким id не существует", id);
        }
        List<Feed> feed = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rowMapperForFeed(rs), id);
        return feed.stream()
                .sorted(Comparator.comparing(Feed::getEventId))
                .collect(Collectors.toList());
    }

    public Feed rowMapperForFeed(ResultSet rs) throws SQLException {
        String eventType = rs.getString("event_type").toUpperCase();
        switch (eventType) {
            case "FRIEND":
                return Feed.builder()
                        .eventType(eventType)
                        .operation(rs.getString("operation").toUpperCase())
                        .userId(rs.getInt("user_id"))
                        .timestamp(rs.getTimestamp("publication_time").toInstant().toEpochMilli())
                        .eventId(rs.getInt("event_id"))
                        .entityId(rs.getInt("friend_id"))
                        .build();
            case "LIKE":
                return Feed.builder()
                        .eventType(eventType)
                        .operation(rs.getString("operation").toUpperCase())
                        .userId(rs.getInt("user_id"))
                        .timestamp(rs.getTimestamp("publication_time").toInstant().toEpochMilli())
                        .eventId(rs.getInt("event_id"))
                        .entityId(rs.getInt("film_id"))
                        .build();
            case "REVIEW":
                return Feed.builder()
                        .eventType(eventType)
                        .operation(rs.getString("operation").toUpperCase())
                        .userId(rs.getInt("user_id"))
                        .timestamp(rs.getTimestamp("publication_time").toInstant().toEpochMilli())
                        .eventId(rs.getInt("event_id"))
                        .entityId(rs.getInt("review_id"))
                        .build();
        }
        return null;
    }

    @Override
    public void updateFeed(String eventType, String operation, int userId, int entityId, Instant instant) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery;
        switch (eventType) {
            case "FRIEND":
                sqlQuery = "INSERT INTO public.events (event_type, operation, publication_time, user_id, friend_id) " +
                        "VALUES (?, ?, ?, ?, ?)";
                break;
            case "LIKE":
                sqlQuery = "INSERT INTO public.events (event_type, operation, publication_time, user_id, film_id) " +
                        "VALUES (?, ?, ?, ?, ?)";
                break;
            case "REVIEW":
                sqlQuery = "INSERT INTO public.events (event_type, operation, publication_time, user_id, review_id) " +
                        "VALUES (?, ?, ?, ?, ?)";
                break;
            default:
                throw new IllegalArgumentException();
        }
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlQuery, new String[]{"event_id"});

            ps.setString(1, eventType);
            ps.setString(2, operation);
            ps.setTimestamp(3, Timestamp.from(instant));
            ps.setInt(4, userId);
            ps.setInt(5, entityId);

            return ps;
        }, keyHolder);
    }
}




