package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
