package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse filmExistsHandler(final FilmExistsException e) {
        return new ErrorResponse("film exists", String.format("name = %s, id = %d",e.getName(), e.getId()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse userExistsHandler(final UserExistsException e) {
        return new ErrorResponse("film exists", String.format("name = %s, id = %d",e.getName(), e.getId()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse filmNotFoundException(final FilmNotFoundException e) {
        return new ErrorResponse("film not found", String.format("id = %d", e.getId()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse("user not found", String.format("id = %d", e.getId()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse userFriendExistsHandler(final UserFriendExistsException e) {
        return new ErrorResponse("friend exists", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userFriendNotFoundHandler(final UserFriendNotFoundException e) {
        return new ErrorResponse(e.getMessage(), e.getParam());
    }
}
