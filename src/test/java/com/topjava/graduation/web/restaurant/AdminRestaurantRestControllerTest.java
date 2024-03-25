package com.topjava.graduation.web.restaurant;

import com.topjava.graduation.MealTestData;
import com.topjava.graduation.UserTestData;
import com.topjava.graduation.model.Restaurant;
import com.topjava.graduation.service.RestaurantService;
import com.topjava.graduation.util.exception.NotFoundException;
import com.topjava.graduation.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.topjava.graduation.MealTestData.NOT_FOUND;
import static com.topjava.graduation.RestaurantTestData.*;
import static com.topjava.graduation.TestUtil.userHttpBasic;
import static com.topjava.graduation.UserTestData.admin;
import static com.topjava.graduation.util.RestaurantUtil.convertToViewDtos;
import static com.topjava.graduation.util.exception.ErrorType.DATA_NOT_FOUND;
import static com.topjava.graduation.util.exception.ErrorType.VALIDATION_ERROR;
import static com.topjava.graduation.web.json.TestJsonUtil.writeValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminRestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestaurantRestController.REST_URL + '/';
    @Autowired
    private RestaurantService service;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "all")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurants));
    }

    @Test
    void getAllWithMealsForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_VIEW_DTO_MATCHER.contentJson(convertToViewDtos(restaurants)));
    }

    @Test
    void getAllWithNumberVoicesForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/number-voices")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_WITH_NUMBER_VOICES_DTO_MATCHER.contentJson(getWithNumberVoicesDtos()));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + italian.id())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(italian));
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
        perform(MockMvcRequestBuilders.get(REST_URL + "all"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void getVotedByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/voted-by-user/" + UserTestData.user_1.getId())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(italian));
    }

    @Test
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newRestaurant)))
                .andExpect(status().isCreated())
                .andDo(print());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    void createInvalidName() throws Exception {
        Restaurant newRestaurant = getNew();
        newRestaurant.setName("");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newRestaurant)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(2, "[name] must not be blank", "[name] size must be between 2 and 255"));
    }

    @Test
    void createHtmlUnsafe() throws Exception {
        Restaurant newRestaurant = getNew();
        newRestaurant.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newRestaurant)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[name] Unsafe html content"));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void createDuplicateDateAndName() throws Exception {
        Restaurant newRestaurant = getNew();
        newRestaurant.setName(italian.getName());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newRestaurant)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "A restaurant with that name already exists"));
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + italian.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getUpdated())))
                .andExpect(status().isNoContent())
                .andDo(print());
        RESTAURANT_MATCHER.assertMatch(service.get(italian.id()), getUpdated());
    }

    @Test
    void updateInvalidName() throws Exception {
        Restaurant updated = getUpdated();
        updated.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL + italian.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(2, "[name] must not be blank", "[name] size must be between 2 and 255"));
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        Restaurant updated = getUpdated();
        updated.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL + italian.id())
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
        Restaurant updated = new Restaurant(null, italian.getName());
        perform(MockMvcRequestBuilders.put(REST_URL + asian.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "A restaurant with that name already exists"));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + italian.id())
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertThrows(NotFoundException.class, () -> service.get(italian.id()));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MealTestData.NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(DATA_NOT_FOUND))
                .andExpect(detailMessages(0));
    }
}
