package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.FilmMpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final FilmService filmService;

    @Autowired
    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<FilmMpa> getAllMpas() {
        log.info("Получен реквест на получение всех рейтингов");
        return filmService.getAllMpas();
    }

    @GetMapping("/{id}")
    public FilmMpa getMpaById(@PathVariable("id") Integer mpaId) {
        log.info("Получен реквест на получение рейтинга с id: " + mpaId);
        return filmService.getMpaById(mpaId);
    }
}
