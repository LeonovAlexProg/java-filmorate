package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDaoImplTest {
    private final FilmDaoImpl filmDao;

    Rating rating;
    List<Genre> genres;
    Film testFilm;
    Film testFilmTwo;

    @BeforeEach
    public void initObjects() {
        rating = Rating.builder().id(5).name("NC-17").build();
        genres = List.of(Genre.builder().id(5).name("Документальный").build(),
                Genre.builder().id(6).name("Боевик").build());
        testFilm = Film.builder().name("test").mpa(rating).description("test description")
                .releaseDate(LocalDate.of(2000, 12, 12)).duration(100).genres(genres)
                .build();
        testFilmTwo = Film.builder().name("test2").mpa(rating).description("test description two")
                .releaseDate(LocalDate.of(1950, 12, 12)).duration(200).genres(genres)
                .build();
    }

    @Test
    void createFilm() {
        Film expectedFilm;
        Film actualFilm;

        actualFilm = filmDao.createFilm(testFilm);
        testFilm.setId(1);
        expectedFilm = testFilm;

        assertEquals(actualFilm, expectedFilm);
    }

    @Test
    void readFilm() {
        Film expectedFilm;
        Film actualFilm;

        filmDao.createFilm(testFilm);
        filmDao.createFilm(testFilm);
        testFilm.setId(2);
        expectedFilm = testFilm;
        actualFilm = filmDao.readFilm(2);

        assertEquals(actualFilm, expectedFilm);
    }

    @Test
    void updateFilm() {
        Film expectedFilm;
        Film actualFilm;

        filmDao.createFilm(testFilm);
        testFilmTwo.setId(1);
        expectedFilm = filmDao.updateFilm(testFilmTwo);
        actualFilm = testFilmTwo;

        assertEquals(actualFilm, expectedFilm);
    }

    @Test
    void getAllFilms() {
        List<Film> expectedFilms;
        List<Film> actualFilms;

        testFilm.setId(1);
        testFilmTwo.setId(2);
        expectedFilms = List.of(testFilm,
                testFilmTwo);
        filmDao.createFilm(testFilm);
        filmDao.createFilm(testFilmTwo);
        actualFilms = filmDao.getAllFilms();

        assertArrayEquals(actualFilms.toArray(), expectedFilms.toArray());
    }

    @Test
    void getAllGenres() {
        List<Genre> expectedList = List.of(Genre.builder().id(1).name("Комедия").build(),
                Genre.builder().id(2).name("Драма").build(),
                Genre.builder().id(3).name("Мультфильм").build(),
                Genre.builder().id(4).name("Триллер").build(),
                Genre.builder().id(5).name("Документальный").build(),
                Genre.builder().id(6).name("Боевик").build());
        List<Genre> actualList;

        actualList = filmDao.getAllGenres();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    void getGenreById() {
        Genre expected = Genre.builder().id(3).name("Мультфильм").build();
        Genre actual;

        actual = filmDao.getGenreById(3);

        assertEquals(actual, expected);
    }

    @Test
    void getAllRatings() {
        List<Rating> expectedList = List.of(Rating.builder().id(1).name("G").build(),
                Rating.builder().id(2).name("PG").build(),
                Rating.builder().id(3).name("PG-13").build(),
                Rating.builder().id(4).name("R").build(),
                Rating.builder().id(5).name("NC-17").build()
        );
        List<Rating> actualList;

        actualList = filmDao.getAllRatings();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    void getRatingById() {
    }

    @Test
    void putLikeOnFilm() {
    }

    @Test
    void deleteLikeFromFilm() {
    }
}