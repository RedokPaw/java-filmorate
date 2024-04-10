package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null || user == null) {
            log.info("add like to film error: user or film is not found! User= " + user + ", film = " + film);
            throw new ElementIsNullException("Film or user is not found! Check User and Film id");
        }
        if (!film.getLikes().add(user.getId())) {
            log.info("Film already has like");
        }
        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null || user == null) {
            log.info("add like to film error: user or film is not found!");
            throw new ElementIsNullException("Film or user is null");
        }
        if (!film.getLikes().remove(user.getId())) {
            log.info("Like is not found");
        }
        return film;
    }

    public List<Film> getMostLikedFilms(int maxSize) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(o -> ((Film) o).getLikes().size()).reversed())
                .limit(maxSize)
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film) {
        if (!isReleaseDateCorrect(film.getReleaseDate())) {
            throw new ValidateException("Дата релиза не может быть раньше 28 декабря 1895 года! " +
                    "(film id: " + film.getId() + ")");
        }
        if (film.getId() == null) {
            return filmStorage.addFilm(film);
        }
        if (!(filmStorage.getFilmById(film.getId()) == null)) {
            log.info("Фильм с id " + film.getId() + " уже существует");
            throw new CreationException("Ошибка добавления существующего фильма с id: " + film.getId());
        }
        return filmStorage.addFilm(film);
    }

    public Film deleteFilm(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            log.info("Фильм с id " + film.getId() + " не был найден!");
            throw new DeleteException("Ошибка удаления фильма с id: " + film.getId() + " фильм не был найден!");
        }
        return filmStorage.deleteFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Film updateFilm(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            log.info("Ошибка при обновлении фильма с id " + film.getId() + ": нет такого фильма в списке");
            throw new UpdateException("Ошибка при обновлении фильма с id" + film.getId());
        }
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    private boolean isReleaseDateCorrect(LocalDate releaseDate) {
        return !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }
}

