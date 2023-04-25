package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class Review {
    @Nullable
    private final Integer reviewId;
    @NotEmpty(message = "Name may not be empty")
    private final String content;
//TODO разобраться с корректной сериализацией имени поля isPositive
    private final boolean isPositive;
    @NotNull
    private final int userId;
    @NotNull
    private final int filmId;

    private int useful = 0;

}
