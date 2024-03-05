package com.topjava.graduation.web.restaurant;

import com.topjava.graduation.model.Restaurant;
import com.topjava.graduation.service.RestaurantService;
import com.topjava.graduation.util.exception.NotFoundException;
import com.topjava.graduation.web.AbstractControllerTest;
import com.topjava.graduation.MealTestData;
import com.topjava.graduation.UserTestData;
import com.topjava.graduation.util.RestaurantUtil;
import com.topjava.graduation.web.json.TestJsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.topjava.graduation.RestaurantTestData.*;
import static com.topjava.graduation.TestUtil.userHttpBasic;
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
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurants));
    }

    @Test
    void getAllWithMealsForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_VIEW_DTO_MATCHER.contentJson(RestaurantUtil.convertToViewDtos(restaurants)));
    }

    @Test
    void getAllWithNumberVoicesForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/number-voices")
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_WITH_NUMBER_VOICES_DTO_MATCHER.contentJson(getWithNumberVoicesDtos()));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + italian.id())
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(italian));
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
        perform(MockMvcRequestBuilders.get(REST_URL + "all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getVotedByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/voted-by-user/" + UserTestData.user_1.getId())
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(italian));
    }

    @Test
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestJsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + italian.id())
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(UserTestData.admin))
                .content(TestJsonUtil.writeValue(getUpdated())))
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(service.get(italian.id()), getUpdated());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + italian.id())
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(italian.id()));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MealTestData.NOT_FOUND)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isUnprocessableEntity());
    }
}
