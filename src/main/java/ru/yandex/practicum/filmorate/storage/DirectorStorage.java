package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    boolean containsDirector(Integer id);
    Optional<Director> getDirectorById(Integer id);
    List<Director> getDirectors();
    Optional<Director> addDirector(Director director);
    Optional<Director> updateDirector(Director director);
    Optional<Director> deleteDirector(Integer id);
}
