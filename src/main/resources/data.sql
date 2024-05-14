DELETE FROM "USER";
ALTER TABLE "USER" ALTER COLUMN ID RESTART WITH 1;
DELETE FROM "FILM";
ALTER TABLE "FILM" ALTER COLUMN ID RESTART WITH 1;

DELETE FROM "FRIENDSHIPS";
DELETE FROM "FILM_LIKES";

DELETE FROM "GENRE";
INSERT INTO GENRE (id, name)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');


DELETE FROM "MPA";
INSERT INTO  MPA (id, name)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');