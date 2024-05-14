create table IF NOT EXISTS GENRE
(
    ID   INTEGER auto_increment,
    NAME VARCHAR(255),
    constraint GENRE_PK
        primary key (ID)
);

create table IF NOT EXISTS MPA
(
    ID   INTEGER auto_increment,
    NAME VARCHAR(255),
    constraint MPA_PK
        primary key (ID)
);

create table IF NOT EXISTS FILM
(
    ID           INTEGER auto_increment,
    MPA_ID       INTEGER,
    DESCRIPTION  VARCHAR(2000),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    NAME         CHARACTER VARYING not null,
    constraint FILM_PK
        primary key (ID),
    constraint FILM_MPA_FK
        foreign key (MPA_ID) references MPA
            on update cascade on delete cascade
);

create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint FILM_GENRE_FILM_FK
        foreign key (FILM_ID) references FILM
            on update cascade on delete cascade,
    constraint FILM_GENRE_GENRE_FK
        foreign key (GENRE_ID) references GENRE
            on update cascade on delete cascade
);

create table IF NOT EXISTS USER
(
    ID       INTEGER auto_increment,
    BIRTHDAY DATE,
    NAME     VARCHAR(255),
    LOGIN    VARCHAR(128) not null,
    EMAIL    VARCHAR(128) not null,
    constraint USER_PK
        primary key (ID)
);

create table IF NOT EXISTS FILM_LIKES
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint FILM_LIKES_FILM_FK
        foreign key (FILM_ID) references FILM
            on update cascade on delete cascade,
    constraint FILM_LIKES_USER_FK
        foreign key (USER_ID) references USER
            on update cascade on delete cascade
);

create table IF NOT EXISTS FRIENDSHIPS
(
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    constraint FRIENDSHIPS_USER_FK
        foreign key (USER_ID) references USER
            on update cascade on delete cascade,
    constraint FRIENDSHIPS_USER_FK_1
        foreign key (FRIEND_ID) references USER
            on update cascade on delete cascade
);

