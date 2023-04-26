package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

public interface ReviewStorage {
    Review postReview(Review review);

    Review putReview(Review review);

    boolean deleteReview(int reviewId);

    Review readReview(int reviewId);
}
