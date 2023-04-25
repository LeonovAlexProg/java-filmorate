package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review postReview(Review review);

    Review putReview(Review review);

    boolean deleteReview(int reviewId);

    Review readReview(int reviewId);

    List<Review> readAllReviews(int count);
    List<Review> readAllReviewsByFilmId(int filmId, int count);

    boolean putLike(int reviewId, int userId);

    boolean putDislike(int reviewId, int userId);

    boolean deleteLike(int reviewId, int userId);
    boolean deleteDislike(int reviewId, int userId);
}
