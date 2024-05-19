package ru.yandex.practicum.filmorate.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.service.validate.ValidateService;

import java.util.Collection;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/films")
public class FilmController {

    final FilmStorage storage;
    long seq = 1;
    final ValidateService validateService;
    final FilmService service;

    @Autowired
    public FilmController(FilmStorage storage, ValidateService validateService, FilmService service) {
        this.storage = storage;
        this.validateService = validateService;
        this.service = service;
    }

    @GetMapping
    public Collection<Film> allFilms() {
        log.info("Отображение списка фильмов");
        return storage.getAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        validateService.validate(newFilm);
        newFilm.setId(getNextId());
        storage.addFilm(newFilm.getId(), newFilm);
        log.info("Успешное создание фильма с id - {}", newFilm.getId());
        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Обновление фильма с id - {}", newFilm.getId());
        return validateService.update(newFilm, storage.getFilms());
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        service.addLike(id, userId);
        log.info("Лайк от пользователя с id - {} к фильму с id - {} успешно добавлен", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        service.removeLike(id, userId);
        log.info("Лайк от пользователя с id - {} к фильму с id - {} успешно удален", userId, id);
    }

    @GetMapping("/popular")
    public Collection<Film> mostPopularFilms(@RequestParam(value = "count", defaultValue = "10") int count) {
        log.info("Успешное отображение списка самых популярных фильмов в кол-ве - {}", count);
        return service.mostPopularFilms(count);
    }

    private long getNextId() {
        return seq++;
    }
}
