package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDirectorDao filmDirectorDao;
    private final DirectorDaoImpl directorDao;

    @Override
    public Film createFilm(Film film) {
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
                    .forEach(genre -> jdbcTemplate.update(sqlGenresQuery, film.getId(), genre.getId()));
        } else {
            log.debug("Film genres are null");
        }

        if (film.getDirectors() != null) {
            film.getDirectors()
                    .forEach(director -> filmDirectorDao.addDirectorToFilm(film.getId(), director.getId()));
        } else {
            log.debug("Film directors are null");
        }

        return film;
    }

    @Override
    public Film readFilm(int filmId) {
        String sqlQuery = "select " +
                "f.film_id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.mpa_name, " +
                "count(l.user_id) as likes " +
                "from films AS f left join mpa as m on f.mpa_id = m.mpa_id " +
                "left join film_likes As l on f.film_id = l.film_id " +
                "where f.film_id = ? " +
                "group by f.film_id ";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::rowMapperForFilm, filmId);
        } catch (EmptyResultDataAccessException exc) {
            log.debug("Film id - {} not found", filmId);
            throw new FilmNotFoundException("Film not found", filmId);
        }
    }

    @Override
    public Film updateFilm(Film film) {
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
            throw new FilmNotFoundException("Film not found", film.getId());
        }

        if (film.getGenres() != null && film.getGenres().size() > 0) {
            updateGenres(film);
        } else {
            deleteGenresByFilmId(film.getId());
            log.debug("Film genres are null");
        }
        filmDirectorDao.deleteFilmDirectors(film.getId());
        if (film.getDirectors() != null) {
            film.getDirectors()
                    .forEach(director -> filmDirectorDao.addDirectorToFilm(film.getId(), director.getId()));
        } else {
            log.debug("Film directors are null");
        }
        return readFilm(film.getId());
    }

    @Override
    public void deleteFilm(Film film) {

    }


    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT f.film_id, f.name, m.mpa_id, m.mpa_name, " +
                "f.description, f.release_date, f.duration, " +
                "g.genre_id, g.genre_name, COUNT(fl.user_id) likes " +
                "FROM films f LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_likes fl ON f.film_id=fl.film_id " +
                "LEFT JOIN film_genres fg ON f.film_id=fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id=g.genre_id " +
                "GROUP BY f.film_id, g.genre_id " +
                "ORDER BY COUNT(fl.user_id) DESC";

        final Map<Integer, Film> films = new HashMap<>();

        jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            int filmId = rs.getInt("film_id");
            Film film = films.get(filmId);
            Genre genre = rowMapperForGenre(rs, rowNum);

            if (film == null) {
                film = Film.builder()
                        .id(rs.getInt("film_id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .duration(rs.getInt("duration"))
                        .mpa(rowMapperForRating(rs, rowNum))
                        .genres(new ArrayList<>())
                        .directors(filmDirectorDao.getFilmDirectors(rs.getInt("film_id")))
                        .likes(rs.getInt("likes"))
                        .build();

                films.put(filmId, film);
            }

            if (genre.getId() != 0 && genre.getName() != null)
                film.getGenres().add(genre);

            return film;
        });

        return films.values().stream()
                .sorted(Comparator.comparing(Film::getLikes).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> getFilmsRecommendation(int userId) {
        String sqlQuery =
                "SELECT film_id FROM films WHERE film_id NOT IN (" +
                        "SELECT film_id FROM film_likes WHERE user_id = ?" +
                        ") AND film_id IN (" +
                        "SELECT film_id FROM film_likes WHERE user_id = (" +
                        "SELECT users.user_id FROM users " +
                        "LEFT JOIN film_likes ON users.user_id = film_likes.user_id WHERE film_likes.film_id IN (" +
                        "SELECT film_likes.film_id FROM users " +
                        "LEFT JOIN film_likes ON users.user_id = film_likes.user_id WHERE users.user_id = ?" +
                        ") AND users.user_id != ? " +
                        "GROUP BY users.user_id ORDER BY COUNT(film_id) DESC LIMIT 1" +
                        "))";

        //TODO разобраться с передачей одного параметра в разные места запроса
        List<Integer> films = jdbcTemplate.queryForList(sqlQuery, Integer.class, userId, userId, userId);

        return films.stream()
                .map(this::readFilm)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> getFilmsByDirectorId(int directorId, String sort) {
        if (directorDao.containsDirector(directorId)) {
            String sqlQuery = "select f.film_id, " +
                    "f.name, " +
                    "f.description, " +
                    "f.release_date, " +
                    "f.duration, " +
                    "f.mpa_id, " +
                    "m.mpa_name, " +
                    "count(l.user_id) as likes " +
                    "from films AS f " +
                    "inner join film_directors as fd on f.film_id = fd.film_id " +
                    "left join mpa as m on f.mpa_id = m.mpa_id " +
                    "left join film_likes As l on f.film_id = l.film_id " +
                    "where fd.director_id = ? " +
                    "group by f.film_id " +
                    "order by sum(l.film_id) desc, f.name";
            List<Film> films = jdbcTemplate.query(sqlQuery, this::rowMapperForFilm, directorId);
            if (sort.equals("likes")) {
                return films;
            } else {
                return films.stream()
                        .sorted(Comparator.comparing(Film::getReleaseDate))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void putLikeOnFilm(int filmId, int userId) {
        String sqlQuery = "INSERT INTO film_likes (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {
        String sqlQuery = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    private List<Genre> getGenresByFilmId(int filmId) {
        String sqlQuery = "SELECT g.genre_id, g.genre_name FROM genres g " +
                "JOIN film_genres fg ON g.genre_id=fg.genre_id " +
                "WHERE fg.film_id = ?";

        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::rowMapperForGenre, filmId);

        return Objects.requireNonNullElseGet(genres, ArrayList::new);
    }

    private void deleteGenresByFilmId(int filmId) {
        String sqlQuery = "DELETE FROM film_genres WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, filmId);
    }

    private void updateGenres(Film film) {
        int filmId = film.getId();
        List<Genre> genres = film.getGenres().stream()
                .distinct().collect(Collectors.toList());
        String sqlDeleteQuery = "DELETE FROM film_genres WHERE film_id = ?";
        String sqlInsertQuery = "INSERT INTO film_genres (film_id, genre_id) " +
                "VALUES (?, ?)";

        jdbcTemplate.update(sqlDeleteQuery, filmId);

        jdbcTemplate.batchUpdate(
                sqlInsertQuery,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genres.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        String sqlQuery = "SELECT film_id FROM film_Likes WHERE user_id=? AND film_id IN (SELECT film_id FROM film_Likes WHERE user_id =?);";
        List<Integer> filmsId = jdbcTemplate.queryForList(sqlQuery, Integer.class, userId, friendId);

        return filmsId.stream()
                .map(x -> readFilm(x))
                .sorted(Comparator.comparing(Film::getLikes).reversed())
                .collect(Collectors.toList());
    }

    private Film rowMapperForFilm(ResultSet rs, int rowNum) throws SQLException {

        Rating rating = rowMapperForRating(rs, rowNum);

        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(rating)
                .genres(getGenresByFilmId(rs.getInt("film_id")))
                .likes(rs.getInt("likes"))
                .directors(filmDirectorDao.getFilmDirectors(rs.getInt("film_id")))
                .build();
    }

    private Genre rowMapperForGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }

    private Rating rowMapperForRating(ResultSet rs, int rowNum) throws SQLException {
        return Rating.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }
}
