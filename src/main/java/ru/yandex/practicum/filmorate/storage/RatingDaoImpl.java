package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class RatingDaoImpl implements RatingStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Rating> getAll() {
        String sqlQuery = "SELECT mpa_id, mpa_name FROM mpa";

        return jdbcTemplate.query(sqlQuery, this::rowMapperForRating);
    }

    @Override
    public Rating getById(int id) {
        String sqlQuery = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::rowMapperForRating, id);
        } catch (EmptyResultDataAccessException exc) {
            log.debug("Rating id = {} not found", id);
            throw new RatingNotFoundException("Rating not found", id);
        }
    }

    private Rating rowMapperForRating(ResultSet rs, int rowNum) throws SQLException {
        return Rating.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }
}
