package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public List<Director> getDirectors() {
        log.debug(String.format("Get запрос режиссеров: Вернул %d элементов", DirectorService.getDirectors().size()));
        return directorService.getDirectors();
    }

    @GetMapping("{id}")
    public Optional<Director> getDirector(@PathVariable Integer id) {
        log.debug(String.format("Get запрос: запрошен режиссер с id=%d", id));
        return directorService.getDirector(id);
    }

    @PostMapping
    public Optional<Director> addDirector(@Valid @RequestBody Director director) {
        log.debug(String.format("Post: добавлен новый режиссер %s", director));
        return directorService.addDirector(director);
    }

    @PutMapping
    public Optional<Director> updateDirector(@Valid @RequestBody Director director) {
        log.debug(String.format("Put: режиссер изменен %s", director));
        return directorService.updateDirector(director);
    }

    @DeleteMapping("{id}")
    public Optional<Director> deleteDirector(@PathVariable Integer id) {
        log.debug(String.format("Delete: режиссер с id=%d удален", id));
        return directorService.deleteDirector(id);
    }

}
