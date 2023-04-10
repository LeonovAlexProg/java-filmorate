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
        filmStorage.createFilm(film);
        return film;
    }

    public Film putFilm(Film film) {
        filmStorage.updateFilm(film);
        return film;
    }
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(int id) {
        return filmStorage.readFilm(id);
    }

    public Film putLikeOnFilm(int filmId, int userId) {
        Film film = filmStorage.readFilm(filmId);
        User user = userStorage.readUser(userId);

        if (!film.getUsersLikes().add(userId)) {
            throw new LikeExistsException(String.format("User like exists, userId = %d, filmId = %d",
                    user.getId(), film.getId()));
        }

        return film;
    }

    public void deleteLikeFromFilm(int filmId, int userId) {
        Film film = filmStorage.readFilm(filmId);
        User user = userStorage.readUser(userId);

        if (!film.getUsersLikes().remove(userId)) {
            throw new LikeNotFoundException(String.format("User like not found, userId = %d, filmId = %d",
                    user.getId(), film.getId()));
        }
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing((Film f) -> f.getUsersLikes().size()).reversed())
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
