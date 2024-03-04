DELETE FROM user_role;
DELETE FROM voice;
DELETE FROM meal;
DELETE FROM users;
DELETE FROM restaurant;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin'),
       ('User_1', 'user_1@gmail.com', 'password_1'),
       ('User_2', 'user_2@gmail.com', 'password_2'),
       ('User_3', 'user_3@gmail.com', 'password_3'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('ADMIN', 100000),
       ('USER', 100000),
       ('USER', 100001),
       ('USER', 100002),
       ('USER', 100003);

INSERT INTO restaurant (name)
VALUES ('Italian'),
       ('Asian'),
       ('French');

INSERT INTO meal (restaurant_id, date, name, price)
VALUES (100005, CURRENT_DATE, 'Steak Philadelphia', 1600),
       (100005, CURRENT_DATE, 'Margherita Pizza', 300),
       (100005, CURRENT_DATE, 'Pasta', 700),
       (100006, CURRENT_DATE, 'Paella', 300),
       (100006, CURRENT_DATE, 'Shawarma', 200),
       (100006, CURRENT_DATE, 'Pad Thai', 250),
       (100006, CURRENT_DATE, 'Tandoori Chicken', 440),
       (100007, CURRENT_DATE, 'Ratatouille', 680),
       (100007, CURRENT_DATE, 'Beef Bourguignon', 530);

INSERT INTO voice (user_id, restaurant_id, date, time)
VALUES (100001, 100005, CURRENT_DATE, '10:00:00'),
       (100002, 100005, CURRENT_DATE, '10:50:00'),
       (100003, 100006, CURRENT_DATE, '11:01:00');
