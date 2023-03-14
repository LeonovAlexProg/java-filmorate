package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import java.util.List;

@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Film postFilm(Film film) {
        inMemoryFilmStorage.createFilm(film);
        return film;
    }

    public Film putFilm(Film film) {
        inMemoryFilmStorage.updateFilm(film);
        return film;
    }
    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film getFilm(int id) {
        return inMemoryFilmStorage.readFilm(id);
    }
}
