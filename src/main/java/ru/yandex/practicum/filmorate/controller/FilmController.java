package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidateService;
import ru.yandex.practicum.filmorate.validation.ValidateServiceImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private long seq = 1;
    private static final ValidateService validateService = new ValidateServiceImpl();

    @GetMapping
    public Collection<Film> allFilms() {
        log.info("Отображение списка фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        validateService.validate(newFilm);
        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.info("Успешное создание фильма с id - {}", newFilm.getId());
        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Обновление фильма с id - {}", newFilm.getId());
        return validateService.update(newFilm, films);
    }

    private long getNextId() {
        return seq++;
    }
}
