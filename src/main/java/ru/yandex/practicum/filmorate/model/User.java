package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @ToString.Exclude
    private Set<Integer> friends = new HashSet<>();
    private Integer id;
    @NotNull
    @Email(message = "Please provide a valid email address")
    private String email;
    private String name;
    @NotBlank(message = "Login must not be blank")
    private String login;
    @PastOrPresent(message = "birthday can't be in the future")
    private LocalDate birthday;
}
