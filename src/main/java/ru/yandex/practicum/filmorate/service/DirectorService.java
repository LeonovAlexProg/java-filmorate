package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorDaoImpl;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorService {
    private final DirectorDaoImpl directorDao;

    @Autowired
    DirectorService(DirectorDaoImpl directorDao) {
        this.directorDao = directorDao;
    }

    public List<Director> getDirectors() {
        return directorDao.getDirectors();
    }

    public Optional<Director> getDirector(Integer id) {
        return directorDao.getDirectorById(id);
    }

    public Optional<Director> addDirector(Director director) {
        return directorDao.addDirector(director);
    }

    public Optional<Director> updateDirector(Director director) {
        return directorDao.updateDirector(director);
    }

    public Optional<Director> deleteDirector(Integer id) {
        return directorDao.deleteDirector(id);
    }
}
