DROP TABLE user_role IF EXISTS;
DROP TABLE voice IF EXISTS;
DROP TABLE meal IF EXISTS;
DROP TABLE users IF EXISTS;
DROP TABLE restaurant IF EXISTS;
DROP SEQUENCE global_seq IF EXISTS;

CREATE SEQUENCE GLOBAL_SEQ AS INTEGER START WITH 100000;

CREATE TABLE users
(
    id               INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name             VARCHAR(255)            NOT NULL,
    email            VARCHAR(255)            NOT NULL,
    password         VARCHAR(255)            NOT NULL,
    registered       TIMESTAMP DEFAULT now() NOT NULL,
    enabled          BOOLEAN   DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON USERS (email);

CREATE TABLE user_role
(
    user_id    INTEGER NOT NULL,
    role       VARCHAR(5) NOT NULL,
    CONSTRAINT user_id_role_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id          INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name        VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX restaurant_unique_name_idx ON restaurant (name);

CREATE TABLE meal
(
    id                INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    restaurant_id     INTEGER      NOT NULL,
    date              DATE         NOT NULL,
    name              VARCHAR(255) NOT NULL,
    price             INt          NOT NULL,
    CONSTRAINT meal_restaurant_id_date_name_idx UNIQUE (restaurant_id, date, name),
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANT (id) ON DELETE CASCADE
);
CREATE INDEX meal_restaurant_datetime_idx ON meal (restaurant_id, date)

CREATE TABLE voice
(
    id                INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    user_id           INTEGER      NOT NULL,
    restaurant_id     INTEGER      NOT NULL,
    date              DATE         NOT NULL,
    time              TIME         NOT NULL,
    CONSTRAINT user_id_date_idx UNIQUE (user_id, date),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANT (id) ON DELETE CASCADE
);
CREATE INDEX idx_voice_date ON voice (date);