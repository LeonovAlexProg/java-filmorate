package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.validator.Validator.validateFilm;
import static ru.yandex.practicum.filmorate.validator.Validator.validateUser;

class ValidatorTest {
    static class FilmValidationTest {
        @Test
        void returnValidationExceptionForEmptyName() {
            Film film = new Film("", "description", LocalDate.now(), 100);
            Exception exception = assertThrows(ValidationException.class, () -> validateFilm(film));

            String expectedMessage = "Пустое имя фильма";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void returnValidationExceptionForDescriptionMoreThen200() {
            Film film = new Film("name", "1".repeat(201),
                    LocalDate.now(), 100);
            Exception exception = assertThrows(ValidationException.class, () -> validateFilm(film));

            String expectedMessage = "Длинна описания больше 200 символов";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void returnValidationExceptionForReleaseDateBefore28DecemberOf1895() {
            Film film = new Film("name", "description", LocalDate.ofYearDay(1895, 360),
                    100);
            Exception exception = assertThrows(ValidationException.class, () -> validateFilm(film));

            String expectedMessage = "Дата выхода фильма ранее 28 декабря 1895 года";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void returnValidationExceptionForNegativeDurationOrZero() {
            Film filmWithNegativeDuration = new Film("name", "description", LocalDate.now(), -1);
            Exception exception = assertThrows(ValidationException.class, () -> validateFilm(filmWithNegativeDuration));

            String expectedMessage = "Отрицательная продолжительность фильма или равна нулю";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);

            Film filmWithZeroDuration = new Film("name", "description", LocalDate.now(), 0);
            exception = assertThrows(ValidationException.class, () -> validateFilm(filmWithZeroDuration));

            expectedMessage = "Отрицательная продолжительность фильма или равна нулю";
            actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }
    }

    static class UserValidationTest {
        @Test
        void returnValidationExceptionForEmptyEmailOrEmailWithoutAtCommercial() {
            User userWithEmptyEmail = new User("", "Tom", LocalDate.ofYearDay(1990, 200));
            Exception exception = assertThrows(ValidationException.class, () -> validateUser(userWithEmptyEmail));

            String expectedMessage = "Почта пуста или не содержит @";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);

            User userWithoutAtCommercial = new User("pochta.yandex.ru", "Tom", LocalDate.ofYearDay(1990, 200));
            exception = assertThrows(ValidationException.class, () -> validateUser(userWithoutAtCommercial));

            expectedMessage = "Почта пуста или не содержит @";
            actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void returnValidationExceptionForEmptyLoginOrLoginWithSpaces() {
            User userWithEmptyLogin = new User("pochta@yandex.ru", "", LocalDate.ofYearDay(1990, 200));
            Exception exception = assertThrows(ValidationException.class, () -> validateUser(userWithEmptyLogin));

            String expectedMessage = "Логин пуст или содержит пробелы";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);

            User userWithSpacedLogin = new User("pochta@yandex.ru", "Tom Anderson", LocalDate.ofYearDay(1990, 200));
            exception = assertThrows(ValidationException.class, () -> validateUser(userWithSpacedLogin));

            expectedMessage = "Логин пуст или содержит пробелы";
            actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void returnValidationExceptionForFutureBirthday() {
            User user = new User("pochta@yandex.ru", "Tom", LocalDate.ofYearDay(3000, 200));
            Exception exception = assertThrows(ValidationException.class, () -> validateUser(user));

            String expectedMessage = "День рождения находится в будущем";
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        @Test
        void shouldReturnNameEqualsToLoginWithEmptyName() throws ValidationException {
            User user = new User("pochta@yandex.ru", "Tom", LocalDate.ofYearDay(1990, 200));
            validateUser(user);

            String expectedName = "Tom";
            String actualName = user.getName();

            assertEquals(expectedName, actualName);
        }
    }
}