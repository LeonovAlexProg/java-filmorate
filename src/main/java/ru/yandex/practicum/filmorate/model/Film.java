package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "id")
public class Film {
    private int id;

    @NotBlank
    private final String name;

    private final Set<String> genre;

    private final String MPA;

    @Size(max = 200)
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final Integer duration;
    private final Set<Integer> usersLikes = new HashSet<>();
}
