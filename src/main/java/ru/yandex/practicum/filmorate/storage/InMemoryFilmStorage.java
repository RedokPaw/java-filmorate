package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Integer id = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Фильм с id " + film.getId() + " создан!");
        return film;
    }

    @Override
    public Film deleteFilmById(int id) {
        log.info("Фильм с id " + id + " удалён!");
        return films.remove(id);
    }

    @Override
    public Film getFilmById(int id) {
        log.info("Фильм с id " + id + " получен!");
        return films.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Фильм с id " + film.getId() + " обновлен!");
        films.replace(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получен список фильмов!");
        return new ArrayList<>(films.values());
    }

    private Integer generateId() {
        return ++this.id;
    }

}
