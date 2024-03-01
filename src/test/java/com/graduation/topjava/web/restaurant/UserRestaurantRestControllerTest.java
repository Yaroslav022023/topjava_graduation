package com.graduation.topjava.web.restaurant;

import com.graduation.topjava.dto.RestaurantVotedByUserDto;
import com.graduation.topjava.service.RestaurantService;
import com.graduation.topjava.util.exception.VotingRestrictionsException;
import com.graduation.topjava.web.AbstractControllerTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.graduation.topjava.RestaurantTestData.*;
import static com.graduation.topjava.TestUtil.userHttpBasic;
import static com.graduation.topjava.UserTestData.user_1;
import static com.graduation.topjava.UserTestData.user_3;
import static com.graduation.topjava.util.RestaurantUtil.convertToViewDtos;
import static com.graduation.topjava.util.RestaurantUtil.convertToVotedByUserDto;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
                .andExpect(RESTAURANT_VIEW_DTO_MATCHER.contentJson(convertToViewDtos(restaurants)));
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
                .andExpect(RESTAURANT_WITH_NUMBER_VOICES_DTO_MATCHER.contentJson(getWithNumberVoicesDtos()));
    }

    @Test
    void getVotedByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/voted-by-user")
                .with(userHttpBasic(user_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(italian));
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

    @Disabled
    @Test
    void voteRestrictions() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + asian.getId())
                .with(userHttpBasic(user_3)))
                .andExpect(status().isCreated());

        assertThrows(VotingRestrictionsException.class, () -> action.andReturn().getResponse().getContentAsString());
    }
}