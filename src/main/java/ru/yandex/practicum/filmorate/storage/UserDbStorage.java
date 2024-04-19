package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserIsNullException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("user").usingGeneratedKeyColumns("id");
        Integer newId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(newId);
        return user;
    }

    @Override
    public User deleteUserById(int id) {
        String sqlQuery = "DELETE FROM user WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery, id) == 0) {
            return null;
        } else return getUserById(id);

    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE user SET EMAIL= ?, NAME= ?, LOGIN= ? , BIRTHDAY= ? WHERE ID= ?;";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getName(), user.getLogin(), user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM user";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User addToFriends(int userId, int friendId) {
        String sqlQuery = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return getUserById(friendId);
    }

    @Override
    public User removeFriendById(int userId, int friendId) {
        if (getUserById(userId) == null) {
            throw new UserIsNullException("User " + userId + " is null");
        }
        String sqlQuery = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return getUserById(friendId);
    }

    @Override
    public List<User> getListOfFriends(int userId) {
        if (getUserById(userId) == null) {
            throw new UserIsNullException("User " + userId + " is null");
        }
        List<User> result = new ArrayList<>();
        String sqlQuery = "SELECT friend_id FROM friendships WHERE user_id = ?";
        jdbcTemplate.query(sqlQuery, rs -> {
            result.add(getUserById(rs.getInt("friend_id")));
        }, userId);
        return result;
    }


    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    }
}
