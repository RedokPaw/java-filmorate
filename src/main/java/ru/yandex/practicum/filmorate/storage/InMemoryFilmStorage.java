package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.CreationException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
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
        if (films.containsKey(film.getId())) {
            log.info("Фильм с id " + film.getId() + " уже существует");
            throw new CreationException("Ошибка добавления существующего фильма с id: " + film.getId());
        }
        if (!isReleaseDateCorrect(film.getReleaseDate())) {
            throw new ValidateException("Дата релиза не может быть раньше 28 декабря 1895 года! " +
                    "(film id: " + film.getId() + ")");
        }
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Фильм с id " + film.getId() + " создан!");
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.info("Фильм с id " + film.getId() + " удалён!");
            return films.remove(film.getId());
        }
        log.info("Фильм с id " + film.getId() + " не был найден!");
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.info("Фильм с id " + film.getId() + " обновлен!");
            return film;
        }
        log.info("Ошибка при обновлении фильма с id " + film.getId() + ": нет такого фильма в списке");
        throw new UpdateException("Ошибка при обновлении фильма с id" + film.getId());
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private Integer generateId() {
        return ++this.id;
    }

    private boolean isReleaseDateCorrect(LocalDate releaseDate) {
        return !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }
}
