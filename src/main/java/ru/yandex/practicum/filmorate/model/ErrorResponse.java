package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ErrorResponse {
    private final String error;
    private final String message;
}
