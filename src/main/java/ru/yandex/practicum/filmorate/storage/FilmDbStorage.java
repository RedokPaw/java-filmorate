package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmMpa;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("film")
                .usingGeneratedKeyColumns("id");
        Integer newId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(newId);
        return film;
    }

    @Override
    public Film deleteFilmById(int id) {
        String sqlQuery = "DELETE FROM film WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery, id) == 0) {
            return null;
        }
        return getFilmById(id);
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "SELECT * FROM film WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE FILM SET MPA_ID=?, DESCRIPTION=?, " +
                "RELEASE_DATE=?, DURATION=?, NAME=? WHERE ID=?;";
        jdbcTemplate.update(sqlQuery, film.getMpa().getId(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getName(), film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM film";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public List<FilmMpa> getAllMpas() {
        String sqlQuery = "SELECT * FROM mpa;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmMpa);
    }

    @Override
    public FilmMpa getMpaById(int id) {
        String sqlQuery = "SELECT * FROM mpa WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilmMpa, id);
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "SELECT * FROM genre WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genre;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Film addLikeToFilm(int userId, int filmId) {
        String sqlQuery = "INSERT INTO film_likes (USER_ID, FILM_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
        return getFilmById(filmId);
    }

    @Override
    public Film removeLikeFromFilm(int userId, int filmId) {
        String sqlQuery = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return getFilmById(filmId);
    }

    @Override
    public Set<Integer> getAllFilmLikes(int filmId) {
        Set<Integer> result = new HashSet<>();
        String sqlQuery = "SELECT DISTINCT user_id FROM film_likes WHERE FILM_ID = ?";
        jdbcTemplate.query(sqlQuery, rs -> {
            result.add(rs.getInt("user_id"));
        }, filmId);
        return result;
    }

    @Override
    public boolean putGenreIdAndFilmId(int filmId, int genreId) {
        String sqlQuery = "INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?)";
        return jdbcTemplate.update(sqlQuery, filmId, genreId) > 0;
    }

    @Override
    public Set<Integer> getAllGenresByFilmId(int filmId) {
        Set<Integer> result = new HashSet<>();
        String sqlQuery = "SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID = ?;";
        jdbcTemplate.query(sqlQuery, rs -> {
            result.add(rs.getInt("genre_id"));
        }, filmId);
        return result;
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setMpa(getMpaById(rs.getInt("mpa_id")));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        return film;
    }

    private FilmMpa mapRowToFilmMpa(ResultSet rs, int rowNum) throws SQLException {
        FilmMpa filmMpa = new FilmMpa();
        filmMpa.setId(rs.getInt("id"));
        filmMpa.setName(rs.getString("name"));
        return filmMpa;
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}
