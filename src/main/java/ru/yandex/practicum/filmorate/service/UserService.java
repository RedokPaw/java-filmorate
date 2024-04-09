package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ElementIsNullException;
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

    public User addFriend(int userId, int FriendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(FriendId);
        if (user == null || friend == null) {
            log.info("add friend error: user or friend is not found!");
            throw new ElementIsNullException("User is null");
        }
        if (!user.getFriends().add(friend.getId())) {
            log.info("Friend already exists!");
            return friend;
        }
        friend.getFriends().add(user.getId());
        return friend;
    }

    public User deleteFriend(int userId, int FriendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(FriendId);
        if (user == null || friend == null) {
            log.info("add friend error: user or friend is not found!");
            throw new ElementIsNullException("User is null");
        }
        if (!user.getFriends().remove(friend.getId())) {
            log.info("Friend is not exists!");
            return friend;
        }
        friend.getFriends().remove(user.getId());
        return friend;
    }

    public List<User> getListOfFriends(int userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.info("get list of friends error: user is not found!");
            throw new ElementIsNullException("User is null");
        }
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getListOfCommonFriends(int userId, int FriendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(FriendId);
        if (user == null || friend == null) {
            log.info("add friend error: user or friend is not found!");
            throw new ElementIsNullException("User is null");
        }
        List<User> result = user.getFriends().stream()
                .distinct()
                .filter(friend.getFriends()::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        return result;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User deleteUser(User user) {
        return userStorage.deleteUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }
}