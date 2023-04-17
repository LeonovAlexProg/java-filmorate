DROP TABLE IF EXISTS FILM_GENRES;
DROP TABLE IF EXISTS FILM_LIKES;
DROP TABLE IF EXISTS FILMS;
DROP TABLE IF EXISTS GENRES;
DROP TABLE IF EXISTS MPA;
DROP TABLE IF EXISTS USER_FRIENDS;
DROP TABLE IF EXISTS USERS;

CREATE TABLE IF NOT EXISTS PUBLIC.MPA (
                                          MPA_ID INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                          MPA_NAME VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILMS (
                                            FILM_ID INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                            NAME VARCHAR(50) NOT NULL,
                                            MPA_ID INT NOT NULL,
                                            DESCRIPTION VARCHAR(255) NOT NULL,
                                            RELEASE_DATE DATE NOT NULL,
                                            DURATION BIGINT NOT NULL,
                                            FOREIGN KEY (MPA_ID) REFERENCES PUBLIC.MPA(MPA_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.GENRES (
                                             GENRE_ID INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                             GENRE_NAME VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_GENRES (
                                                  FILM_ID INT NOT NULL,
                                                  GENRE_ID INT NOT NULL,
                                                  PRIMARY KEY (FILM_ID, GENRE_ID),
                                                  FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID),
                                                  FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRES(GENRE_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
                                            USER_ID INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                            EMAIL VARCHAR(50) NOT NULL UNIQUE,
                                            LOGIN VARCHAR(50) NOT NULL UNIQUE,
                                            NAME VARCHAR(50),
                                            BIRTHDAY DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_LIKES (
                                                 FILM_ID INT,
                                                 USER_ID INT,
                                                 PRIMARY KEY (FILM_ID, USER_ID),
                                                 FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID),
                                                 FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.USER_FRIENDS (
                                                   USER_ID INT NOT NULL,
                                                   FRIEND_ID INT NOT NULL,
                                                   APPLIED BOOLEAN NOT NULL,
                                                   PRIMARY KEY (USER_ID, FRIEND_ID, APPLIED),
                                                   FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID),
                                                   FOREIGN KEY (FRIEND_ID) REFERENCES PUBLIC.USERS(USER_ID)
);
