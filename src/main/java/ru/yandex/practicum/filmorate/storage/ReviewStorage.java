package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

public interface ReviewStorage {
    Review postReview(Review review);

    Review readReview(int reviewId);
}
