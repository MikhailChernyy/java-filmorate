package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

@Slf4j
public class ValidateServiceImpl implements ValidateService {
 private static final int MAX_DESCRIPTION = 200;

    @Override
    public User validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Ошибка заполнения имейла");
            throw new ConditionsNotMetException("Имейл должен быть заполнен");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Ошибка заполнения имейла");
            throw new ConditionsNotMetException("Имейл должен содержать символ - @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Ошибка заполнения логина");
            throw new ConditionsNotMetException("Логин не должен быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Ошибка заполнения логина");
            throw new ConditionsNotMetException("Логин не должен содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка заполнения даты рождения");
            throw new DateTimeException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Успешная валидация");
        return user;
    }

    @Override
    public User update(User newUser, Map<Long, User> users) {
        if (newUser.getId() == null) {
            log.warn("Ошибка указания id");
            throw new ConditionsNotMetException("id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (!newUser.getEmail().equals(oldUser.getEmail())) {
                for (User user : users.values()) {
                    if (user.getEmail().equals(newUser.getEmail())) {
                        log.warn("Ошибка заполнения имейла");
                        throw new DuplicateDataException("Имайл не должен повторяться");
                    }
                }
            }
            oldUser.setLogin(validate(newUser).getLogin());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            return oldUser;
        }
        log.warn("Ошибка указания id");
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    @Override
    public void validate(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Ошибка указания названия");
            throw new ConditionsNotMetException("Название должно быть указано");
        }
        if (film.getDescription().getBytes().length > MAX_DESCRIPTION) {
            log.warn("Ошибка указания описания");
            throw new DimensionalViolationException("Длина описания должна быть меньше 200 символов");
        }
        if (film.getDuration() < 0) {
            log.warn("Ошибка указания описания");
            throw new DateTimeException("Продолжительность не должна быть отрицательной");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.warn("Ошибка указания даты релиза");
            throw new DateTimeException("Дата релиза не должна быть раньше 28.12.1895");
        }
        log.info("Успешная валидация");
    }

    @Override
    public Film update(Film newFilm, Map<Long, Film> films) {
        if (newFilm.getId() == null) {
            log.warn("Ошибка указания id");
            throw new ConditionsNotMetException("id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            validate(newFilm);
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            return newFilm;
        }
        log.warn("Ошибка указания id");
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }
}
