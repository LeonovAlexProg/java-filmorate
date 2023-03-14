package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    public void createFilm(Film film);

    public Film readFilm(int id);

    public void updateFilm(Film film);

    public void deleteFilm(Film film);
}
