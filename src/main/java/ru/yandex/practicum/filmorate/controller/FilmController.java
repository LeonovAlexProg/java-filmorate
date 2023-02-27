package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    Map<Integer, Film> films = new HashMap<>();

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) throws FilmExistsException, ValidationException {
        if (Validator.validateFilm(film)) {
            if (!films.containsValue(film) && !films.containsKey(film.getId())) {
                log.debug("Фильм {} уже есть в коллекции", film.getName());
                throw new FilmExistsException("Фильм уже существует");
            } else {
                films.put(film.getId(), film);
                log.debug("Фильм {} добавлен в коллекцию под индексом {}", film.getName(), film.getId());
                return film;
            }
        } else {
            log.debug("Неправильный формат данных фильма");
            throw new ValidationException();
        }
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) throws ValidationException {
        if (Validator.validateFilm(film)) {
            if (films.containsKey(film.getId())) {
                films.replace(film.getId(), film);
                log.debug("Фильм под индексом {} был обновлен", film.getId());
            } else {
                films.put(film.getId(), film);
                log.debug("Фильм {} был добавлен", film.getName());
            }
            return film;
        } else {
            log.debug("Неправильный формат данных фильма");
            throw new ValidationException();
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
}
