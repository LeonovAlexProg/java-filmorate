package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FilmDirectorDao {
    JdbcTemplate jdbcTemplate;
    DirectorDaoImpl directorDao;

    public FilmDirectorDao(JdbcTemplate jdbcTemplate, DirectorDaoImpl directorDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.directorDao = directorDao;
    }

    public List<Director> getFilmDirectors(Integer id) {
        String sqlQuery = "select * from film_directors where film_id = ? order by director_id";
        return jdbcTemplate.query(sqlQuery, this::makeFilmDirector, id).stream()
                .map(FilmDirector::getDirectorId)
                .map(directorDao::getDirectorById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void deleteFilmDirectors(Integer id) {
        String sqlQuery = "delete from film_directors where film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public void addDirectorToFilm(Integer filmId, Integer directorId) {
        if (directorDao.containsDirector(directorId)) {
            String sqlQuery = "merge into film_directors KEY (film_id, director_id) values(?, ?)";
            jdbcTemplate.update(sqlQuery, filmId, directorId);
        }
    }

    private FilmDirector makeFilmDirector(ResultSet rs, int rowNum) throws SQLException {
        return new FilmDirector(rs.getInt("film_id"), rs.getInt("director_id"));
    }
}
