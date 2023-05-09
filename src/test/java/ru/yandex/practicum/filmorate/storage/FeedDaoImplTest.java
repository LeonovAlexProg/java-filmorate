package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FeedDaoImplTest {

    private final FilmService filmService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final FeedDaoImpl feedDao;

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
                .directors(new ArrayList<>())
                .build();
        testFilmTwo = Film.builder().name("test2").mpa(rating).description("test description two")
                .releaseDate(LocalDate.of(1950, 12, 12)).duration(200).genres(genres)
                .directors(new ArrayList<>())
                .build();
        testUser = User.builder().name("tom").email("email@test.ru").login("anderson")
                .birthday(LocalDate.of(2000, 12, 12)).build();
        testUserTwo = User.builder().name("john").email("email2@test.ru").login("bjornson")
                .birthday(LocalDate.of(2000, 12, 12)).build();
    }

    @Test
    void getEmptyFeedTest() {
        userService.postUser(testUser);
        assertEquals(feedDao.getFeedList(testUser.getId()), Collections.emptyList());
    }

    @Test
    void getAddFriendsFeedTest() {
        User user1 = userService.postUser(testUser);
        User user2 = userService.postUser(testUserTwo);
        userService.addUserFriend(user1.getId(),user2.getId());
        Feed actualFeed = new ArrayList<>(feedDao.getFeedList(testUser.getId())).get(0);
        Feed expectedFeed = Feed.builder()
                .eventId(1)
                .eventType("FRIEND")
                .operation("ADD")
                .userId(user1.getId())
                .entityId(user2.getId())
                .timestamp(actualFeed.getTimestamp())
                .build();
        assertEquals(actualFeed, expectedFeed);
    }

    @Test
    void getPutLikeToFilmFeedTest() {
        User user = userService.postUser(testUser);
        Film film = filmService.postFilm(testFilm);
        filmService.putLikeOnFilm(film.getId(), film.getId());
        Feed actualFeed = new ArrayList<>(feedDao.getFeedList(user.getId())).get(0);
        Feed expectedFeed = Feed.builder()
                .eventId(1)
                .eventType("LIKE")
                .operation("ADD")
                .userId(user.getId())
                .entityId(film.getId())
                .timestamp(actualFeed.getTimestamp())
                .build();
        assertEquals(actualFeed, expectedFeed);
    }

    @Test
    void getReviewToFilmFeedTest() {
        User user = userService.postUser(testUser);
        Film film = filmService.postFilm(testFilm);
        Review review = reviewService.addReview(Review.builder()
                .filmId(film.getId())
                .isPositive(false)
                .userId(user.getId())
                .content("This film is soo bad.")
                .build());
        Feed actualFeed = new ArrayList<>(feedDao.getFeedList(user.getId())).get(0);
        Feed expectedFeed = Feed.builder()
                .eventId(1)
                .eventType("REVIEW")
                .operation("ADD")
                .userId(user.getId())
                .entityId(review.getReviewId())
                .timestamp(actualFeed.getTimestamp())
                .build();
        assertEquals(actualFeed, expectedFeed);
    }

    @Test
    void getFeedListTest() {
        User user = userService.postUser(testUser);
        Film film = filmService.postFilm(testFilm);
        User user2 = userService.postUser(testUserTwo);
        userService.addUserFriend(testUser.getId(),testUserTwo.getId());
        filmService.putLikeOnFilm(film.getId(), film.getId());
        reviewService.addReview(Review.builder()
                .filmId(film.getId())
                .isPositive(false)
                .userId(user.getId())
                .content("This film is soo bad.")
                .build());
        assertEquals(new ArrayList<>(feedDao.getFeedList(user.getId())).size(), 3);
    }
}
