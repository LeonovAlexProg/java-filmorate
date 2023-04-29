package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmDirector {
    private Integer filmId;
    private Integer directorId;

    public FilmDirector(Integer filmId, Integer directorId) {
        this.filmId = filmId;
        this.directorId = directorId;
    }
}
