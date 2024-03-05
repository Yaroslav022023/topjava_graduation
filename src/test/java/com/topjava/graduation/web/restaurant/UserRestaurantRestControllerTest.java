package com.topjava.graduation.web.restaurant;

import com.topjava.graduation.dto.RestaurantVotedByUserDto;
import com.topjava.graduation.service.RestaurantService;
import com.topjava.graduation.web.AbstractControllerTest;
import com.topjava.graduation.RestaurantTestData;
import com.topjava.graduation.UserTestData;
import com.topjava.graduation.util.RestaurantUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.topjava.graduation.TestUtil.userHttpBasic;
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
                .with(userHttpBasic(UserTestData.user_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_VIEW_DTO_MATCHER.contentJson(RestaurantUtil.convertToViewDtos(RestaurantTestData.restaurants)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllWithNumberVoicesForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/number-voices")
                .with(userHttpBasic(UserTestData.user_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_WITH_NUMBER_VOICES_DTO_MATCHER.contentJson(RestaurantTestData.getWithNumberVoicesDtos()));
    }

    @Test
    void getVotedByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/voted-by-user")
                .with(userHttpBasic(UserTestData.user_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(RestaurantTestData.italian));
    }

    @Test
    void vote() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + RestaurantTestData.asian.id())
                .with(userHttpBasic(UserTestData.user_1)))
                .andExpect(status().isCreated());

        RestaurantVotedByUserDto created = RestaurantTestData.RESTAURANT_VOTED_BY_USER_DTO_MATCHER.readFromJson(action);
        RestaurantTestData.RESTAURANT_VOTED_BY_USER_DTO_MATCHER.assertMatch(created, RestaurantUtil.convertToVotedByUserDto(RestaurantTestData.asian));

        RestaurantTestData.restaurantsWithNumberVoices.get(0).setVoices(1);
        RestaurantTestData.restaurantsWithNumberVoices.get(1).setVoices(2);
        RestaurantTestData.RESTAURANT_WITH_NUMBER_VOICES_DTO_MATCHER.assertMatch(
                service.getAllWithNumberVoicesForToday(), RestaurantTestData.restaurantsWithNumberVoices);
        RestaurantTestData.restaurantsWithNumberVoices.get(0).setVoices(2);
        RestaurantTestData.restaurantsWithNumberVoices.get(1).setVoices(1);
    }

    @Test
    void voteRestrictions() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + RestaurantTestData.asian.getId())
                .with(userHttpBasic(UserTestData.user_3)))
                .andExpect(status().isUnprocessableEntity());
    }
}