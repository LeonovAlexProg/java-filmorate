package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component("filmDaoImpl")
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public void createFilm(Film film) {
        String sqlFilmQuery = "INSERT INTO films (name, mpa_id, description, release_date, duration) " +
                "VALUES (?, ?, ?, ?, ?)";
        String sqlGenresQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlFilmQuery, new String[]{"film_id"});

            ps.setString(1, film.getName());
            ps.setInt(2, film.getMpa().getId());
            ps.setString(3, film.getDescription());
            ps.setDate(4, Date.valueOf(film.getReleaseDate()));
            ps.setInt(5, film.getDuration());

            return ps;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());

        if (film.getGenres() != null) {
            film.getGenres()
                    .forEach(genre -> jdbcTemplate.update(sqlGenresQuery, genre.getId(), genre.getName()));
        } else {
            log.debug("Film genres are null");
        }
    }

    @Override
    public Film readFilm(int id) {
        return null;
    }

    @Override
    public void updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, " +
                "mpa_id = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ? " +
                "WHERE film_id = ?";

        int rowsAffected = jdbcTemplate.update(sqlQuery, film.getName(), film.getMpa().getId(),
                film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());

        if (rowsAffected == 0) {
            log.debug("Film id {} not found", film.getId());
            throw new UserNotFoundException("Film not found", film.getId());
        }
    }

    @Override
    public void deleteFilm(Film film) {

    }


    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT f.film_id, f.name, m.mpa_id, m.mpa_name, " +
                "f.description, f.release_date, f.duration, g.genre_id, g.genre_name " +
                "FROM films f LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id";

        return jdbcTemplate.query(sqlQuery, this::rowMapperForFilm);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT genre_id, genre_name FROM genres";

        return jdbcTemplate.query(sqlQuery, this::rowMapperForGenre);
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::rowMapperForGenre, id);
        } catch (EmptyResultDataAccessException exc) {
            log.debug("Genre id = {} not found", id);
            throw new GenreNotFoundException("Genre not found", id);
        }
    }

    @Override
    public List<Rating> getAllRatings() {
        String sqlQuery = "SELECT mpa_id, mpa_name FROM mpa";

        return jdbcTemplate.query(sqlQuery, this::rowMapperForRating);
    }

    @Override
    public Rating getRatingById(int id) {
        String sqlQuery = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::rowMapperForRating, id);
        } catch (EmptyResultDataAccessException exc) {
            log.debug("Rating id = {} not found", id);
            throw new RatingNotFoundException("Rating not found", id);
        }
    }
    //доделать жанры и рейтинг
    public Film rowMapperForFilm(ResultSet rs, int rowNum) throws SQLException {
        Rating rating = rowMapperForRating(rs, rowNum);

        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(rating)
                .build();

    }

    public Genre rowMapperForGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }

    public Rating rowMapperForRating(ResultSet rs, int rowNum) throws SQLException {
        return Rating.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }
}
