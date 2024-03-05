package com.topjava.graduation.service;

import com.topjava.graduation.MealTestData;
import com.topjava.graduation.model.Meal;
import com.topjava.graduation.util.exception.NotFoundException;
import com.topjava.graduation.RestaurantTestData;
import org.hsqldb.HsqlException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static com.topjava.graduation.TestUtil.validateRootCause;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MealServiceTest extends AbstractServiceTest {

    @Autowired
    protected MealService service;

    @Test
    void getAll() {
        MealTestData.MEAL_MATCHER.assertMatch(service.getAll(RestaurantTestData.ITALIAN_ID), MealTestData.italian_meals);
    }

    @Test
    void get() {
        Meal actual = service.get(MealTestData.asian_meal1.id(), RestaurantTestData.ASIAN_ID);
        MealTestData.MEAL_MATCHER.assertMatch(actual, MealTestData.asian_meal1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.NOT_FOUND, RestaurantTestData.ITALIAN_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.asian_meal1.id(), RestaurantTestData.ITALIAN_ID));
    }

    @Test
    void create() {
        Meal created = service.save(MealTestData.getNew(), RestaurantTestData.FRENCH_ID);
        int newId = created.id();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        MealTestData.MEAL_MATCHER.assertMatch(created, newMeal);
        MealTestData.MEAL_MATCHER.assertMatch(service.get(newId, RestaurantTestData.FRENCH_ID), newMeal);
    }

    @Test
    void update() {
        Meal updated = MealTestData.getUpdated();
        service.save(updated, RestaurantTestData.ITALIAN_ID);
        MealTestData.MEAL_MATCHER.assertMatch(service.get(MealTestData.italian_meal1.id(), RestaurantTestData.ITALIAN_ID), MealTestData.getUpdated());
    }

    @Test
    void updateNotOwn() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.save(MealTestData.getUpdated(), RestaurantTestData.ASIAN_ID));
        Assertions.assertEquals("Not found entity with id=" + MealTestData.italian_meal1.id(), exception.getMessage());
        MealTestData.MEAL_MATCHER.assertMatch(service.get(MealTestData.italian_meal1.id(), RestaurantTestData.ITALIAN_ID), MealTestData.italian_meal1);
    }

    @Test
    void updateToDuplicate() {
        Meal updated = service.get(MealTestData.asian_meal1.id(), RestaurantTestData.ASIAN_ID);
        updated.setName(MealTestData.asian_meal2.getName());
        validateRootCause(HsqlException.class, () -> service.save(updated, RestaurantTestData.ASIAN_ID));
    }

    @Test
    void delete() {
        service.delete(MealTestData.asian_meal1.id(), RestaurantTestData.ASIAN_ID);
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.asian_meal1.id(), RestaurantTestData.ASIAN_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.NOT_FOUND, RestaurantTestData.ASIAN_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.italian_meal1.id(), RestaurantTestData.FRENCH_ID));
    }

    @Test
    void addDuplicateMealIntoRestaurantForToday() {
        assertThrows(DataIntegrityViolationException.class, () ->
                service.save(new Meal(
                                null,
                                MealTestData.asian_meal1.getDate(),
                                MealTestData.asian_meal1.getName(),
                                MealTestData.asian_meal1.getPrice()),
                        RestaurantTestData.ASIAN_ID));
    }
}
