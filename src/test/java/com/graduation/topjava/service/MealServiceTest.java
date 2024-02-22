package com.graduation.topjava.service;

import com.graduation.topjava.MealTestData;
import com.graduation.topjava.model.Meal;
import com.graduation.topjava.util.exception.ExistException;
import com.graduation.topjava.util.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.graduation.topjava.MealTestData.*;
import static com.graduation.topjava.RestaurantTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MealServiceTest extends AbstractServiceTest {

    @Autowired
    protected MealService service;

    @Test
    void getAll() {
        MEAL_MATCHER.assertMatch(service.getAll(ITALIAN_ID), italian_meals);
    }

    @Test
    void get() {
        Meal actual = service.get(asian_meal1.id(), ASIAN_ID);
        MEAL_MATCHER.assertMatch(actual, asian_meal1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, ITALIAN_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(asian_meal1.id(), ITALIAN_ID));
    }

    @Test
    void create() {
        Meal created = service.save(MealTestData.getNew(), FRENCH_ID);
        int newId = created.id();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, FRENCH_ID), newMeal);
    }

    @Test
    void update() {
        Meal updated = MealTestData.getUpdated();
        service.save(updated, ITALIAN_ID);
        MEAL_MATCHER.assertMatch(service.get(italian_meal1.id(), ITALIAN_ID), MealTestData.getUpdated());
    }

    @Test
    void updateNotOwn() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.save(MealTestData.getUpdated(), ASIAN_ID));
        Assertions.assertEquals("Not found entity with id=" + italian_meal1.id(), exception.getMessage());
        MEAL_MATCHER.assertMatch(service.get(italian_meal1.id(), ITALIAN_ID), italian_meal1);
    }

    @Test
    void delete() {
        service.delete(asian_meal1.id(), ASIAN_ID);
        assertThrows(NotFoundException.class, () -> service.get(asian_meal1.id(), ASIAN_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, ASIAN_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(italian_meal1.id(), FRENCH_ID));
    }

    @Test
    void addDuplicateMealIntoRestaurantForToday() {
        assertThrows(ExistException.class, () -> service.save(asian_meal1, ASIAN_ID));
    }
}
