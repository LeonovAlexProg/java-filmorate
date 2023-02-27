package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    @Test
    void shouldReturnFalseForEmptyName() {
        Film film = new Film(1, null, "123", LocalDate.now(), Duration.ofHours(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateFilm(film);

        assertFalse(expectedValidation);
    }

    @Test
    void shouldReturnTrueForFilmWithName() {
        Film film = new Film(1, "name", "123", LocalDate.now(), Duration.ofHours(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateFilm(film);

        assertTrue(expectedValidation);
    }

    @Test
    void shouldReturnTrueForDescriptionLessThen200() {
        Film film = new Film(1, "name", "123", LocalDate.now(), Duration.ofHours(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateFilm(film);

        assertTrue(expectedValidation);
    }

    @Test
    void shouldReturnTrueForDescriptionOf200() {
        Film film = new Film(1, "name", "111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111", LocalDate.now(), Duration.ofHours(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateFilm(film);

        assertTrue(expectedValidation);
    }

    @Test
    void shouldReturnFalseForDescriptionMoreThen200() {
        Film film = new Film(1, "name", "111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
                LocalDate.now(), Duration.ofHours(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateFilm(film);

        assertFalse(expectedValidation);
    }

    @Test
    void shouldReturnTrueForDateAfter28December1895() {
        Film film = new Film(1, "name", "123", LocalDate.now(), Duration.ofHours(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateFilm(film);

        assertTrue(expectedValidation);
    }

    @Test
    void shouldReturnTrueForDateOf28December1895() {
        Film film = new Film(1, "name", "123", LocalDate.ofYearDay(1895, 362),
                Duration.ofHours(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateFilm(film);

        assertTrue(expectedValidation);
    }

    @Test
    void shouldReturnFalseForDateBefore28December1895() {
        Film film = new Film(1, "name", "123", LocalDate.ofYearDay(1895, 300),
                Duration.ofHours(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateFilm(film);

        assertFalse(expectedValidation);
    }

    @Test
    void shouldReturnTrueForPositiveDuration() {
        Film film = new Film(1, "name", "123", LocalDate.now(), Duration.ofHours(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateFilm(film);

        assertTrue(expectedValidation);
    }

    @Test
    void shouldReturnFalseForZeroDuration() {
        Film film = new Film(1, "name", "123", LocalDate.now(), Duration.ZERO);
        boolean expectedValidation;

        expectedValidation = Validator.validateFilm(film);

        assertFalse(expectedValidation);
    }

    @Test
    void shouldReturnFalseForNegativeDuration() {
        Film film = new Film(1, "name", "123", LocalDate.now(),
                Duration.ZERO.minus(Duration.ofHours(5)));
        boolean expectedValidation;

        expectedValidation = Validator.validateFilm(film);

        assertFalse(expectedValidation);
    }

    @Test
    void shouldReturnTrueForNonEmptyEmailWithAtCommercial() {
        User user = new User(1, "123@email.com", "Login", LocalDate.now().minusYears(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateUser(user);

        assertTrue(expectedValidation);
    }

    @Test
    void shouldReturnFalseForEmptyEmail() {
        User user = new User(1, null, "Login", LocalDate.now().minusYears(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateUser(user);

        assertFalse(expectedValidation);
    }

    @Test
    void shouldReturnFalseForNonEmptyEmailWithoutAtCommercial() {
        User user = new User(1, "123email.com", "Login", LocalDate.now().minusYears(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateUser(user);

        assertFalse(expectedValidation);
    }

    @Test
    void shouldReturnTrueForNonEmptyLoginWithoutSpaces() {
        User user = new User(1, "123@email.com", "Login", LocalDate.now().minusYears(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateUser(user);

        assertTrue(expectedValidation);
    }

    @Test
    void shouldReturnFalseForEmptyLogin() {
        User user = new User(1, "123@email.com", null, LocalDate.now().minusYears(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateUser(user);

        assertFalse(expectedValidation);
    }

    @Test
    void shouldReturnFalseForNonEmptyLoginWithSpaces() {
        User user = new User(1, "123@email.com", "Login ", LocalDate.now().minusYears(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateUser(user);

        assertFalse(expectedValidation);
    }

    @Test
    void shouldReturnNameOfAlexWithSetName() {
        User user = new User(1, "123@email.com", "Login", LocalDate.now().minusYears(1));
        String expectedName = "Alex";
        String actualName;

        Validator.validateUser(user);
        user.setName(expectedName);
        actualName = user.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    void shouldReturnNameEqualsToLoginWithEmptyName() {
        User user = new User(1, "123@email.com", "Login", LocalDate.now().minusYears(1));
        String expectedName = "Login";
        String actualName;

        Validator.validateUser(user);
        actualName = user.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    void shouldReturnTrueForBirthdayBeforeFuture() {
        User user = new User(1, "123@email.com", "Login", LocalDate.now().minusYears(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateUser(user);

        assertTrue(expectedValidation);
    }

    @Test
    void shouldReturnFalseForBirthdayInFuture() {
        User user = new User(1, "123@email.com", "Login", LocalDate.now().plusYears(1));
        boolean expectedValidation;

        expectedValidation = Validator.validateUser(user);

        assertFalse(expectedValidation);
    }
}