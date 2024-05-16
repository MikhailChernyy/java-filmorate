package ru.yandex.practicum.filmorate.storage.film;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InMemoryFilmStorage implements FilmStorage {

    static Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public void addFilm(long id, Film newFilm) {
        films.put(id, newFilm);
    }

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }
}
