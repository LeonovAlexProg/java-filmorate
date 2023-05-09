package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    void putReview() {
        Review expectedReview;
        Review actualReview;

        Review postReview = Review.builder().reviewId(1).content("some content")
                .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(0).build();
        reviewStorage.postReview(postReview);

        expectedReview = Review.builder().reviewId(1).content("updated content")
                .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(0).build();
        reviewStorage.putReview(expectedReview);
        actualReview = reviewStorage.readReview(1);

        assertEquals(expectedReview, actualReview);
    }

    @Test
    void deleteReview() {
        Review expectedReview;
        Review actualReview;

        Review testReviewOne = Review.builder().reviewId(1).content("some content")
                .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(0).build();
        Review testReviewTwo = Review.builder().reviewId(2).content("some content")
                .isPositive(Boolean.TRUE).userId(2).filmId(2).useful(0).build();

        reviewStorage.postReview(testReviewOne);
        reviewStorage.postReview(testReviewTwo);
        reviewStorage.deleteReview(2);

        expectedReview = testReviewOne;
        actualReview = reviewStorage.readReview(1);

        assertEquals(expectedReview, actualReview);
        assertThrows(ReviewNotFoundException.class, () -> reviewStorage.readReview(2));
    }

    @Test
    void readAllReviews() {
        List<Review> expectedList;
        List<Review> actualList;

        expectedList = List.of(
                Review.builder().reviewId(1).content("some content one")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(0).build(),
                Review.builder().reviewId(2).content("some content two")
                        .isPositive(Boolean.FALSE).userId(2).filmId(2).useful(0).build(),
                Review.builder().reviewId(3).content("some content three")
                        .isPositive(Boolean.TRUE).userId(1).filmId(2).useful(0).build(),
                Review.builder().reviewId(4).content("some content four")
                        .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(0).build()
        );

        expectedList.forEach(reviewStorage::postReview);
        actualList = reviewStorage.readAllReviews(4);

        assertArrayEquals(expectedList.toArray(), actualList.toArray());

        expectedList = List.of(
                Review.builder().reviewId(4).content("some content four")
                .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(0).build(),
                Review.builder().reviewId(3).content("some content three")
                        .isPositive(Boolean.TRUE).userId(1).filmId(2).useful(0).build()
                );
        actualList = reviewStorage.readAllReviews(2);

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    void readAllReviewsByFilmId() {
        List<Review> expectedList;
        List<Review> actualList;

        List<Review> testReviews = List.of(
                Review.builder().reviewId(1).content("some content one")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(0).build(),
                Review.builder().reviewId(2).content("some content two")
                        .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(0).build(),
                Review.builder().reviewId(3).content("some content three")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(0).build(),
                Review.builder().reviewId(4).content("some content four")
                        .isPositive(Boolean.FALSE).userId(2).filmId(2).useful(0).build()
        );
        testReviews.forEach(reviewStorage::postReview);

        expectedList = testReviews.stream().limit(3).collect(Collectors.toList());
        actualList = reviewStorage.readAllReviewsByFilmId(1, 3);

        assertArrayEquals(expectedList.toArray(), actualList.toArray());

        expectedList = testReviews.stream().skip(3).collect(Collectors.toList());
        actualList = reviewStorage.readAllReviewsByFilmId(2, 1);

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    void putLike() {
        List<Review> expectedList;
        List<Review> actualList;

        List<Review> testReviews = List.of(
                Review.builder().reviewId(1).content("some content one")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(0).build(),
                Review.builder().reviewId(2).content("some content two")
                        .isPositive(Boolean.FALSE).userId(2).filmId(2).useful(0).build(),
                Review.builder().reviewId(3).content("some content three")
                        .isPositive(Boolean.TRUE).userId(1).filmId(2).useful(0).build(),
                Review.builder().reviewId(4).content("some content four")
                        .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(0).build()
        );

        testReviews.forEach(reviewStorage::postReview);
        reviewStorage.putLike(1, 1);
        reviewStorage.putLike(1, 2);
        reviewStorage.putLike(2, 1);

        expectedList = List.of(
                Review.builder().reviewId(1).content("some content one")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(2).build(),
                Review.builder().reviewId(2).content("some content two")
                        .isPositive(Boolean.FALSE).userId(2).filmId(2).useful(1).build()
        );
        actualList = reviewStorage.readAllReviews(2);

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    void putDislike() {
        List<Review> expectedList;
        List<Review> actualList;

        List<Review> testReviews = List.of(
                Review.builder().reviewId(1).content("some content one")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(0).build(),
                Review.builder().reviewId(2).content("some content two")
                        .isPositive(Boolean.FALSE).userId(2).filmId(2).useful(0).build(),
                Review.builder().reviewId(3).content("some content three")
                        .isPositive(Boolean.TRUE).userId(1).filmId(2).useful(0).build(),
                Review.builder().reviewId(4).content("some content four")
                        .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(0).build()
        );

        testReviews.forEach(reviewStorage::postReview);
        reviewStorage.putDislike(3, 1);
        reviewStorage.putDislike(3, 2);
        reviewStorage.putDislike(4, 2);

        expectedList = List.of(
                Review.builder().reviewId(4).content("some content four")
                        .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(-1).build(),
                Review.builder().reviewId(3).content("some content three")
                        .isPositive(Boolean.TRUE).userId(1).filmId(2).useful(-2).build()
        );
        actualList = reviewStorage.readAllReviews(4).stream().skip(2).collect(Collectors.toList());

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    void deleteLike() {
        List<Review> expectedList;
        List<Review> actualList;
        List<Review> testReviews = List.of(
                Review.builder().reviewId(1).content("some content one")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(0).build(),
                Review.builder().reviewId(2).content("some content two")
                        .isPositive(Boolean.FALSE).userId(2).filmId(2).useful(0).build(),
                Review.builder().reviewId(3).content("some content three")
                        .isPositive(Boolean.TRUE).userId(1).filmId(2).useful(0).build(),
                Review.builder().reviewId(4).content("some content four")
                        .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(0).build()
        );

        testReviews.forEach(reviewStorage::postReview);
        reviewStorage.putLike(4, 1);
        reviewStorage.putLike(4, 2);
        reviewStorage.putLike(3, 1);
        reviewStorage.putDislike(1, 1);
        reviewStorage.putDislike(2, 1);
        reviewStorage.putDislike(2, 2);

        expectedList = List.of(
                Review.builder().reviewId(4).content("some content four")
                        .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(2).build(),
                Review.builder().reviewId(3).content("some content three")
                        .isPositive(Boolean.TRUE).userId(1).filmId(2).useful(1).build(),
                Review.builder().reviewId(1).content("some content one")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(-1).build(),
                Review.builder().reviewId(2).content("some content two")
                        .isPositive(Boolean.FALSE).userId(2).filmId(2).useful(-2).build()
        );
        actualList = reviewStorage.readAllReviews(4);

        assertArrayEquals(expectedList.toArray(), actualList.toArray());

        reviewStorage.deleteLike(4, 1);
        reviewStorage.deleteLike(4, 2);
        expectedList = List.of(
                Review.builder().reviewId(3).content("some content three")
                    .isPositive(Boolean.TRUE).userId(1).filmId(2).useful(1).build(),
                Review.builder().reviewId(4).content("some content four")
                        .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(0).build(),
                Review.builder().reviewId(1).content("some content one")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(-1).build(),
                Review.builder().reviewId(2).content("some content two")
                        .isPositive(Boolean.FALSE).userId(2).filmId(2).useful(-2).build()
        );

        actualList = reviewStorage.readAllReviews(4);

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    void deleteDislike() {
        List<Review> expectedList;
        List<Review> actualList;
        List<Review> testReviews = List.of(
                Review.builder().reviewId(1).content("some content one")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(0).build(),
                Review.builder().reviewId(2).content("some content two")
                        .isPositive(Boolean.FALSE).userId(2).filmId(2).useful(0).build(),
                Review.builder().reviewId(3).content("some content three")
                        .isPositive(Boolean.TRUE).userId(1).filmId(2).useful(0).build(),
                Review.builder().reviewId(4).content("some content four")
                        .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(0).build()
        );

        testReviews.forEach(reviewStorage::postReview);
        reviewStorage.putLike(4, 1);
        reviewStorage.putLike(4, 2);
        reviewStorage.putLike(3, 1);
        reviewStorage.putDislike(1, 1);
        reviewStorage.putDislike(2, 1);
        reviewStorage.putDislike(2, 2);

        expectedList = List.of(
                Review.builder().reviewId(4).content("some content four")
                        .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(2).build(),
                Review.builder().reviewId(3).content("some content three")
                        .isPositive(Boolean.TRUE).userId(1).filmId(2).useful(1).build(),
                Review.builder().reviewId(1).content("some content one")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(-1).build(),
                Review.builder().reviewId(2).content("some content two")
                        .isPositive(Boolean.FALSE).userId(2).filmId(2).useful(-2).build()
        );
        actualList = reviewStorage.readAllReviews(4);

        assertArrayEquals(expectedList.toArray(), actualList.toArray());

        reviewStorage.deleteDislike(2, 1);
        reviewStorage.deleteDislike(2, 2);

        expectedList = List.of(
                Review.builder().reviewId(4).content("some content four")
                        .isPositive(Boolean.FALSE).userId(2).filmId(1).useful(2).build(),
                Review.builder().reviewId(3).content("some content three")
                        .isPositive(Boolean.TRUE).userId(1).filmId(2).useful(1).build(),
                Review.builder().reviewId(2).content("some content two")
                        .isPositive(Boolean.FALSE).userId(2).filmId(2).useful(0).build(),
                Review.builder().reviewId(1).content("some content one")
                        .isPositive(Boolean.TRUE).userId(1).filmId(1).useful(-1).build()
        );
        actualList = reviewStorage.readAllReviews(4);

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }
}