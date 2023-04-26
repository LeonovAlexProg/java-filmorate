package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DirectorDaoImplTest {
    private final DirectorDaoImpl directorDao;

    @Test
    public void testContainsDirector() {
        Director director = Director.builder()
                .id(1)
                .name("Name")
                .build();
        directorDao.addDirector(director);
        assertTrue(directorDao.containsDirector(1));
        assertThrows(DirectorNotFoundException.class, () -> directorDao.containsDirector(2));
    }

    @Test
    public void testAddDirector() {
        assertThrows(DirectorNotFoundException.class, () -> directorDao.containsDirector(1));
        Director director = Director.builder()
                .id(1)
                .name("Name")
                .build();
        directorDao.addDirector(director);
        assertTrue(directorDao.containsDirector(1));
    }

    @Test
    public void testGetDirectorById() {
        Director director = Director.builder()
                .id(1)
                .name("Name")
                .build();
        directorDao.addDirector(director);
        Optional<Director> directorOptional = directorDao.getDirectorById(1);
        assertThat(directorOptional)
                .isPresent()
                .hasValueSatisfying(d ->
                        assertThat(d).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetDirectors() {
        Director director = Director.builder()
                .name("Name")
                .build();
        directorDao.addDirector(director);
        Director director2 = Director.builder()
                .name("Name2")
                .build();
        directorDao.addDirector(director2);
        List<Director> directors = directorDao.getDirectors();
        assertEquals(2, directors.size());
        assertEquals("Name", directors.get(0).getName());
        assertEquals("Name2", directors.get(1).getName());
    }

    @Test
    public void testGetEmptyDirectors() {
        List<Director> directors = directorDao.getDirectors();
        assertEquals(0, directors.size());
    }

    @Test
    public void testUpdateDirector() {
        Director director = Director.builder()
                .id(1)
                .name("Name")
                .build();
        directorDao.addDirector(director);
        Director director2 = Director.builder()
                .id(1)
                .name("Name2")
                .build();
        directorDao.updateDirector(director2);
        Optional<Director> directorOptional = directorDao.getDirectorById(1);
        assertThat(directorOptional)
                .isPresent()
                .hasValueSatisfying(d ->
                        assertThat(d).hasFieldOrPropertyWithValue("name", "Name2")
                );
    }

    @Test
    public void testDeleteDirector() {
        Director director = Director.builder()
                .id(1)
                .name("Name")
                .build();
        directorDao.addDirector(director);
        directorDao.deleteDirector(1);
        assertThrows(DirectorNotFoundException.class, () -> directorDao.containsDirector(1));
    }
}