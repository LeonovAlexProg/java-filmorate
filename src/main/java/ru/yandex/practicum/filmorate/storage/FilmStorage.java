package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film readFilm(int id);

    Film updateFilm(Film film);

    void deleteFilmByID(int id);

    List<Film> getAllFilms();

    List<Film> searchFilms(String query, String fields);

    void putLikeOnFilm(int filmId, int userId);

    void deleteLikeFromFilm(int filmId, int userId);

    List<Film> getFilmsRecommendation(int userId);

    List<Film> getFilmsByYearGenres(int count, int genreId, int year);

    List<Film> getCommonFilms(int userId, int friendId);
}
