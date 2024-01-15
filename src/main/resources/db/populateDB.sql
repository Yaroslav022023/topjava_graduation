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

INSERT INTO restaurant (id, name)
VALUES (100100, 'Italian'),
       (100101, 'Asian'),
       (100102, 'French'),
       (100103, 'European');

INSERT INTO meal (restaurant_id, date, name, price)
VALUES (100100, '2024-01-15', 'Steak Philadelphia', 1600),
       (100100, '2024-01-15', 'Margherita Pizza', 300),
       (100100, '2024-01-15', 'Sushi', 700),
       (100101, '2024-01-15', 'Paella', 300),
       (100101, '2024-01-15', 'Shawarma', 200),
       (100101, '2024-01-15', 'Pad Thai', 250),
       (100101, '2024-01-15', 'Tandoori Chicken', 440),
       (100102, '2024-01-15', 'Ratatouille', 680),
       (100102, '2024-01-15', 'Beef Bourguignon', 530),
       (100103, '2024-01-15', 'Feijoada', 365),
       (100103, '2024-01-15', 'Kimchi', 220),
       (100103, '2024-01-15', 'Burger', 880),
       (100103, '2024-01-15', 'Ceviche', 920);

INSERT INTO voice (user_id, restaurant_id, date, time)
VALUES (100001, 100100, '2024-01-15', '10:00:00'),
       (100002, 100100, '2024-01-15', '10:50:00'),
       (100003, 100102, '2024-01-15', '11:01:00');
