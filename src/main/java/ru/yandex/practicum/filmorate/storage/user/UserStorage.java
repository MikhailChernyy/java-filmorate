package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    Collection<User> getAllUsers();

    void addUser(long id, User newUser);

    Map<Long, User> getUsers();
}
