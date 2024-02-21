DELETE FROM user_role;
DELETE FROM voice;
DELETE FROM meal;
DELETE FROM users;
DELETE FROM restaurant;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin'),
       ('User1', 'user1@yandex.ru', 'password1'),
       ('User2', 'user2@yandex.ru', 'password2'),
       ('User3', 'use3r@yandex.ru', 'password3'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('ADMIN', 100000),
       ('USER', 100001),
       ('USER', 100002),
       ('USER', 100003);

INSERT INTO restaurant (name)
VALUES ('Italian'),
       ('Asian'),
       ('French');

INSERT INTO meal (restaurant_id, date, name, price)
VALUES (100005, '2024-01-15', 'Steak Philadelphia', 1600),
       (100005, '2024-01-15', 'Margherita Pizza', 300),
       (100005, '2024-01-15', 'Sushi', 700),
       (100006, '2024-01-15', 'Paella', 300),
       (100006, '2024-01-15', 'Shawarma', 200),
       (100006, '2024-01-15', 'Pad Thai', 250),
       (100006, '2024-01-15', 'Tandoori Chicken', 440),
       (100007, '2024-01-15', 'Ratatouille', 680),
       (100007, '2024-01-15', 'Beef Bourguignon', 530);

INSERT INTO voice (user_id, restaurant_id, date, time)
VALUES (100001, 100005, '2024-01-15', '10:00:00'),
       (100002, 100005, '2024-01-15', '10:50:00'),
       (100003, 100006, '2024-01-15', '11:01:00');
