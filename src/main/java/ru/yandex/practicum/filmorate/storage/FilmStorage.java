package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmMpa;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film addFilm(Film film);

    Film deleteFilmById(int id);

    Film getFilmById(int id);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    List<FilmMpa> getAllMpas();

    FilmMpa getMpaById(int id);

    Genre getGenreById(int id);

    List<Genre> getAllGenres();

    Film addLikeToFilm(int userId, int filmId);

    Film removeLikeFromFilm(int userId, int filmId);

    Set<Integer> getAllFilmLikes(int filmId);

    boolean putGenreIdAndFilmId(int filmId, int genreId);

    Set<Genre> getAllGenresByFilmId(int filmId);

}
