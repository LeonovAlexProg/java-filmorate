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
    Integer id = 0;
    Map<Integer, Film> films = new HashMap<>();

//    @PostMapping("/films")
//    public Film postFilm(@RequestBody Film film) throws FilmExistsException, ValidationException {
//
//    }
//
//    @PutMapping("/films")
//    public Film putFilm(@RequestBody Film film) throws ValidationException {
//
//    }

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
