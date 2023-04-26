package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FilmDirector {
    private Integer filmId;
    private Integer directorId;

    public FilmDirector(Integer filmId, Integer directorId) {
        this.filmId = filmId;
        this.directorId = directorId;
    }
}
