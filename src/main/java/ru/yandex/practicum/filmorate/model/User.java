package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "id")
public class User {
    private final Set<Long> friends;
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
