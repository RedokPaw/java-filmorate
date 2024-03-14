package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private Integer id;
    @Email(message = "The string has to be a well-formed email address.")
    private String email;
    private String name;
    @NotBlank(message = "Login must not be blank")
    private String login;
    @PastOrPresent(message = "birthday can't be in the future")
    private LocalDate birthday;
}
