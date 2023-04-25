package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class Review {
    @Nullable
    private final Integer reviewId;

    @NotBlank
    private final String content;
    @NotNull
    private final Boolean isPositive;
    @NotNull
    private final Integer userId;
    @NotNull
    private final Integer filmId;
    @Nullable
    private Integer useful;

}
