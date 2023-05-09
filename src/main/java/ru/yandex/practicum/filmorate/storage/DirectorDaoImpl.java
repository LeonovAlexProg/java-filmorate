package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
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

@Component
@RequiredArgsConstructor
public class DirectorDaoImpl implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean containsDirector(int id) {
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet("select * from directors where director_id=?", id);
        if (directorRows.next()) {
            return true;
        } else {
            throw new DirectorNotFoundException(String.format("Режиссера с id=%d не существует", id));
        }
    }

    @Override
    public Director getDirectorById(int id) {
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet("select * from directors where director_id=?", id);
        if (directorRows.next()) {
            return Director.builder()
                    .id(directorRows.getInt("director_id"))
                    .name(directorRows.getString("name"))
                    .build();
        } else {
            throw new DirectorNotFoundException(String.format("Режиссера с id=%d не существует", id));
        }
    }

    @Override
    public List<Director> getDirectors() {
        String sqlQuery = "select * from directors order by director_id";
        return jdbcTemplate.query(sqlQuery, this::makeDirector);
    }

    @Override
    public Director addDirector(Director director) {
        String sqlQuery = "insert into directors (name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            int id = keyHolder.getKey().intValue();
            return getDirectorById(id);
        } else {
            throw new RuntimeException(String.format("Произошла ошибка во время создания режиссера %s", director));
        }
    }

    @Override
    public Director updateDirector(Director director) {
        if (containsDirector(director.getId())) {
            String sqlQuery = "update directors set name = ? where director_id = ?";
            jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        }
        return getDirectorById(director.getId());
    }

    @Override
    public void deleteDirector(int id) {
        if (containsDirector(id)) {
            String sqlQuery = "delete from directors where director_id = ?";
            jdbcTemplate.update(sqlQuery, id);
        }
    }

    private Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getInt("director_id"))
                .name(rs.getString("name"))
                .build();
    }
}
