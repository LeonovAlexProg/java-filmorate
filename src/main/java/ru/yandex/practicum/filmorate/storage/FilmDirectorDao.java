package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class FilmDirectorDao implements FilmDirectorStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Integer> getDirectorsIdByFilmId(int filmId) {
        String sqlQuery = "select director_id from film_directors where film_id = ? order by director_id";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
    }

    @Override
    public List<Integer> getFilmsIdByDirectorId(int directorId) {
        String sqlQuery = "select film_id from film_directors where director_id = ? order by film_id";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, directorId);
    }

    @Override
    public void deleteFilmDirectors(int filmId) {
        String sqlQuery = "delete from film_directors where film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public void addDirectorToFilm(Integer filmId, Integer directorId) {
        String sqlQuery = "merge into film_directors KEY (film_id, director_id) values(?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, directorId);
    }

    @Override
    public void setFilmDirectors(List<Director> directors, int filmId) {
        if (directors == null || directors.size() == 0) return;
        jdbcTemplate.batchUpdate(
                "insert into film_directors (film_id, director_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, directors.get(i).getId());
                    }

                    public int getBatchSize() {
                        return directors.size();
                    }
                });
    }

    @Override
    public void updateFilmDirectors(List<Director> directors, int filmId) {
        deleteFilmDirectors(filmId);
        setFilmDirectors(directors, filmId);
    }
}
