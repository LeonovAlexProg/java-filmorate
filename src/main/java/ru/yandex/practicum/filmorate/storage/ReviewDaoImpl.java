package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewDaoImpl implements ReviewStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review postReview(Review review) {
        String sqlQuery = "INSERT INTO reviews (content, is_positive, user_id, film_id) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"review_id"});
            statement.setString(1, review.getContent());
            statement.setBoolean(2, review.getIsPositive());
            statement.setInt(3, review.getUserId());
            statement.setInt(4, review.getFilmId());
            return statement;
        }, keyHolder);

        return readReview(keyHolder.getKey().intValue());
    }

    //TODO разобраться с тестами Postman и SQL запросом
    //тест - Review update to positive
    @Override
    public Review readReview(int reviewId) {
        String sqlQuery = "SELECT review_id, content, is_positive, user_id, film_id, useful " +
                "FROM reviews WHERE review_id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToReview, reviewId);
        } catch (EmptyResultDataAccessException exc) {
            log.debug("Review id = {} not found", reviewId);
            throw new ReviewNotFoundException("Review not found", reviewId);
        }
    }

    @Override
    public Review putReview(Review review) {
        String sqlQuery = "UPDATE reviews SET " +
                "content = ?, " +
                "is_positive = ? " +
                "WHERE review_id = ?";

        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );

        return readReview(review.getReviewId());
    }

    @Override
    public boolean deleteReview(int reviewId) {
        String deleteLikeQuery = "DELETE FROM review_likes WHERE review_id = ?";
        String deleteReviewQuery = "DELETE FROM reviews WHERE review_id = ?";

        jdbcTemplate.update(deleteLikeQuery, reviewId);
        return jdbcTemplate.update(deleteReviewQuery, reviewId) == 1;
    }

    @Override
    public List<Review> readAllReviews(int count) {
        String sqlQuery = "SELECT review_id, " +
                "content, " +
                "is_positive, " +
                "user_id, " +
                "film_id, " +
                "useful " +
                "FROM reviews " +
//                "ORDER BY review_id " +
                "ORDER BY useful DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToReview, count);
    }

    @Override
    public List<Review> readAllReviewsByFilmId(int filmId, int count) {
        String sqlQuery = "SELECT review_id, " +
                "content, " +
                "is_positive, " +
                "user_id, " +
                "film_id, " +
                "useful " +
                "FROM reviews WHERE film_id = ? " +
//                "ORDER BY review_id " +
                "ORDER BY useful DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToReview, filmId, count);
    }

    @Override
    public boolean putLike(int reviewId, int userId) {
        String putLikeQuery = "INSERT INTO review_likes (review_id, user_id, is_positive) " +
                "VALUES (?, ?, true)";
        String updateUseful = "UPDATE reviews SET useful = useful + 1 " +
                "WHERE review_id = ?";

        if (jdbcTemplate.update(putLikeQuery, reviewId, userId) == 1) {
            return jdbcTemplate.update(updateUseful, reviewId) == 1;
        }

        return false;
    }

    @Override
    public boolean putDislike(int reviewId, int userId) {
        String putDislikeQuery = "INSERT INTO review_likes (review_id, user_id, is_positive) " +
                "VALUES (?, ?, false)";
        String updateUsefulQuery = "UPDATE reviews SET useful = useful - 1 " +
                "WHERE review_id = ?";

        if (jdbcTemplate.update(putDislikeQuery, reviewId, userId) == 1) {
            return jdbcTemplate.update(updateUsefulQuery, reviewId) == 1;
        }

        return false;
    }

    @Override
    public boolean deleteLike(int reviewId, int userId) {
        String deleteLikeQuery = "DELETE FROM review_likes " +
                "WHERE review_id = ? AND user_id = ?";
        String updateUsefulQuery = "UPDATE reviews SET useful = useful - 1 " +
                "WHERE review_id = ?";

        if (jdbcTemplate.update(deleteLikeQuery, reviewId, userId) == 1) {
            return jdbcTemplate.update(updateUsefulQuery, reviewId) == 1;
        }

        return false;
    }

    @Override
    public boolean deleteDislike(int reviewId, int userId) {
        String deleteDislikeQuery = "DELETE FROM review_likes " +
                "WHERE review_id = ? AND user_id = ?";
        String updateUsefulQuery = "UPDATE reviews SET useful = useful + 1 " +
                "WHERE review_id = ?";

        if (jdbcTemplate.update(deleteDislikeQuery, reviewId, userId) == 1) {
            return jdbcTemplate.update(updateUsefulQuery, reviewId) == 1;
        }

        return false;
    }

    private Review mapRowToReview(ResultSet resultSet, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(resultSet.getInt("review_id"))
                .content(resultSet.getString("content"))
                .isPositive(resultSet.getBoolean("is_positive"))
                .userId(resultSet.getInt("user_id"))
                .filmId(resultSet.getInt("film_id"))
                .useful(resultSet.getInt("useful"))
                .build();
    }
}
