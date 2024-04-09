package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping(value = "/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Реквест на получение всех фильмов ");
        return filmService.getFilms();
    }

    @PostMapping
    public Film createNewFilm(@Validated @RequestBody Film film, BindingResult errors) {
        log.info("Реквест на создание фильма c id: " + film.getId());
        if (errors.hasErrors()) {
            throw new ValidationException("Ошибка валидации входных данных!");
        }
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult errors) {
        log.info("Реквест на обновление фильма c id: " + film.getId());
        if (errors.hasErrors()) {
            log.info("Ошибка валидации входных данных!");
            throw new ValidationException("Ошибка валидации входных данных!");
        }
        return filmService.updateFilm(film);
    }

    @PutMapping(value = "/{id}")
    public Film getFilm(@PathVariable Integer id) {
        log.info("Реквест на получение фильма с id: " + id);
        return filmService.getFilmById(id);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public Film addLike(@PathVariable(value = "id") int filmId, @PathVariable int userId) {
        log.info("Реквест на добавление лайка фильму с id: " + filmId + " от пользователя с id: " + userId);
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public Film deleteLike(@PathVariable(value = "id") int filmId, @PathVariable int userId) {
        log.info("Реквест на удаление лайка фильму с id: " + filmId + " от пользователя с id: " + userId);
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getMostLikedFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Реквест на получение наиболее популярных фильмов с макс количеством: " + count);
        return filmService.getMostLikedFilms(count);
    }
}
