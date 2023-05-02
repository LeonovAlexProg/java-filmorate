package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final FeedStorage feedStorage;

    public Review addReview(Review review) {
        filmStorage.readFilm(review.getFilmId());
        userStorage.readUser(review.getUserId());

        Review postedReview = reviewStorage.postReview(review);
        feedStorage.updateFeed("REVIEW", "ADD", postedReview.getUserId(),
                postedReview.getReviewId(), Instant.now());
        return postedReview;
    }

    public Review updateReview(Review review) {
        filmStorage.readFilm(review.getFilmId());
        userStorage.readUser(review.getUserId());
        reviewStorage.readReview(review.getReviewId());

        Review updatedReview = reviewStorage.putReview(review);
        feedStorage.updateFeed("REVIEW", "UPDATE", updatedReview.getUserId(),
                updatedReview.getReviewId(), Instant.now());
        return updatedReview;
    }

    public boolean deleteReview(int id) {
        Review deletedReview = reviewStorage.readReview(id);
        boolean isDeleted = reviewStorage.deleteReview(id);
        if (isDeleted) {
                feedStorage.updateFeed("REVIEW", "REMOVE", deletedReview.getUserId(),
                        id, Instant.now());
        }
        return isDeleted;
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
