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
    private final ReviewStorage reviewDao;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Review addReview(Review review) {
        filmStorage.readFilm(review.getFilmId());
        userStorage.readUser(review.getUserId());

        return reviewDao.postReview(review);
    }

    //TODO доделать обновление и удаление обзоров
//    public Review updateReview(Review review) {
//        reviewDao.putReview(review);
//        return reviewDao.readReview(review.getReviewId());
//    }
//
//    public boolean deleteReview(int id) {
//        return reviewDao.deleteReview(id);
//    }

    public Review getReview(int id) {
        return reviewDao.readReview(id);
    }
}
