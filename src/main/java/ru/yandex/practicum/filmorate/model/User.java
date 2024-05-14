package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class User {
    private Integer id;
    @NotNull
    @Email(message = "Please provide a valid email address")
    private String email;
    private String name;
    @NotBlank(message = "Login must not be blank")
    private String login;
    @PastOrPresent(message = "birthday can't be in the future")
    private LocalDate birthday;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("ID", id);
        map.put("EMAIL", email);
        map.put("NAME", name);
        map.put("LOGIN", login);
        map.put("BIRTHDAY", birthday);
        return map;
    }
}
