package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Review addReview(Review review) {
        filmStorage.readFilm(review.getFilmId());
        userStorage.readUser(review.getUserId());

        return reviewStorage.postReview(review);
    }

    public Review updateReview(Review review) {
        filmStorage.readFilm(review.getFilmId());
        userStorage.readUser(review.getUserId());
        reviewStorage.readReview(review.getReviewId());

        return reviewStorage.putReview(review);
    }

    public boolean deleteReview(int id) {
        reviewStorage.readReview(id);
        return reviewStorage.deleteReview(id);
    }

    public Review getReview(int id) {
        return reviewStorage.readReview(id);
    }
}
