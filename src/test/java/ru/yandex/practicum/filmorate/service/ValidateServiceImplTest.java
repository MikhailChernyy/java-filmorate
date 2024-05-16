package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DateTimeException;
import ru.yandex.practicum.filmorate.exception.DimensionalViolationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.validate.ValidateService;
import ru.yandex.practicum.filmorate.service.validate.ValidateServiceImpl;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidateServiceImplTest {
    private static final ValidateService validateService = new ValidateServiceImpl();

    private RuntimeException exception;

    User user1 = new User(2L, "name@yandex.ru", "login123", "name123", LocalDate.of(2002, Month.NOVEMBER, 22));

    Film film1 = new Film(1L, "Titanic", "CryFilm", LocalDate.of(1997, Month.NOVEMBER, 1), 194);

    @Test
    public void filmNameShouldNotBeBlankTest() {
        film1.setName("");
        exception = new ConditionsNotMetException("");
        assertEquals("Название должно быть указано", createThrownForFilm(exception));
    }

    @Test
    public void filmDescriptionShouldNotExceed200Characters() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 210; i++) {
            stringBuilder.append(i);
        }
        film1.setDescription(stringBuilder.toString());
        exception = new DimensionalViolationException("");
        assertEquals("Длина описания должна быть меньше 200 символов", createThrownForFilm(exception));
    }

    @Test
    public void filmReleaseDateShouldNotEarlierBirthdayMovie() {
        film1.setReleaseDate(LocalDate.of(1884, Month.DECEMBER, 1));
        exception = new DateTimeException("");
        assertEquals("Дата релиза не должна быть раньше 28.12.1895", createThrownForFilm(exception));
    }

    @Test
    public void filmDurationShouldNotBeNegative() {
        film1.setDuration(-1);
        exception = new DateTimeException("");
        assertEquals("Продолжительность не должна быть отрицательной", createThrownForFilm(exception));
    }

    @Test
    public void userEmailShouldNotBeBlank() {
        user1.setEmail("");
        exception = new ConditionsNotMetException("");
        assertEquals("Имейл должен быть заполнен", createThrownForUser(exception));
    }

    @Test
    public void userEmailShouldContainCharacterAt() {
        user1.setEmail("nameyandex.ru");
        exception = new ConditionsNotMetException("");
        assertEquals("Имейл должен содержать символ - @", createThrownForUser(exception));
    }

    @Test
    public void userLoginShouldNotBeBlank() {
        user1.setLogin("");
        exception = new ConditionsNotMetException("");
        assertEquals("Логин не должен быть пустым", createThrownForUser(exception));
    }

    @Test
    public void userLoginShouldNotContainWhiteSpace() {
        user1.setLogin("Na me");
        exception = new ConditionsNotMetException("");
        assertEquals("Логин не должен содержать пробелы", createThrownForUser(exception));
    }

    @Test
    public void userBirthdayShouldNotBeInFuture() {
        user1.setBirthday(LocalDate.of(2025, Month.APRIL, 12));
        exception = new DateTimeException("");
        assertEquals("Дата рождения не может быть в будущем", createThrownForUser(exception));
    }

    @Test
    public void userNameReplacedOnLoginIfBlank() {
        User user2 = user1;
        user2.setName(user1.getLogin());
        user1.setName("");
        assertEquals(user2, user1);
    }

    @Test
    public void createdFilm() {
        assertDoesNotThrow(() -> validateService.validate(film1));
    }

    @Test
    public void createdUser() {
        assertDoesNotThrow(() -> {
            validateService.validate(user1);
        });
    }

    private String createThrownForFilm(RuntimeException exception) {
        Throwable thrown = assertThrows(exception.getClass(), () -> validateService.validate(film1));
        return thrown.getMessage();
    }

    private String createThrownForUser(RuntimeException exception) {
        Throwable thrown = assertThrows(exception.getClass(), () -> validateService.validate(user1));
        return thrown.getMessage();
    }
}