package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id;
    @NotBlank(message = "Name must not be blank")
    private String name;
    @Size(max = 200, message = "Max size of description is 200")
    private String description;
    private LocalDate releaseDate;
    @Min(value = 1, message = "Duration below zero")
    private int duration;
}
