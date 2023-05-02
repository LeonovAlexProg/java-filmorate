package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    DirectorController directorController;
    @Autowired
    FilmController filmController;
    @Autowired
    UserController userController;

    @Test
    public void searchEmptyDBEmptyEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/films/search/")
                )
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    public void searchEmptyDBQuery() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/films/search?query=whatever")
                )
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    public void searchEmptyDBTitle() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/films/search?query=whatever&by=title")
                )
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    public void searchEmptyDBDirector() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/films/search?query=whatever&by=director")
                )
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    public void searchEmptyDBAll() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/films/search?query=whatever&by=title,director")
                )
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    public void searchEmptyDBUnknownFieldAlone() throws Exception {
        mockMvc.perform(
                        get("/films/search?query=whatever&by=unknown")
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void searchEmptyDBUnknownAsPart() throws Exception {
        mockMvc.perform(
                        get("/films/search?query=whatever&by=unknown,title")
                )
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        get("/films/search?query=whatever&by=title,unknown")
                )
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        get("/films/search?query=whatever&by=unknown,director")
                )
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        get("/films/search?query=whatever&by=director,unknown")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void searchEmptyQuery() throws Exception {
        initDB();
        mockMvc.perform(
                        get("/films/search")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$.[0].name").value("Toy Story"))
                .andExpect(jsonPath("$.[1].name").value("Mirror"))
                .andExpect(jsonPath("$.[2].name").value("The Departed"))
                .andExpect(jsonPath("$.[3].name").value("House, M.D"));
    }

    @Test
    public void searchOnTitle() throws Exception {
        initDB();
        mockMvc.perform(
                        get("/films/search?query=or&by=title")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name").value("Toy Story"))
                .andExpect(jsonPath("$.[1].name").value("Mirror"));
    }

    @Test
    public void searchOnTitleNull() throws Exception {
        initDB();
        mockMvc.perform(
                        get("/films/search?query=non-existent-text&by=title")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void searchOnTitleWithRegister() throws Exception {
        initDB();
        mockMvc.perform(
                        get("/films/search?query=oR&by=title")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name").value("Toy Story"))
                .andExpect(jsonPath("$.[1].name").value("Mirror"));
    }

    @Test
    public void searchOnDirector() throws Exception {
        initDB();
        mockMvc.perform(
                        get("/films/search?query=se&by=director")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name").value("Toy Story"))
                .andExpect(jsonPath("$.[1].name").value("The Departed"));
    }

    @Test
    public void searchOnDirectorNull() throws Exception {
        initDB();
        mockMvc.perform(
                        get("/films/search?query=non-existent-text&by=title")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void searchOnDirectorWithRegister() throws Exception {
        initDB();
        mockMvc.perform(
                        get("/films/search?query=Se&by=director")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name").value("Toy Story"))
                .andExpect(jsonPath("$.[1].name").value("The Departed"));
    }

    @Test
    public void searchOnDirectorAndTitle() throws Exception {
        initDB();
        mockMvc.perform(
                        get("/films/search?query=se&by=director,title")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].name").value("Toy Story"))
                .andExpect(jsonPath("$.[1].name").value("The Departed"))
                .andExpect(jsonPath("$.[2].name").value("House, M.D"));
    }

    @Test
    public void searchOnDirectorAndTitleNull() throws Exception {
        initDB();
        mockMvc.perform(
                        get("/films/search?query=non-existent-text&by=title,director")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void searchOnDirectorAndTitleWithRegister() throws Exception {
        initDB();
        mockMvc.perform(
                        get("/films/search?query=Se&by=director,title")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].name").value("Toy Story"))
                .andExpect(jsonPath("$.[1].name").value("The Departed"))
                .andExpect(jsonPath("$.[2].name").value("House, M.D"));
    }

    private void initDB() {
        Genre comedy = Genre.builder().id(1).name("Комедия").build();
        Genre drama = Genre.builder().id(2).name("Драма").build();
        Genre cartoon = Genre.builder().id(3).name("Мультфильм").build();
        Genre thriller = Genre.builder().id(4).name("Триллер").build();
        Genre documentary = Genre.builder().id(5).name("Документальный").build();
        Genre actionMovie = Genre.builder().id(6).name("Боевик").build();

        Director olivia = Director.builder().id(1).name("Olivia Wilde").build();
        Director martin = Director.builder().id(2).name("Martin Scorsese").build();
        Director andrey = Director.builder().id(3).name("Andrei Tarkovsky").build();
        Director john = Director.builder().id(4).name("John Lasseter").build();

        Rating g = Rating.builder().id(1).name("G").build();
        Rating pg = Rating.builder().id(2).name("PG").build();
        Rating pg_13 = Rating.builder().id(3).name("PG-13").build();
        Rating r = Rating.builder().id(4).name("R").build();

        Film houseFilm = Film.builder()
                .name("House, M.D")
                .description("Description")
                .releaseDate(LocalDate.of(2014, 11, 16))
                .duration(44)
                .mpa(g)
                .directors(List.of(olivia))
                .genres(List.of(drama, actionMovie))
                .build();

        Film departedFilm = Film.builder()
                .name("The Departed")
                .description("Description")
                .releaseDate(LocalDate.of(2006, 9, 26))
                .duration(151)
                .mpa(pg)
                .directors(List.of(martin))
                .genres(List.of(drama, thriller))
                .build();

        Film mirrorFilm = Film.builder()
                .name("Mirror")
                .description("Description")
                .releaseDate(LocalDate.of(1975, 3, 7))
                .duration(107)
                .mpa(pg_13)
                .directors(List.of(andrey))
                .genres(List.of(drama, documentary))
                .build();

        Film toyFilm = Film.builder()
                .name("Toy Story")
                .description("Description")
                .releaseDate(LocalDate.of(1995, 11, 19))
                .duration(81)
                .mpa(r)
                .directors(List.of(john))
                .genres(List.of(comedy, cartoon))
                .build();

        User user1 = User.builder()
                .name("User1")
                .login("login1")
                .email("mail1@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();

        User user2 = User.builder()
                .name("User2")
                .login("login2")
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();

        User user3 = User.builder()
                .name("User3")
                .login("login3")
                .email("mail3@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();

        directorController.addDirector(olivia);
        directorController.addDirector(martin);
        directorController.addDirector(andrey);
        directorController.addDirector(john);

        filmController.postFilm(houseFilm);
        filmController.postFilm(departedFilm);
        filmController.postFilm(mirrorFilm);
        filmController.postFilm(toyFilm);

        userController.postUser(user1);
        userController.postUser(user2);
        userController.postUser(user3);

        filmController.putLikeOnFilm(4, 1);
        filmController.putLikeOnFilm(4, 2);
        filmController.putLikeOnFilm(4, 3);

        filmController.putLikeOnFilm(3, 1);
        filmController.putLikeOnFilm(3, 2);

        filmController.putLikeOnFilm(2, 1);

        assertEquals(4, directorController.getDirectors().size());
        assertEquals(4, filmController.getAllFilms().size());
        assertEquals(3, userController.getAllUsers().size());
    }
}