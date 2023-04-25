package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
public class Director {
    @PositiveOrZero(message = "Id режиссера должно быть больше 0")
    private Integer id;
    @NotEmpty(message = "Имя режиссера не может быть пустым")
    private String name;
}