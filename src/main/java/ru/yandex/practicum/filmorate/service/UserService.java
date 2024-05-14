package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.CreationException;
import ru.yandex.practicum.filmorate.exceptions.DeleteException;
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
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            throw new CreationException("User not found, check data");
        }
        log.info("У пользователя с id: " + userId + " добавлен друг с id: " + friendId);
        return userStorage.addToFriends(userId, friendId);
    }

    public User deleteFriend(int userId, int friendId) {
        log.info("У пользователя с id: " + userId + " удалён друг с id: " + friendId);
        return userStorage.removeFriendById(userId, friendId);
    }

    public List<User> getListOfFriends(int userId) {

        log.info("Получен список друзей пользователя: " + userId);
        return userStorage.getListOfFriends(userId);
    }

    public List<User> getListOfCommonFriends(int userId, int friendId) {
        List<User> userFriends = userStorage.getListOfFriends(userId);
        List<User> secondsUserFriends = userStorage.getListOfFriends(friendId);
        List<User> result = userFriends.stream()
                .distinct()
                .filter(secondsUserFriends::contains)
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
        User user = userStorage.deleteUserById(id);
        if (user == null) {
            log.info("Пользователь с id " + id + " не был найден!");
            throw new DeleteException("Пользователь с id " + id + " не был удалён, проверьте id");
        }
        return user;
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User updateUser(User user) {
        User updatedUser = userStorage.updateUser(user);
        if (updatedUser == null) {
            log.info("Ошибка при обновлении пользователя (id: " + user.getId() + "): нет такого пользователя");
            throw new UpdateException("User " + user.getId() + " update exception");
        }
        return updatedUser;
    }

    public List<User> getUsers() {
        return userStorage.getAllUsers();
    }
}