package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDaoImplTest {
    private final FilmDaoImpl filmDao;
    private final UserDaoImpl userDao;

    Rating rating;
    List<Genre> genres;
    Film testFilm;
    Film testFilmTwo;
    User testUser;
    User testUserTwo;

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
        testUser = User.builder().name("tom").email("email@test.ru").login("anderson")
                .birthday(LocalDate.of(2000, 12, 12)).build();
        testUserTwo = User.builder().name("john").email("email2@test.ru").login("bjornson")
                .birthday(LocalDate.of(2000, 12, 12)).build();
    }

    @Test
    void createFilm() {
        Film expectedFilm;
        Film actualFilm;

        actualFilm = filmDao.createFilm(testFilm);
        testFilm.setId(1);
        expectedFilm = testFilm;

        assertEquals(expectedFilm, actualFilm);
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

        assertEquals(expectedFilm, actualFilm);
    }

    @Test
    void updateFilm() {
        Film expectedFilm;
        Film actualFilm;

        filmDao.createFilm(testFilm);
        testFilmTwo.setId(1);
        expectedFilm = filmDao.updateFilm(testFilmTwo);
        actualFilm = testFilmTwo;

        assertEquals(expectedFilm, actualFilm);
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

        assertArrayEquals(expectedFilms.toArray(), actualFilms.toArray());
    }

    @Test
    void putLikeOnFilm() {
        Film expectedFilm;
        Film actualFilm;

        userDao.createUser(testUser);
        userDao.createUser(testUserTwo);
        filmDao.createFilm(testFilm);
        filmDao.createFilm(testFilmTwo);
        testFilmTwo.setLikes(2);
        expectedFilm = testFilmTwo;
        filmDao.putLikeOnFilm(1, 1);
        filmDao.putLikeOnFilm(2, 1);
        filmDao.putLikeOnFilm(2, 2);
        actualFilm = filmDao.getAllFilms().stream().limit(1).collect(Collectors.toList()).get(0);

        assertEquals(expectedFilm, actualFilm);
    }

    @Test
    void deleteLikeFromFilm() {
        userDao.createUser(testUser);
        userDao.createUser(testUserTwo);
        filmDao.createFilm(testFilmTwo);
        filmDao.putLikeOnFilm(1, 1);
        filmDao.putLikeOnFilm(1, 2);
        assertEquals(2, filmDao.readFilm(1).getLikes());
        filmDao.deleteLikeFromFilm(1, 1);

        assertEquals(1, filmDao.readFilm(1).getLikes());
    }

    @Test
    void getCommonFilms() {
        Film testFilmThree = Film.builder().name("test3").mpa(rating).description("test description three")
                .releaseDate(LocalDate.of(1967, 3, 25)).duration(100).genres(genres)
                .build();
        User testUserThree = User.builder().name("Nick").email("mail@mail.ru").login("Nick Name")
                .birthday(LocalDate.of(1946, 8, 20)).build();
        userDao.createUser(testUser);
        userDao.createUser(testUserTwo);
        userDao.createUser(testUserThree);
        filmDao.createFilm(testFilm);
        filmDao.createFilm(testFilmTwo);
        filmDao.createFilm(testFilmThree);
        filmDao.putLikeOnFilm(1, 1);
        filmDao.putLikeOnFilm(2, 1);
        filmDao.putLikeOnFilm(2, 2);
        filmDao.putLikeOnFilm(3, 1);
        filmDao.putLikeOnFilm(3, 2);
        filmDao.putLikeOnFilm(3, 3);
        List<Film> commonFilms = filmDao.getCommonFilms(1, 2);

        assertEquals(2, commonFilms.size());
        assertEquals(filmDao.readFilm(3), commonFilms.get(0));
        assertEquals(filmDao.readFilm(2), commonFilms.get(1));

    }
}