package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final RatingStorage ratingDao;
    private final GenreStorage genreDao;
    private final FeedStorage feedStorage;
    private final DirectorStorage directorDao;
    private final FilmDirectorStorage filmDirectorDao;

    public Film postFilm(Film film) {
        filmStorage.createFilm(film);
        filmDirectorDao.setFilmDirectors(film.getDirectors(), film.getId());
        return getFilm(film.getId());
    }

    public Film putFilm(Film film) {
        filmStorage.updateFilm(film);
        filmDirectorDao.updateFilmDirectors(film.getDirectors(), film.getId());
        return getFilm(film.getId());
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        for (Film film: films) {
            List<Director> directors = filmDirectorDao.getDirectorsIdByFilmId(film.getId())
                    .stream()
                    .map(directorDao::getDirectorById)
                    .collect(Collectors.toList());
            film.setDirectors(directors);
        }
        return films;
    }

    public Film getFilm(int id) {
        Film film = filmStorage.readFilm(id);
        List<Director> directors = filmDirectorDao.getDirectorsIdByFilmId(film.getId()).stream()
                .map(directorDao::getDirectorById)
                .collect(Collectors.toList());
        film.setDirectors(directors);
        return film;
    }

    public List<Film> getFilmsByDirectorId(int id, String sort) {
        if(!(sort.equals("likes") || sort.equals("year"))){
            throw new IllegalArgumentException("неизвестная сортировка " + sort + ". Варианты: [likes, year]");
        }

        if (directorDao.containsDirector(id)) {
            List<Film> films = filmDirectorDao.getFilmsIdByDirectorId(id).stream()
                    .map(this::getFilm)
                    .collect(Collectors.toList());

            for (Film film: films) {
                List<Director> directors = filmDirectorDao.getDirectorsIdByFilmId(film.getId())
                        .stream()
                        .map(directorDao::getDirectorById)
                        .collect(Collectors.toList());
                film.setDirectors(directors);
            }

            if(sort.equals("likes")) {
                return films.stream()
                        .sorted(Comparator.comparing(Film::getLikes).reversed())
                        .collect(Collectors.toList());
            } else {
                return films.stream()
                        .sorted(Comparator.comparing(Film::getReleaseDate))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    public List<Film> searchFilms(String query, String fields) {
        List<String> allowed = List.of("title", "director");
        List<String> inputFields = Arrays.asList(fields.split(","));

        if(!allowed.containsAll(inputFields)){
            throw new IllegalArgumentException("неизвестное поле для поиска " + fields + ". Варианты: [director, title]");
        }
        List<Film> films = filmStorage.searchFilms(query, fields);
        for (Film film: films) {
            List<Director> directors = filmDirectorDao.getDirectorsIdByFilmId(film.getId())
                    .stream()
                    .map(directorDao::getDirectorById)
                    .collect(Collectors.toList());
            film.setDirectors(directors);
        }
        return films;
    }

    public void putLikeOnFilm(int filmId, int userId) {
        filmStorage.putLikeOnFilm(filmId, userId);
        feedStorage.updateFeed("LIKE", "ADD", userId, filmId, Instant.now());
    }

    public void deleteLikeFromFilm(int filmId, int userId) {
        filmStorage.readFilm(filmId);
        userStorage.readUser(userId);

        filmStorage.deleteLikeFromFilm(filmId, userId);
        feedStorage.updateFeed("LIKE", "REMOVE", userId, filmId, Instant.now());
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films =  filmStorage.getAllFilms().stream()
                .limit(count)
                .collect(Collectors.toList());
        for (Film film: films) {
            List<Director> directors = filmDirectorDao.getDirectorsIdByFilmId(film.getId())
                    .stream()
                    .map(directorDao::getDirectorById)
                    .collect(Collectors.toList());
            film.setDirectors(directors);
        }
        return films;
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAll();
    }

    public Genre getGenreById(int id) {
        return genreDao.getById(id);
    }

    public List<Rating> getAllRatings() {
        return ratingDao.getAll();
    }

    public Rating getRatingById(int id) {
        return ratingDao.getById(id);
    }

    public void deleteFilmByID(int filmId) {
        filmStorage.deleteFilmByID(filmId);
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        List<Film> films =  filmStorage.getCommonFilms(userId, friendId);
        for (Film film: films) {
            List<Director> directors = filmDirectorDao.getDirectorsIdByFilmId(film.getId())
                    .stream()
                    .map(directorDao::getDirectorById)
                    .collect(Collectors.toList());
            film.setDirectors(directors);
        }
        return films;
    }

    public List<Film> getPopularFilmsByYearGenres(int limit, int genreId, int year) {
        List<Film> films;
        if (genreId == 0 && year == 0 && limit == 0) {
            films = filmStorage.getAllFilms();
        } else if (genreId == 0 && year == 0) {
            films = getPopularFilms(limit);
        } else {
            films = filmStorage.getFilmsByYearGenres(limit, genreId, year);
        }
        for (Film film: films) {
            List<Director> directors = filmDirectorDao.getDirectorsIdByFilmId(film.getId())
                    .stream()
                    .map(directorDao::getDirectorById)
                    .collect(Collectors.toList());
            film.setDirectors(directors);
        }
        return films;
    }
}
