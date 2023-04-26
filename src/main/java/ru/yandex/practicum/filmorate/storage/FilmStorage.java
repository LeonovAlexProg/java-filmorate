package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film readFilm(int id);

    Film updateFilm(Film film);

    void deleteFilm(Film film);

    List<Film> getAllFilms();

    List<Film> getFilmsByDirectorId(int directorId);

    void putLikeOnFilm(int filmId, int userId);

    void deleteLikeFromFilm(int filmId, int userId);
}
