package ru.yandex.practicum.filmorate.controller;


import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private Integer id = 0;
    private Map<Integer, Film> films = new HashMap<>();

    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) {
        Validator.validateFilm(film);
        if (!films.containsValue(film) && film.getId() == 0) {
            id++;
            film.setId(id);
            films.put(id, film);
            return film;
        } else {
            Film existingFilm = findFilm(film);
            log.debug("Фильм {} уже существует под индексом {}", existingFilm.getName(), existingFilm.getId());
            return existingFilm;
        }
    }

    @PutMapping("/films")
    public Film putFilm(@Valid @RequestBody Film film) {
        Validator.validateFilm(film);
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            return film;
        } else {
            log.debug("Фильм {} не был найден", film.getName());
            throw new FilmNotFoundException("Фильм не был найден");
        }
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        if (!films.isEmpty()) {
            return new ArrayList<>(films.values());
        } else {
            log.debug("Список фильмов пуст");
            throw new FilmNotFoundException("Список фильмов пуст");
        }
    }

    private Film findFilm(Film film) {
        for (Film currentFilm : films.values()) {
            if (currentFilm.equals(film))
                film = currentFilm;
        }
        return film;
    }
}
