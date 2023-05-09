package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = "id")
@Builder
@Setter
public class Film {
    private int id;
    @NotBlank
    private final String name;
    private final List<Genre> genres;
    private List<Director> directors;
    private final Rating mpa;
    @Size(max = 200)
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final Integer duration;
    private int likes;
}
