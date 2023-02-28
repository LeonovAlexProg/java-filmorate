package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(exclude = "id")
public class Film {
    private int id;

    @NotBlank
    private final String name;

    @Size(min = 0, max = 200)
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final Integer duration;
}
