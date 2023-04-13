package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikeExistsException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmDaoImpl") FilmStorage filmStorage,
                       @Qualifier("userDaoImpl") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        return filmStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return filmStorage.getGenreById(id);
    }

    public List<Rating> getAllRatings() {
        return filmStorage.getAllRatings();
    }

    public Rating getRatingById(int id) {
        return filmStorage.getRatingById(id);
    }
}
