package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.CreationException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private Integer id = 0;
    private HashMap<Integer, User> users = new HashMap<>();

    @GetMapping(value = "/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User createNewUser(@Valid @RequestBody User user) {
        if (user.getId() != null) {
            throw new CreationException("ID должен быть null");
        }
        replaceNameWithLogin(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            replaceNameWithLogin(user);
            users.replace(user.getId(), user);
            log.info("Пользователь с id " + user.getId() + " обновлен!");
            return user;
        }
        log.info("Ошибка при обновлении пользователя (id: " + user.getId() + "): нет такого пользователя");
        throw new UpdateException("User " + user.getId() + " update exception");
    }

    private void replaceNameWithLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private Integer generateId() {
        return ++this.id;
    }
}
