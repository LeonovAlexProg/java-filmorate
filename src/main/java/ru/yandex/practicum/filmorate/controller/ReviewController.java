package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public Review postNewReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping("/reviews")
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/reviews/{id}")
    public boolean deleteReview(@PathVariable int id) {
        return reviewService.deleteReview(id);
    }

    @GetMapping("/reviews/{id}")
    public Review getReview(@PathVariable int id) {
        return reviewService.getReview(id);
    }

    @GetMapping("/reviews")
    public List<Review> getReviewsByFilm(@RequestParam(required = false, defaultValue = "0") int filmId,
                                         @RequestParam(required = false, defaultValue = "10") int count) {
        return reviewService.getAllReviews(filmId, count);
    }

    @PutMapping("/reviews/{id}/like/{userId}")
    public boolean putLikeOnReview(@PathVariable(value = "id") int reviewId,
                                 @PathVariable int userId) {
        return reviewService.putLikeOnReview(reviewId, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}")
    public boolean putDislikeOnReview(@PathVariable(value = "id") int reviewId,
                                 @PathVariable int userId) {
        return reviewService.putDislikeOnReview(reviewId, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")
    public boolean deleteLikeFromReview(@PathVariable(value = "id") int reviewId,
                                    @PathVariable int userId) {
        return reviewService.deleteLike(reviewId, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    public boolean deletedislikeFromReview(@PathVariable(value = "id") int reviewId,
                                        @PathVariable int userId) {
        return reviewService.deleteDislike(reviewId, userId);
    }
}
