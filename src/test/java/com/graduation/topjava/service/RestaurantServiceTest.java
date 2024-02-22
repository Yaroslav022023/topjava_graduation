package com.graduation.topjava.service;

import com.graduation.topjava.model.Restaurant;
import com.graduation.topjava.util.RestaurantUtil;
import com.graduation.topjava.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.graduation.topjava.MealTestData.NOT_FOUND;
import static com.graduation.topjava.RestaurantTestData.*;
import static com.graduation.topjava.UserTestData.GUEST_ID;
import static com.graduation.topjava.UserTestData.USER_1_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    protected RestaurantService service;

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(service.getAll(), restaurants);
    }

    @Test
    void get() {
        RESTAURANT_MATCHER.assertMatch(service.get(ITALIAN_ID), italian);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void create() {
        Restaurant created = service.save(getNew());
        int newId = created.id();
        Restaurant newRestaurant = getNew();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    void update() {
        Restaurant updated = getUpdated();
        service.save(updated);
        RESTAURANT_MATCHER.assertMatch(service.get(ITALIAN_ID), getUpdated());
    }

    @Test
    void updateNotFound() {
        Restaurant notFound = new Restaurant(NOT_FOUND, "Not Exist");
        assertThrows(NotFoundException.class, () -> service.save(notFound));
    }

    @Test
    void delete() {
        service.delete(ASIAN_ID);
        assertThrows(NotFoundException.class, () -> service.get(ASIAN_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void getAllWithMealsForToday() {
        RESTAURANT_VIEW_DTO_MATCHER.assertMatch(service.getAllWithMealsForToday(),
                RestaurantUtil.convertToViewDtos(restaurants));
    }

    @Test
    void getVotedByUser() {
        RESTAURANT_VOTED_BY_USER_DTO_MATCHER.assertMatch(
                service.getVotedByUser(USER_1_ID), RestaurantUtil.convertToVotedByUserDto(italian));
    }

    @Test
    void getVotedByUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.getVotedByUser(GUEST_ID));
    }
}
