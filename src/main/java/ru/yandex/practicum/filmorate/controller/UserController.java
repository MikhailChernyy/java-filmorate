package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidateService;
import ru.yandex.practicum.filmorate.validation.ValidateServiceImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Map<Long, User> users = new HashMap<>();
    private long seq = 1;
    private static final ValidateService validateService = new ValidateServiceImpl();

    @GetMapping
    public Collection<User> allUsers() {
        log.info("Отображение пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        validateService.validate(newUser);
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        log.info("Успешное создание пользователя c id - {}", newUser.getId());
        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Обновления пользователя с id - {}", newUser.getId());
        return validateService.update(newUser, users);
    }

    private long getNextId() {
        return seq++;
    }
}
