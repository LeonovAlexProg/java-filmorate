package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "id")
@Builder
@Setter
public class Film {
    private int id;

    @NotBlank
    private final String name;

    private final List<Genre> genres;
    private final Rating mpa;
    @Size(max = 200)
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final Integer duration;
    private final Set<Integer> usersLikes = new HashSet<>();
}
