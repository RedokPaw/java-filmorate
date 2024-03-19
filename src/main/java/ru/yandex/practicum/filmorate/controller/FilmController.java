package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.CreationException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Slf4j
public class FilmController {
    private Integer id = 0;
    private HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping(value = "/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film createNewFilm(@Valid @RequestBody Film film) {
        log.info("Реквест на создание фильма c id: " + film.getId());
        if (films.containsKey(film.getId())) {
            log.info("Фильм с id " + film.getId() + " уже существует");
            throw new CreationException("Ошибка добавления существующего фильма с id: " + film.getId());
        }
        if (!isReleaseDateCorrect(film.getReleaseDate())) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года! " +
                    "(film id: " + film.getId() + ")");
        }
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Фильм с id " + film.getId() + " создан!");
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Реквест на обновление фильма c id: " + film.getId());
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.info("Фильм с id " + film.getId() + " обновлен!");
            return film;
        }
        log.info("Ошибка при обновлении фильма с id " + film.getId() + ": нет такого фильма в списке");
        throw new NoSuchElementException();
    }

    private boolean isReleaseDateCorrect(LocalDate releaseDate) {
        return !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }

    private Integer generateId() {
        return ++this.id;
    }
}
