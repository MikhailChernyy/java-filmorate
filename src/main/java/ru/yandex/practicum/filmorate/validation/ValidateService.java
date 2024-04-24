package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface ValidateService {
    User validate(User user);

    User update(User user, Map<Long, User> map);

    void validate(Film film);

    Film update(Film film, Map<Long, Film> map);
}
