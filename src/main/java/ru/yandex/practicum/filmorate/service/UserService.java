package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.CreationException;
import ru.yandex.practicum.filmorate.exceptions.DeleteException;
import ru.yandex.practicum.filmorate.exceptions.ElementIsNullException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            log.info("add friend error: user or friend is not found!");
            throw new ElementIsNullException("User is null");
        }
        if (!user.getFriends().add(friend.getId())) {
            log.info("Friend already exists!");
            return friend;
        }
        log.info("У пользователя с id: " + userId + " добавлен друг с id: " + friendId);
        return friend;
    }

    public User deleteFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            log.info("delete friend error: user or friend is not found!");
            throw new ElementIsNullException("User is null");
        }
        if (!user.getFriends().remove(friend.getId())) {
            log.info("Friend is not exists!");
            return friend;
        }
        log.info("У пользователя с id: " + userId + " удалён друг с id: " + friendId);
        return friend;
    }

    public List<User> getListOfFriends(int userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.info("get list of friends error: user is not found!");
            throw new ElementIsNullException("User is null");
        }
        log.info("Получен список друзей пользователя: " + userId);
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getListOfCommonFriends(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            log.info("add friend error: user or friend is not found!");
            throw new ElementIsNullException("User is null");
        }
        List<User> result = user.getFriends().stream()
                .distinct()
                .filter(friend.getFriends()::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        log.info("Получен список общих друзей пользователя: " + userId + " с пользователем: " + friendId);
        return result;
    }

    public User addUser(User user) {
        if (user.getId() != null) {
            throw new CreationException("ID должен быть null");
        }
        return userStorage.addUser(user);
    }

    public User deleteUserById(int id) {
        User deletedUser = userStorage.deleteUserById(id);
        if (deletedUser == null) {
            log.info("Пользователь с id " + id + " не был найден!");
            throw new DeleteException("Пользователь с id " + id + " не был удалён, проверьте id");
        }
        return deletedUser;
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()) == null) {
            log.info("Ошибка при обновлении пользователя (id: " + user.getId() + "): нет такого пользователя");
            throw new UpdateException("User " + user.getId() + " update exception");
        }
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }
}