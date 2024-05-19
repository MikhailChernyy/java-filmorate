package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    void addFilm(long id, Film newFilm);

    Map<Long, Film> getFilms();
}
