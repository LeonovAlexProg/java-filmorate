package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Validator {
    public static void validateFilm(Film film) throws ValidationException {
        if (film.getName().equals("")) {
            throw new ValidationException("Пустое имя фильма");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Длинна описания больше 200 символов");
        } else if (!film.getReleaseDate().isAfter(LocalDate.ofYearDay(1895, 362))) {
            throw new ValidationException("Дата выхода фильма ранее 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("Отрицательная продолжительность фильма или равна нулю");
        }
    }

    public static void validateUser(User user) throws ValidationException {
        if (user.getEmail().equals("") || !user.getEmail().contains("@")) {
            throw new ValidationException("Почта пуста или не содержит @");
        } else if (user.getLogin().equals("") || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин пуст или содержит пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения находится в будущем");
        }

        if (user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }
}
