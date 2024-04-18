package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Set<Integer> likes = new HashSet<>();
    private Integer id;
    @NotNull
    @NotBlank(message = "Name must not be blank")
    private String name;
    @Size(max = 200, message = "Max size of description is 200")
    private String description;
    private LocalDate releaseDate;
    @Min(value = 1, message = "Duration below zero")
    private int duration;
}
