package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

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

    public List<Review> getAllReviews(int filmId, int count) {
        if (filmId == 0) {
            return reviewStorage.readAllReviews(count);
        }
        else {
            filmStorage.readFilm(filmId);
            return reviewStorage.readAllReviewsByFilmId(filmId, count);
        }
    }

    public boolean putLikeOnReview(int reviewId, int userId) {
        reviewStorage.readReview(reviewId);
        userStorage.readUser(userId);

        return reviewStorage.putLike(reviewId, userId);
    }

    public boolean putDislikeOnReview(int reviewId, int userId) {
        reviewStorage.readReview(reviewId);
        userStorage.readUser(userId);

        return reviewStorage.putDislike(reviewId, userId);
    }

    public boolean deleteLike(int reviewId, int userId) {
        reviewStorage.readReview(reviewId);
        userStorage.readUser(userId);

        return reviewStorage.deleteLike(reviewId, userId);
    }

    public boolean deleteDislike(int reviewId, int userId) {
        reviewStorage.readReview(reviewId);
        userStorage.readUser(userId);

        return reviewStorage.deleteDislike(reviewId, userId);
    }
}
