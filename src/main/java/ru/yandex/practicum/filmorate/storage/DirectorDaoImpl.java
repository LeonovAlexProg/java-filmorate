package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DirectorDaoImpl implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    public boolean containsDirector(Integer id) {
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet("select * from directors where director_id=?", id);
        if (directorRows.next()) {
            return true;
        } else {
            throw new DirectorNotFoundException(String.format("Режиссера с id=%d не существует", id));
        }
    }

    public Optional<Director> getDirectorById(Integer id) {
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet("select * from directors where director_id=?", id);
        if (directorRows.next()) {
            Director director = Director.builder()
                    .id(directorRows.getInt("director_id"))
                    .name(directorRows.getString("name"))
                    .build();
            return Optional.of(director);
        } else {
            throw new DirectorNotFoundException(String.format("Режиссера с id=%d не существует", id));
        }
    }

    public List<Director> getDirectors() {
        String sqlQuery = "select * from directors order by director_id";
        return jdbcTemplate.query(sqlQuery, this::makeDirector);
    }

    public Optional<Director> addDirector(Director director) {
        String sqlQuery = "insert into directors (name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            Integer id = keyHolder.getKey().intValue();
            return getDirectorById(id);
        } else {
            throw new RuntimeException(String.format("Произошла ошибка во время создания режиссера %s", director));
        }
    }

    public Optional<Director> updateDirector(Director director) {
        String sqlQuery = "update directors set name = ? where director_id = ?";
        jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        return getDirectorById(director.getId());
    }

    public Optional<Director> deleteDirector(Integer id) {
        Optional<Director> director = getDirectorById(id);
        String sqlQuery = "delete from directors where director_id = ?";
        jdbcTemplate.update(sqlQuery, id);
        return director;
    }

    private Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getInt("director_id"))
                .name(rs.getString("name"))
                .build();
    }
}
