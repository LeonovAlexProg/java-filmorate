package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Validator {
    public static boolean validateFilm(Film film) {
        return film.getName() != null && film.getDescription().length() <= 200
                && film.getReleaseDate().isAfter(LocalDate.ofYearDay(1895, 361))
                && !film.getDuration().isNegative() && !film.getDuration().isZero();
    }

    public static boolean validateUser(User user) {
        if (user.getEmail() != null && user.getEmail().contains("@")
                && user.getLogin() != null && !user.getLogin().contains(" ")
                && user.getBirthday().isBefore(LocalDate.now())) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            return true;
        } else {
            return false;
        }
    }
}
