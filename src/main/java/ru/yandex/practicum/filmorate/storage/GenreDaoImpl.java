package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT genre_id, genre_name FROM genres";

        return jdbcTemplate.query(sqlQuery, this::rowMapperForGenre);
    }

    @Override
    public Genre getById(int id) {
        String sqlQuery = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::rowMapperForGenre, id);
        } catch (EmptyResultDataAccessException exc) {
            log.debug("Genre id = {} not found", id);
            throw new GenreNotFoundException("Genre not found", id);
        }
    }

    private Genre rowMapperForGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }

}
