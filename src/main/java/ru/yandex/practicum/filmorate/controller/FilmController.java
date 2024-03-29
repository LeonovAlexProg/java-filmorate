package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Validator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) {
        Validator.validateFilm(film);
        return filmService.postFilm(film);
    }

    @PutMapping("/films")
    public Film putFilm(@Valid @RequestBody Film film) {
        Validator.validateFilm(film);
        return filmService.putFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/films/director/{directorId}")
    public List<Film> getFilmsByDirectorId(
            @PathVariable int directorId,
            @RequestParam(value = "sortBy", defaultValue = "likes") String sort) {
        return filmService.getFilmsByDirectorId(directorId, sort);
    }

    @GetMapping("/films/search")
    public List<Film> searchFilms(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "by", defaultValue = "director,title") String fields) {
        return filmService.searchFilms(query, fields);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void putLikeOnFilm(@PathVariable(value = "id") int filmId, @PathVariable int userId) {
        filmService.putLikeOnFilm(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable(value = "id") int filmId, @PathVariable int userId) {
        filmService.deleteLikeFromFilm(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}")
    public void deleteFilmByID(@PathVariable int filmId) {
        filmService.deleteFilmByID(filmId);
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return filmService.getGenreById(id);
    }

    @GetMapping("/mpa")
    public List<Rating> getAllRatings() {
        return filmService.getAllRatings();
    }

    @GetMapping("/mpa/{id}")
    public Rating getRatingById(@PathVariable int id) {
        return filmService.getRatingById(id);
    }

    @GetMapping("films/common")
    public List<Film> getCommonFilms(@RequestParam int userId,@RequestParam int friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilmsByYearGenres(@RequestParam(defaultValue = "0") int count,
                                                  @RequestParam(defaultValue = "0") int genreId,
                                                  @RequestParam(defaultValue = "0") int year) {
        return filmService.getPopularFilmsByYearGenres(count, genreId, year);
    }
}
