package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmMpa;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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

    public Film addLike(int userId, int filmId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null || user == null) {
            log.info("add like to film error: user or film is not found! User= " + user + ", film = " + film);
            throw new ElementIsNullException("Film or user is not found! Check User and Film id");
        }
        return filmStorage.addLikeToFilm(userId, filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null || user == null) {
            log.info("add like to film error: user or film is not found!");
            throw new ElementIsNullException("Film or user is null");
        }
        return filmStorage.removeLikeFromFilm(userId, filmId);
    }

    public List<Film> getMostLikedFilms(int maxSize) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(o -> (filmStorage.getAllFilmLikes(((Film) o).getId()).size())).reversed())
                .limit(maxSize)
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film) {
        if (!isReleaseDateCorrect(film.getReleaseDate())) {
            throw new ValidateException("Дата релиза не может быть раньше 28 декабря 1895 года! " +
                    "(film id: " + film.getId() + ")");
        }
        if (film.getId() == null) {
            genreAndMpaValidation(film);
            Integer newId = filmStorage.addFilm(film).getId();
            film.setId(newId);
            addFilmGenresToDb(film);
            return film;
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
        return filmStorage.deleteFilmById(film.getId());
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.getFilmById(id);
        genreAndMpaValidation(film);
        film.getGenres().clear();
        film.setGenres(getAllGenresByFilmId(id));
        return film;
    }

    public Film updateFilm(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            log.info("Ошибка при обновлении фильма с id " + film.getId() + ": нет такого фильма в списке");
            throw new UpdateException("Ошибка при обновлении фильма с id" + film.getId());
        }
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return filmStorage.getGenreById(id);
    }

    public Set<Genre> getAllGenresByFilmId(int filmId) {
        return filmStorage.getAllGenresByFilmId(filmId);
    }

    public FilmMpa getMpaById(int id) {
        return filmStorage.getMpaById(id);
    }

    public List<FilmMpa> getAllMpas() {
        return filmStorage.getAllMpas();
    }

    private boolean isReleaseDateCorrect(LocalDate releaseDate) {
        return !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }

    private void addFilmGenresToDb(Film film) {
        for (Genre genre : film.getGenres()) {
            filmStorage.putGenreIdAndFilmId(film.getId(), genre.getId());
        }
    }

    private void genreAndMpaValidation(Film film) {
        if (film.getMpa() != null) {
            FilmMpa mpa = filmStorage.getMpaById(film.getMpa().getId());
            if (mpa == null) {
                throw new ValidateException("Mpa with id " + film.getMpa().getId() + " does not exists");
            }
            film.setMpa(mpa);
        }
        if (film.getGenres() != null) {
            try {
                Set<Genre> genres = getAllGenres().stream()
                        .peek(genre -> genre.setName(null))
                        .collect(Collectors.toSet());
                for (Genre genre : film.getGenres()) {
                    if (!genres.contains(genre)) {
                        throw new ValidateException("Genre with id " + genre.getId() + " does not exists");
                    }
                }
            } catch (EmptyResultDataAccessException e) {
                throw new ValidateException("Genre validate exception, check if genre exists");
            }
        }
    }
}

