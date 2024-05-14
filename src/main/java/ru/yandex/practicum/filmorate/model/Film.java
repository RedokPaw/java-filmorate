package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class Film {
    private Integer id;
    @NotNull
    @NotBlank(message = "Name must not be blank")
    private String name;
    @Size(max = 200, message = "Max size of description is 200")
    private String description;
    private LocalDate releaseDate;
    @Min(value = 1, message = "Duration below zero")
    private Integer duration;
    private FilmMpa mpa;
    private Set<Genre> genres = new HashSet<>();

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("ID", id);
        map.put("NAME", name);
        map.put("DESCRIPTION", description);
        map.put("RELEASE_DATE", releaseDate);
        map.put("DURATION", duration);
        map.put("MPA_ID", mpa.getId());
        return map;
    }

}
