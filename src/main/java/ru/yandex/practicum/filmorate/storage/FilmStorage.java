package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film deleteFilm(Film film);

    Film getFilmById(int id);

    Film updateFilm(Film film);

    List<Film> getFilms();
}
