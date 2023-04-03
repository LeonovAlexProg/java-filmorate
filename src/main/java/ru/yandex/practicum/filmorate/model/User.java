package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Builder
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
    private final Map<Boolean, Integer> areFriends = new HashMap<>();
}
