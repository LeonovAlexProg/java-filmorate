package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    boolean containsDirector(int id);
    Director getDirectorById(int id);
    List<Director> getDirectors();
    Director addDirector(Director director);
    Director updateDirector(Director director);
    void deleteDirector(int id);
}
