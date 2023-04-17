package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RatingDaoImplTest {
    private final RatingDaoImpl ratingDao;

    @Test
    void getAllRatings() {
        List<Rating> expectedList = List.of(Rating.builder().id(1).name("G").build(),
                Rating.builder().id(2).name("PG").build(),
                Rating.builder().id(3).name("PG-13").build(),
                Rating.builder().id(4).name("R").build(),
                Rating.builder().id(5).name("NC-17").build()
        );
        List<Rating> actualList;

        actualList = ratingDao.getAll();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    void getRatingById() {
        Rating expectedRating = Rating.builder().id(3).name("PG-13").build();
        Rating actualRating;

        actualRating = ratingDao.getById(3);

        assertEquals(expectedRating, actualRating);
    }
}