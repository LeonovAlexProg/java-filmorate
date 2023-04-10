package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private Integer id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    @Override
    public void createFilm(Film film) {
        if (!films.containsValue(film) && film.getId() == 0) {
            id++;
            film.setId(id);
            films.put(id, film);
        } else {
            throw new FilmExistsException("Film already exists", film.getId(), film.getName());
        }
    }

    @Override
    public Film readFilm(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new FilmNotFoundException("Film is not found", id);
        }
    }

    @Override
    public void updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
        } else {
            throw new FilmNotFoundException("Film is not found", film.getId());
        }
    }

    @Override
    public void deleteFilm(Film film) {
        if (films.remove(film.getId()) == null) {
            throw new FilmNotFoundException("Film is not found", film.getId());
        }
    }

    @Override
    public Film getPopularFilm() {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Genre getGenreById(int id) {
        return null;
    }

    @Override
    public List<Rating> getAllRatings() {
        return null;
    }

    @Override
    public Rating getRatingById(int id) {
        return null;
    }
}
