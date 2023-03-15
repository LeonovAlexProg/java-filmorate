package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "id")
public class User {
    private int id;
    private final Set<Integer> friends = new HashSet<>();
    private final Map<Boolean, Integer> areFriends = new HashMap<>();

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String login;

    private String name;
    @PastOrPresent
    private final LocalDate birthday;
}
