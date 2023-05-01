package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReviewDaoImplTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final ReviewStorage reviewStorage;

    Film testFilmOne;
    Film testFilmTwo;

    User testUserOne;
    User testUserTwo;

    @BeforeEach
    public void init() {
        Rating rating = Rating.builder().id(5).name("NC-17").build();
        List<Genre> genres = List.of(Genre.builder().id(5).name("Документальный").build(),
                Genre.builder().id(6).name("Боевик").build());
        testFilmOne = Film.builder().name("test").mpa(rating).description("test description")
                .releaseDate(LocalDate.of(2000, 12, 12)).duration(100).genres(genres)
                .directors(new ArrayList<>())
                .build();
        testFilmTwo = Film.builder().name("test2").mpa(rating).description("test description two")
                .releaseDate(LocalDate.of(1950, 12, 12)).duration(200).genres(genres)
                .directors(new ArrayList<>())
                .build();
        testUserOne = User.builder().name("tom").email("email@test.ru").login("anderson")
                .birthday(LocalDate.of(2000, 12, 12)).build();
        testUserTwo = User.builder().name("john").email("email2@test.ru").login("bjornson")
                .birthday(LocalDate.of(2000, 12, 12)).build();

        filmStorage.createFilm(testFilmOne);
        filmStorage.createFilm(testFilmTwo);
        userStorage.createUser(testUserOne);
        userStorage.createUser(testUserTwo);
    }


    @Test
    void postReview() {
        Review expectedReview;
        Review actualReview;

        expectedReview = Review.builder().reviewId(1).content("some content")
                .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(0).build();
        reviewStorage.postReview(expectedReview);
        actualReview = reviewStorage.readReview(1);

        assertEquals(expectedReview, actualReview);

        expectedReview = Review.builder().reviewId(2).content("some content")
                .isPositive(Boolean.TRUE).userId(2).filmId(2).useful(0).build();
        reviewStorage.postReview(expectedReview);
        actualReview = reviewStorage.readReview(2);

        assertEquals(expectedReview, actualReview);
    }

    @Test
    void readReview() {
    }

    @Test
    void putReview() {
    }

    @Test
    void deleteReview() {
    }

    @Test
    void readAllReviews() {
    }

    @Test
    void readAllReviewsByFilmId() {
    }

    @Test
    void putLike() {
    }

    @Test
    void putDislike() {
    }

    @Test
    void deleteLike() {
    }

    @Test
    void deleteDislike() {
    }
}