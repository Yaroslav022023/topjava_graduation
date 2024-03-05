package com.topjava.graduation.web.meal;

import com.topjava.graduation.model.Meal;
import com.topjava.graduation.service.MealService;
import com.topjava.graduation.util.exception.NotFoundException;
import com.topjava.graduation.web.AbstractControllerTest;
import com.topjava.graduation.MealTestData;
import com.topjava.graduation.RestaurantTestData;
import com.topjava.graduation.UserTestData;
import com.topjava.graduation.web.json.TestJsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.topjava.graduation.TestUtil.userHttpBasic;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminMealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL =
            AdminMealRestController.REST_URL.replace("{restaurantId}", Integer.toString(RestaurantTestData.ITALIAN_ID)) + '/';

    @Autowired
    private MealService service;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MEAL_MATCHER.contentJson(MealTestData.italian_meals));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.italian_meal1.id())
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MEAL_MATCHER.contentJson(MealTestData.italian_meal1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.NOT_FOUND)
                .with(userHttpBasic(UserTestData.admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.italian_meal1.id()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestJsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = MealTestData.MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MealTestData.MEAL_MATCHER.assertMatch(created, newMeal);
        MealTestData.MEAL_MATCHER.assertMatch(service.get(newId, RestaurantTestData.ITALIAN_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MealTestData.italian_meal1.id())
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(UserTestData.admin))
                .content(TestJsonUtil.writeValue(MealTestData.getUpdated())))
                .andExpect(status().isNoContent());
        MealTestData.MEAL_MATCHER.assertMatch(service.get(MealTestData.italian_meal1.id(), RestaurantTestData.ITALIAN_ID), MealTestData.getUpdated());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MealTestData.italian_meal1.id())
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.italian_meal1.id(), RestaurantTestData.ITALIAN_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MealTestData.NOT_FOUND)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isUnprocessableEntity());
    }
}
