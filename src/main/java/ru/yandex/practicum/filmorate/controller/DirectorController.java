package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<Director> getDirectors() {
        List<Director> directors = directorService.getDirectors();
        log.debug(String.format("Get: Запрос режиссеров. Вернул %d элементов", directors.size()));
        return directors;
    }

    @GetMapping("{id}")
    public Director getDirector(@PathVariable Integer id) {
        log.debug(String.format("Get: запрошен режиссер с id=%d", id));
        return directorService.getDirector(id);
    }

    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director) {
        log.debug(String.format("Post: запрос на добавление режиссера %s", director));
        return directorService.addDirector(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        log.debug(String.format("Put: запрос на изменение режиссера %s", director));
        return directorService.updateDirector(director);
    }

    @DeleteMapping("{id}")
    public void deleteDirector(@PathVariable Integer id) {
        log.debug(String.format("Delete: запрос на удаление режиссера с id=%d", id));
        directorService.deleteDirector(id);
    }

}
