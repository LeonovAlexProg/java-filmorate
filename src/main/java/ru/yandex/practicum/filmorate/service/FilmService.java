package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikeExistsException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public FilmService(InMemoryUserStorage inMemoryUserStorage, InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }


    public Film postFilm(Film film) {
        inMemoryFilmStorage.createFilm(film);
        return film;
    }

    public Film putFilm(Film film) {
        inMemoryFilmStorage.updateFilm(film);
        return film;
    }
    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film getFilm(int id) {
        return inMemoryFilmStorage.readFilm(id);
    }

    public Film putLikeOnFilm(int filmId, int userId) {
        Film film = inMemoryFilmStorage.readFilm(filmId);
        User user = inMemoryUserStorage.readUser(userId);

        if (!film.getUsersLikes().add(userId)) {
            throw new LikeExistsException(String.format("User like exists, userId = %d, filmId = %d",
                    user.getId(), film.getId()));
        }

        return film;
    }

    public void deleteLikeFromFilm(int filmId, int userId) {
        Film film = inMemoryFilmStorage.readFilm(filmId);
        User user = inMemoryUserStorage.readUser(userId);

        if (!film.getUsersLikes().remove(userId)) {
            throw new LikeNotFoundException(String.format("User like not found, userId = %d, filmId = %d",
                    user.getId(), film.getId()));
        }
    }

    public List<Film> getPopularFilms(int count) {
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing((Film f) -> f.getUsersLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
