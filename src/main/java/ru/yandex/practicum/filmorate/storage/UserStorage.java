package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User deleteUserById(int id);

    User getUserById(int id);

    User updateUser(User user);

    List<User> getAllUsers();

    User addToFriends(int userId, int friendId);

    User removeFriendById(int userId, int friendId);

    List<User> getListOfFriends(int userId);
}
