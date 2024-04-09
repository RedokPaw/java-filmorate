package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ElementIsNullException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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
        return filmStorage.addFilm(film);
    }

    public Film deleteFilm(Film film) {
        return filmStorage.deleteFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

}
