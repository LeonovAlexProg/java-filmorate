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
                "is_positive = ?, " +
                "user_id = ?, " +
                "film_id = ?, " +
                "useful = ? " +
                "WHERE review_id = ?";

        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful(),
                review.getReviewId()
        );

        return readReview(review.getReviewId());
    }

    @Override
    public boolean deleteReview(int reviewId) {
        String sqlQuery = "DELETE FROM reviews WHERE review_id = ?";

        return jdbcTemplate.update(sqlQuery, reviewId) == 1;
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
