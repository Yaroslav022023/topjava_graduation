package com.graduation.topjava.web.meal;

import com.graduation.topjava.model.Meal;
import com.graduation.topjava.service.MealService;
import com.graduation.topjava.util.exception.NotFoundException;
import com.graduation.topjava.web.AbstractControllerTest;
import com.graduation.topjava.web.json.JsonUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.graduation.topjava.MealTestData.*;
import static com.graduation.topjava.RestaurantTestData.ITALIAN_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminMealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL =
            AdminMealRestController.REST_URL.replace("{restaurantId}", Integer.toString(ITALIAN_ID)) + '/';

    @Autowired
    private MealService service;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
//                .with(userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(italian_meals));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + italian_meal1.id()))
//                .with(userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(italian_meal1));
    }

    @Disabled
    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + italian_meal1.id()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
//                .with(userHttpBasic(UserTestData.user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, ITALIAN_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + italian_meal1.id())
                .contentType(MediaType.APPLICATION_JSON)
//                .with(userHttpBasic(UserTestData.user))
                .content(JsonUtil.writeValue(getUpdated())))
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(service.get(italian_meal1.id(), ITALIAN_ID), getUpdated());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + italian_meal1.id()))
//                .with(userHttpBasic(UserTestData.user)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(italian_meal1.id(), ITALIAN_ID));
    }
}
