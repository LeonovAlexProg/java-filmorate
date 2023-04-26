package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final RatingDaoImpl ratingDao;
    private final GenreDaoImpl genreDao;

    public FilmService(@Qualifier("filmDaoImpl") FilmStorage filmStorage,
                       @Qualifier("userDaoImpl") UserStorage userStorage,
                       RatingDaoImpl ratingDao,
                       GenreDaoImpl genreDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.ratingDao = ratingDao;
        this.genreDao = genreDao;
    }

    public Film postFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film putFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(int id) {
        return filmStorage.readFilm(id);
    }

    public void putLikeOnFilm(int filmId, int userId) {
        filmStorage.putLikeOnFilm(filmId, userId);
    }

    public void deleteLikeFromFilm(int filmId, int userId) {
        filmStorage.readFilm(filmId);
        userStorage.readUser(userId);

        filmStorage.deleteLikeFromFilm(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .limit(count)
                .collect(Collectors.toList());
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

    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }
}
