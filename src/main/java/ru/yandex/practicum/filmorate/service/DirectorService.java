package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorDaoImpl;

import java.util.List;

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
}
