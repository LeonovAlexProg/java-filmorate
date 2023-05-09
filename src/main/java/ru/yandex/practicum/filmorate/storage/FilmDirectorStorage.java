package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface FilmDirectorStorage {
    List<Integer> getDirectorsIdByFilmId(int filmId);
    List<Integer> getFilmsIdByDirectorId(int directorId);
    void deleteFilmDirectors(int filmId);
    void addDirectorToFilm(Integer filmId, Integer directorId);
    void setFilmDirectors(List<Director> directorsId, int filmId);
    void updateFilmDirectors(List<Director> directorsId, int filmId);
}
