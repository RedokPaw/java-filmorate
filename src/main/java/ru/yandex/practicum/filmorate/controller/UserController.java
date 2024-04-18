package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Реквест на получение списка всех пользователей");
        return userService.getUsers();
    }

    @PostMapping
    public User createNewUser(@Valid @RequestBody User user) {
        log.info("Реквест на создание нового пользователя");
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Реквест на обновление пользователя с id " + user.getId());
        return userService.updateUser(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public User addFriend(@PathVariable(value = "id") int userId, @PathVariable int friendId) {
        log.info("Реквест на добавление в друзья пользователя от id: " + userId + " к пользователю с id: " + friendId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable(value = "id") int userId, @PathVariable int friendId) {
        log.info("Реквест на удаления из друзей пользователя от id: " + userId + " пользователя с id: " + friendId);
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getListOfFriends(@PathVariable(value = "id") int userId) {
        log.info("Реквест на получение списка друзей пользователя с id: " + userId);
        return userService.getListOfFriends(userId);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getListOfCommonFriends(@PathVariable(value = "id") int userId, @PathVariable(value = "otherId")
    int friendId) {
        log.info("Реквест на получение списка общиих друзей пользователя с id: " + userId + " с пользователем с id: " +
                friendId);
        return userService.getListOfCommonFriends(userId, friendId);
    }
}
