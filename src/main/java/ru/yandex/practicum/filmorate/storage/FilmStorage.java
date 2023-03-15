package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void createFilm(Film film);

    Film readFilm(int id);

    void updateFilm(Film film);

    void deleteFilm(Film film);

    List<Film> getAllFilms();
}
