package com.graduation.topjava.service;

import com.graduation.topjava.model.User;
import com.graduation.topjava.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.graduation.topjava.MealTestData.NOT_FOUND;
import static com.graduation.topjava.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Test
    void getAll() {
        USER_MATCHER.assertMatch(service.getAll(), users);
    }

    @Test
    void get() {
        USER_MATCHER.assertMatch(service.get(USER_1_ID), user_1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void create() {
        User created = service.save(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    void update() {
        User updated = getUpdated();
        service.save(updated);
        USER_MATCHER.assertMatch(service.get(USER_2_ID), getUpdated());
    }

    @Test
    void updateNotFound() {
        User notFound = getNew();
        notFound.setId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.save(notFound));
    }

    @Test
    void delete() {
        service.delete(USER_1_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void getByEmail() {
        User user = service.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void getByEmailNotFound() {
        assertThrows(NotFoundException.class, () -> service.getByEmail("notFound@gmail.com"));
    }

    @Test
    void enable() {
        service.enable(USER_1_ID, false);
        assertFalse(service.get(USER_1_ID).isEnabled());
        service.enable(USER_1_ID, true);
        assertTrue(service.get(USER_1_ID).isEnabled());
    }
}
