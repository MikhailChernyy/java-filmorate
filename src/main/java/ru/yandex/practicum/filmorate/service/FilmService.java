package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long filmId, long userId) {
        Film film;
        log.info("Добавление лайка к фильму c id - {}", filmId);
        if (filmStorage.getFilms().containsKey(filmId)) {
            film = filmStorage.getFilms().get(filmId);
        } else {
            log.warn("Фильм с id - {} не найден", filmId);
            throw new NotFoundException("Фильм не найден");
        }
        if (userStorage.getUsers().containsKey(userId)) {
            film.getIdOfTheLikes().add(userId);
        } else {
            log.warn("Ползователь с id - {} не найден", userId);
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public void removeLike(long filmId, long userId) {
        Film film;
        log.info("Удаление лайка с фильма c id - {}", filmId);
        if (filmStorage.getFilms().containsKey(filmId)) {
            film = filmStorage.getFilms().get(filmId);
        } else {
            log.warn("Фильм с id - {} не найден", filmId);
            throw new NotFoundException("Фильм не найден");
        }
        if (userStorage.getUsers().containsKey(userId)) {
            film.getIdOfTheLikes().remove(userId);
        } else {
            log.warn("Ползователь с id - {} не найден", userId);
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public Collection<Film> mostPopularFilms(int count) {
        if (count <= 0) {
            log.warn("Неверное указание кол-ва фильмов! Введенное число - {}", count);
            throw new ValidationException("count", "Количество фильмов должно быть больше 0");
        }
        return filmStorage.getFilms().values().stream()
                .sorted((f1, f2) -> f2.getIdOfTheLikes().size() - f1.getIdOfTheLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
