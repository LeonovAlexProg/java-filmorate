package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.validator.Validator.validateFilm;
import static ru.yandex.practicum.filmorate.validator.Validator.validateUser;

class ValidatorTest {
    static class FilmValidationTest {
        Rating rating;
        List<Genre> genres;

        @BeforeEach
        public void initAdditional() {
            rating = Rating.builder().build();
            genres = Collections.emptyList();
        }

        @Test
        void returnValidationExceptionForEmptyName() {
            Film film = Film.builder().name("").build();
            Exception exception = assertThrows(ValidationException.class, () -> validateFilm(film));

            String expectedMessage = "Пустое имя фильма";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void returnValidationExceptionForDescriptionMoreThen200() {
            Film film = Film.builder().id(0).name("test").description("1".repeat(201)).build();
            Exception exception = assertThrows(ValidationException.class, () -> validateFilm(film));

            String expectedMessage = "Длинна описания больше 200 символов";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void returnValidationExceptionForReleaseDateBefore28DecemberOf1895() {
            Film film = Film.builder().id(0).name("test").description("test")
                    .releaseDate(LocalDate.ofYearDay(1895, 360)).build();
            Exception exception = assertThrows(ValidationException.class, () -> validateFilm(film));

            String expectedMessage = "Дата выхода фильма ранее 28 декабря 1895 года";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void returnValidationExceptionForNegativeDurationOrZero() {
            Film filmWithNegativeDuration = Film.builder().id(0).name("test").description("test")
                    .releaseDate(LocalDate.now()).duration(-1).build();
            Exception exception = assertThrows(ValidationException.class, () -> validateFilm(filmWithNegativeDuration));

            String expectedMessage = "Отрицательная продолжительность фильма или равна нулю";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);

            Film filmWithZeroDuration = Film.builder().id(0).name("test").description("test")
                    .releaseDate(LocalDate.now()).duration(0).build();
            exception = assertThrows(ValidationException.class, () -> validateFilm(filmWithZeroDuration));

            expectedMessage = "Отрицательная продолжительность фильма или равна нулю";
            actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }
    }

    static class UserValidationTest {
        @Test
        void returnValidationExceptionForEmptyEmailOrEmailWithoutAtCommercial() {
            User userWithEmptyEmail = User.builder().email("").build();
            Exception exception = assertThrows(ValidationException.class, () -> validateUser(userWithEmptyEmail));

            String expectedMessage = "Почта пуста или не содержит @";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);

            User userWithoutAtCommercial = User.builder().email("pochta.yandex.ru").build();
            exception = assertThrows(ValidationException.class, () -> validateUser(userWithoutAtCommercial));

            expectedMessage = "Почта пуста или не содержит @";
            actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void returnValidationExceptionForEmptyLoginOrLoginWithSpaces() {
            User userWithEmptyLogin = User.builder().login("").build();
            Exception exception = assertThrows(ValidationException.class, () -> validateUser(userWithEmptyLogin));

            String expectedMessage = "Логин пуст или содержит пробелы";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);

            User userWithSpacedLogin = User.builder().login("Tom Anderson").build();

            expectedMessage = "Логин пуст или содержит пробелы";
            actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void returnValidationExceptionForFutureBirthday() {
            User user = User.builder().birthday(LocalDate.ofYearDay(3000, 200)).build();
            Exception exception = assertThrows(ValidationException.class, () -> validateUser(user));

            String expectedMessage = "День рождения находится в будущем";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void shouldReturnNameEqualsToLoginWithEmptyName() throws ValidationException {
            User user = User.builder().name("Tom").build();
            validateUser(user);

            String expectedName = "Tom";
            String actualName = user.getName();

            assertEquals(expectedName, actualName);
        }
    }
}