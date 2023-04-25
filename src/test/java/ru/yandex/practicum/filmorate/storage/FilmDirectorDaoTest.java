package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDirectorDaoTest {
    private final FilmDirectorDao filmDirectorDao;
    private final FilmDaoImpl filmDao;
    private final DirectorDaoImpl directorDao;

    @Test
    public void testGetFilmDirectors() {
        List<Genre> genres = List.of(Genre.builder().id(5).name("Документальный").build(),
                Genre.builder().id(6).name("Боевик").build());
        List<Director> directors = List.of(Director.builder().id(1).name("Михалков Петр").build(),
                Director.builder().id(2).name("Скарсезе Иван").build());
        Rating rating = Rating.builder().id(5).name("NC-17").build();
        directorDao.addDirector(directors.get(0));
        directorDao.addDirector(directors.get(1));

        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(rating)
                .directors(directors)
                .genres(genres)
                .build();
        Film testFilm = filmDao.createFilm(film);

        Integer filmId = testFilm.getId();
        List<Director> filmDirectors = filmDirectorDao.getFilmDirectors(filmId);
        assertEquals(2, filmDirectors.size());
        assertEquals(directors, filmDirectors);
    }

    @Test
    public void testDeleteFilmDirectors() {
        List<Genre> genres = List.of(Genre.builder().id(5).name("Документальный").build(),
                Genre.builder().id(6).name("Боевик").build());
        List<Director> directors = List.of(Director.builder().id(1).name("Михалков Петр").build(),
                Director.builder().id(2).name("Скарсезе Иван").build());
        Rating rating = Rating.builder().id(5).name("NC-17").build();
        directorDao.addDirector(directors.get(0));
        directorDao.addDirector(directors.get(1));

        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(rating)
                .directors(directors)
                .genres(genres)
                .build();
        Film testFilm = filmDao.createFilm(film);

        Integer filmId = testFilm.getId();
        List<Director> filmDirectors = filmDirectorDao.getFilmDirectors(filmId);
        assertEquals(2, filmDirectors.size());
        assertEquals(directors, filmDirectors);

        film = Film.builder()
                .id(1)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(rating)
                .directors(List.of(directors.get(0)))
                .genres(genres)
                .build();
        filmDao.updateFilm(film);
        filmDirectors = filmDirectorDao.getFilmDirectors(filmId);
        assertEquals(1, filmDirectors.size());
        assertEquals(List.of(directors.get(0)), filmDirectors);
    }

    @Test
    public void testDeleteDirector() {
        List<Genre> genres = List.of(Genre.builder().id(5).name("Документальный").build(),
                Genre.builder().id(6).name("Боевик").build());
        List<Director> directors = List.of(Director.builder().id(1).name("Михалков Петр").build(),
                Director.builder().id(2).name("Скарсезе Иван").build());
        Rating rating = Rating.builder().id(5).name("NC-17").build();
        directorDao.addDirector(directors.get(0));
        directorDao.addDirector(directors.get(1));

        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(rating)
                .directors(directors)
                .genres(genres)
                .build();
        Film testFilm = filmDao.createFilm(film);

        Integer filmId = testFilm.getId();
        List<Director> filmDirectors = filmDirectorDao.getFilmDirectors(filmId);
        assertEquals(2, filmDirectors.size());
        assertEquals(directors, filmDirectors);

        directorDao.deleteDirector(2);
        filmDirectors = filmDirectorDao.getFilmDirectors(filmId);
        assertEquals(1, filmDirectors.size());
        assertEquals(List.of(directors.get(0)), filmDirectors);
    }

    @Test
    public void testAddToFilmDirector() {
        List<Genre> genres = List.of(Genre.builder().id(5).name("Документальный").build(),
                Genre.builder().id(6).name("Боевик").build());
        List<Director> directors = List.of(Director.builder().id(1).name("Михалков Петр").build(),
                Director.builder().id(2).name("Скарсезе Иван").build());
        Rating rating = Rating.builder().id(5).name("NC-17").build();
        directorDao.addDirector(directors.get(0));
        directorDao.addDirector(directors.get(1));

        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(rating)
                .directors(directors)
                .genres(genres)
                .build();
        Film testFilm = filmDao.createFilm(film);

        Integer filmId = testFilm.getId();
        List<Director> filmDirectors = filmDirectorDao.getFilmDirectors(filmId);
        assertEquals(2, filmDirectors.size());
        assertEquals(directors, filmDirectors);

        directorDao.addDirector(Director.builder().id(3).name("Стивенс Спилберг").build());
        filmDirectorDao.addDirectorToFilm(1, 3);
        filmDirectors = filmDirectorDao.getFilmDirectors(filmId);
        assertEquals(3, filmDirectors.size());
    }
}