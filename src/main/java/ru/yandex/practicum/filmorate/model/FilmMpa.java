package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FilmMpa {
    @NotNull
    private int id;
    private String name;
}
