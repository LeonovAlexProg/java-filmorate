package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Data
@EqualsAndHashCode(exclude = "id")
@Builder
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
    @Singular private final Map<Integer, Boolean> friends;
}
