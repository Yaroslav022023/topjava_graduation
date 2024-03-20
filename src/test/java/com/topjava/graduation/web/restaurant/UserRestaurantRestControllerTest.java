package com.topjava.graduation.web.restaurant;

import com.topjava.graduation.RestaurantTestData;
import com.topjava.graduation.dto.RestaurantVotedByUserDto;
import com.topjava.graduation.service.RestaurantService;
import com.topjava.graduation.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.topjava.graduation.RestaurantTestData.*;
import static com.topjava.graduation.TestUtil.userHttpBasic;
import static com.topjava.graduation.UserTestData.user_1;
import static com.topjava.graduation.UserTestData.user_3;
import static com.topjava.graduation.util.RestaurantUtil.convertToViewDtos;
import static com.topjava.graduation.util.RestaurantUtil.convertToVotedByUserDto;
import static com.topjava.graduation.util.exception.ErrorType.VOTING_RESTRICTIONS;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = UserRestaurantRestController.REST_URL + '/';
    @Autowired
    private RestaurantService service;

    @Test
    void getAllWithMealsForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_VIEW_DTO_MATCHER
                        .contentJson(convertToViewDtos(RestaurantTestData.restaurants)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllWithNumberVoicesForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/number-voices")
                .with(userHttpBasic(user_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_WITH_NUMBER_VOICES_DTO_MATCHER
                        .contentJson(getWithNumberVoicesDtos()));
    }

    @Test
    void getVotedByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/voted-by-user")
                .with(userHttpBasic(user_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RestaurantTestData.italian));
    }

    @Test
    void vote() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + asian.id())
                .with(userHttpBasic(user_1)))
                .andExpect(status().isCreated());

        RestaurantVotedByUserDto created = RESTAURANT_VOTED_BY_USER_DTO_MATCHER.readFromJson(action);
        RESTAURANT_VOTED_BY_USER_DTO_MATCHER.assertMatch(created, convertToVotedByUserDto(asian));

        restaurantsWithNumberVoices.get(0).setVoices(1);
        restaurantsWithNumberVoices.get(1).setVoices(2);
        RESTAURANT_WITH_NUMBER_VOICES_DTO_MATCHER.assertMatch(
                service.getAllWithNumberVoicesForToday(), restaurantsWithNumberVoices);
        restaurantsWithNumberVoices.get(0).setVoices(2);
        restaurantsWithNumberVoices.get(1).setVoices(1);
    }

    @Test
    void voteRestrictions() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + asian.getId())
                .with(userHttpBasic(user_3)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VOTING_RESTRICTIONS))
                .andExpect(detailMessages(1, "It is not possible to change the voting time after 11:00 a.m."));
    }
}