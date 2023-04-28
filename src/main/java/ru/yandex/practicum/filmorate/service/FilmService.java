package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final RatingStorage ratingDao;
    private final GenreStorage genreDao;
    private final FeedStorage feedStorage;

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

    public List<Film> getFilmsByDirectorId(int id, String sort) {
        if(!(sort.equals("likes") || sort.equals("year"))){
            throw new IllegalArgumentException("неизвестная сортировка " + sort + ". Варианты: [likes, year]");
        }
        return filmStorage.getFilmsByDirectorId(id, sort);
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
