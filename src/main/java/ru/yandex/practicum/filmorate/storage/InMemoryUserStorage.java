package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Integer id = 0;
    private HashMap<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        replaceNameWithLogin(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Пользователь с id " + user.getId() + " создан");
        return user;
    }

    @Override
    public User deleteUserById(int id) {
        log.info("Пользователь с id " + id + " удалён!");
        return users.remove(id);
    }

    @Override
    public User getUserById(int id) {
        log.info("Получен пользователь с id: " + id);
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        replaceNameWithLogin(user);
        users.replace(user.getId(), user);
        log.info("Пользователь с id " + user.getId() + " обновлен!");
        return user;
    }

    @Override
    public List<User> getUsers() {
        log.info("Получен список пользователей!");
        return new ArrayList<>(users.values());
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
