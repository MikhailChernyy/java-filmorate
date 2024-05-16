package ru.yandex.practicum.filmorate.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.service.validate.ValidateService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    final UserStorage storage;
    long seq = 1;
    final ValidateService validateService;
    final UserService service;

    @Autowired
    public UserController(UserStorage storage, ValidateService validateService, UserService service) {
        this.storage = storage;
        this.validateService = validateService;
        this.service = service;
    }

    @GetMapping
    public Collection<User> allUsers() {
        log.info("Отображение пользователей");
        return storage.getAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        validateService.validate(newUser);
        newUser.setId(getNextId());
        storage.addUser(newUser.getId(), newUser);
        log.info("Успешное создание пользователя c id - {}", newUser.getId());
        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Обновления пользователя с id - {}", newUser.getId());
        return validateService.update(newUser, storage.getUsers());
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable long id, @PathVariable long friendId) {
        service.addToFriends(id, friendId);
        log.info("Успешное добавление в друзья пользователей с id - {} и {}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable long id, @PathVariable long friendId) {
        service.removeFromFriends(id, friendId);
        log.info("Успешное удаление из друзей пользователей с id - {} и {}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable long id) {
        log.info("Успешное получение списка друзей у пользователя с id - {}", id);
        return service.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Успешное получение списка друзей у пользователя с id - {}, общих с пользователем с id - {}", id, otherId);
        return service.getMutualFriends(id, otherId);
    }

    private long getNextId() {
        return seq++;
    }
}
