package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.CreationException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
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
        if (user.getId() != null) {
            throw new CreationException("ID должен быть null");
        }
        replaceNameWithLogin(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Пользователь с id " + user.getId() + " создан");
        return user;
    }

    @Override
    public User deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            log.info("Пользователь с id " + user.getId() + " удалён!");
            return users.remove(user.getId());
        }
        log.info("Пользователь с id " + user.getId() + " не был найден!");
        return user;
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            replaceNameWithLogin(user);
            users.replace(user.getId(), user);
            log.info("Пользователь с id " + user.getId() + " обновлен!");
            return user;
        }
        log.info("Ошибка при обновлении пользователя (id: " + user.getId() + "): нет такого пользователя");
        throw new UpdateException("User " + user.getId() + " update exception");
    }

    @Override
    public List<User> getUsers() {
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
