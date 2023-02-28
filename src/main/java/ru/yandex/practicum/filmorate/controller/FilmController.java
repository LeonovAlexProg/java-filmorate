package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    Integer id = 0;
    Map<Integer, Film> films = new HashMap<>();

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) throws FilmExistsException, ValidationException {
        Validator.validateFilm(film);
        if (!films.containsValue(film) && film.getId() == 0) {
            id++;
            film.setId(id);
            films.put(id, film);
            return film;
        } else {
            log.debug("Фильм уже существует");
            return findFilm(film);
        }
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) throws ValidationException, FilmNotFoundException {
        Validator.validateFilm(film);
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            return film;
        } else {
            log.debug("Фильм не был найден");
            throw new FilmNotFoundException("Фильм не был найден");
        }
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() throws FilmNotFoundException {
        if (!films.isEmpty()) {
            return new ArrayList<>(films.values());
        } else {
            log.debug("Список фильмов пуст");
            throw new FilmNotFoundException("Список фильмов пуст");
        }
    }

    private Film findFilm(Film film) {
        for (Film currentUser : films.values()) {
            if (currentUser.equals(film))
                film = currentUser;
        }
        return film;
    }
}
