package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addToFriends(long userId, long friendId) {
        User user;
        User friend;
        log.info("Добавление в друзья пользовтелей с id - {} и {}", userId, friendId);
        if (storage.getUsers().containsKey(userId)) {
            user = storage.getUsers().get(userId);
        } else {
            log.warn("Ползователь с id - {} не найден", userId);
            throw new NotFoundException("Пользователь не найден");
        }
        if (storage.getUsers().containsKey(friendId)) {
            friend = storage.getUsers().get(friendId);
        } else {
            log.warn("Ползователь с id - {} не найден", friendId);
            throw new NotFoundException("Пользователь не найден");
        }
        user.getFriendsId().add(friendId);
        friend.getFriendsId().add(userId);
    }

    public void removeFromFriends(long userId, long friendId) {
        User user;
        User friend;
        log.info("Удаление из друзей пользовтелей с id - {} и {}", userId, friendId);
        if (storage.getUsers().containsKey(userId)) {
            user = storage.getUsers().get(userId);
        } else {
            log.warn("Ползователь с id - {} не найден", userId);
            throw new NotFoundException("Пользователь не найден");
        }
        if (storage.getUsers().containsKey(friendId)) {
            friend = storage.getUsers().get(friendId);
        } else {
            log.warn("Ползователь с id - {} не найден", friendId);
            throw new NotFoundException("Пользователь не найден");
        }
        user.getFriendsId().remove(friendId);
        friend.getFriendsId().remove(userId);
    }

    public Collection<User> getUserFriends(long id) {
        User user;
        List<User> userFriends = new ArrayList<>();

        if (storage.getUsers().containsKey(id)) {
            user = storage.getUsers().get(id);
        } else {
            log.warn("Ползователь с id - {} не найден", id);
            throw new NotFoundException("Пользователь не найден");
        }

        for (long friendsId : user.getFriendsId()) {
            User friend = storage.getUsers().get(friendsId);
            userFriends.add(friend);
        }
        return userFriends;
    }

    public Collection<User> getMutualFriends(long id, long otherId) {
        if (!storage.getUsers().containsKey(id)) {
            log.warn("Ползователь с id - {} не найден", id);
            throw new NotFoundException("Пользователь не найден");
        }
        if (!storage.getUsers().containsKey(otherId)) {
            log.warn("Ползователь с id - {} не найден", otherId);
            throw new NotFoundException("Пользователь не найден");
        }
        User firstUser = storage.getUsers().get(id);
        User secondUser = storage.getUsers().get(otherId);
        Set<Long> intersection = new HashSet<>(firstUser.getFriendsId());
        intersection.retainAll(secondUser.getFriendsId());
        List<User> mutualFriends = new ArrayList<>();
        for (Long i : intersection) {
            mutualFriends.add(storage.getUsers().get(i));
        }
        return mutualFriends;
    }
}
