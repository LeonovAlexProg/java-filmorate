package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(exclude = "id")
public class User {
    private int id;
    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String login;

    private String name;
    @PastOrPresent
    private final LocalDate birthday;
}
