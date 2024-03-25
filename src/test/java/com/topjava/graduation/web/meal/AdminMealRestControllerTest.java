package com.topjava.graduation.web.meal;

import com.topjava.graduation.MealTestData;
import com.topjava.graduation.model.Meal;
import com.topjava.graduation.service.MealService;
import com.topjava.graduation.util.exception.NotFoundException;
import com.topjava.graduation.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.topjava.graduation.MealTestData.*;
import static com.topjava.graduation.RestaurantTestData.ITALIAN_ID;
import static com.topjava.graduation.TestUtil.userHttpBasic;
import static com.topjava.graduation.UserTestData.admin;
import static com.topjava.graduation.util.exception.ErrorType.DATA_NOT_FOUND;
import static com.topjava.graduation.util.exception.ErrorType.VALIDATION_ERROR;
import static com.topjava.graduation.web.json.TestJsonUtil.writeValue;
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
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(italian_meals));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + italian_meal1.id())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(italian_meal1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(DATA_NOT_FOUND))
                .andExpect(detailMessages(0));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + italian_meal1.id()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newMeal)))
                .andExpect(status().isCreated())
                .andDo(print());

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, ITALIAN_ID), newMeal);
    }

    @Test
    void createInvalidName() throws Exception {
        Meal newMeal = getNew();
        newMeal.setName("");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newMeal)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(2, "[name] must not be blank", "[name] size must be between 2 and 255"));
    }

    @Test
    void createInvalidPriceLessThanMin() throws Exception {
        Meal newMeal = getNew();
        newMeal.setPrice(4);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newMeal)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[price] must be between 5 and 2000"));
    }

    @Test
    void createInvalidPriceMoreThanMax() throws Exception {
        Meal newMeal = getNew();
        newMeal.setPrice(2001);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newMeal)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[price] must be between 5 and 2000"));
    }

    @Test
    void createHtmlUnsafe() throws Exception {
        Meal newMeal = getNew();
        newMeal.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newMeal)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[name] Unsafe html content"));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void createDuplicateDateAndName() throws Exception {
        Meal newMeal = getNew();
        newMeal.setDate(italian_meal1.getDate());
        newMeal.setName(italian_meal1.getName());
        newMeal.setPrice(100);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newMeal)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "This restaurant already has food with that name and date"));
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + italian_meal1.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(MealTestData.getUpdated())))
                .andExpect(status().isNoContent())
                .andDo(print());
        MEAL_MATCHER.assertMatch(service.get(italian_meal1.id(), ITALIAN_ID), getUpdated());
    }

    @Test
    void updateInvalidName() throws Exception {
        Meal updated = getUpdated();
        updated.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL + italian_meal1.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(2, "[name] must not be blank", "[name] size must be between 2 and 255"));
    }

    @Test
    void updateInvalidPriceLessThanMin() throws Exception {
        Meal updated = MealTestData.getUpdated();
        updated.setPrice(4);
        perform(MockMvcRequestBuilders.put(REST_URL + italian_meal1.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[price] must be between 5 and 2000"));
    }

    @Test
    void updateInvalidPriceMoreThanMax() throws Exception {
        Meal updated = MealTestData.getUpdated();
        updated.setPrice(2001);
        perform(MockMvcRequestBuilders.put(REST_URL + italian_meal1.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[price] must be between 5 and 2000"));
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        Meal updated = getUpdated();
        updated.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL + italian_meal1.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[name] Unsafe html content"));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void updateDuplicateDateAndName() throws Exception {
        Meal updated = new Meal(null, italian_meal1.getDate(), italian_meal1.getName(), 100);
        perform(MockMvcRequestBuilders.put(REST_URL + italian_meal2.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "This restaurant already has food with that name and date"));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + italian_meal1.id())
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertThrows(NotFoundException.class, () -> service.get(italian_meal1.id(), ITALIAN_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(DATA_NOT_FOUND))
                .andExpect(detailMessages(0));
    }
}
