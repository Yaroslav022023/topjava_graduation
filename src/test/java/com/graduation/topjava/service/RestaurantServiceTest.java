package com.graduation.topjava.service;

import com.graduation.topjava.RestaurantTestData;
import com.graduation.topjava.VoiceTestData;
import com.graduation.topjava.model.Restaurant;
import com.graduation.topjava.model.Voice;
import com.graduation.topjava.util.RestaurantUtil;
import com.graduation.topjava.util.exception.NotFoundException;
import com.graduation.topjava.util.exception.VotingRestrictionsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.graduation.topjava.MealTestData.NOT_FOUND;
import static com.graduation.topjava.RestaurantTestData.*;
import static com.graduation.topjava.UserTestData.*;
import static com.graduation.topjava.VoiceTestData.VOICE_MATCHER;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    protected RestaurantService service;

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(service.getAll(), restaurants);
    }

    @Test
    void get() {
        RESTAURANT_MATCHER.assertMatch(service.get(ITALIAN_ID), italian);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void create() {
        Restaurant created = service.save(RestaurantTestData.getNew());
        int newId = created.id();
        Restaurant newRestaurant = RestaurantTestData.getNew();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    void update() {
        Restaurant updated = RestaurantTestData.getUpdated();
        service.save(updated);
        RESTAURANT_MATCHER.assertMatch(service.get(ITALIAN_ID), RestaurantTestData.getUpdated());
    }

    @Test
    void updateNotFound() {
        Restaurant notFound = new Restaurant(NOT_FOUND, "Not Exist");
        assertThrows(NotFoundException.class, () -> service.save(notFound));
    }

    @Test
    void delete() {
        service.delete(ASIAN_ID);
        assertThrows(NotFoundException.class, () -> service.get(ASIAN_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void getAllWithMealsForToday() {
        RESTAURANT_VIEW_DTO_MATCHER.assertMatch(service.getAllWithMealsForToday(),
                RestaurantUtil.convertToViewDtos(restaurants));
    }

    @Test
    void getAllWithNumberVoicesForToday() {
        RESTAURANT_WITH_NUMBER_VOICES_DTO_MATCHER.assertMatch(
                service.getAllWithNumberVoicesForToday(), restaurantsWithNumberVoices);
    }

    @Test
    void vote() {
        Voice created = service.vote(VoiceTestData.getNew(), ADMIN_ID, FRENCH_ID);
        int newId = created.id();
        Voice newVoice = VoiceTestData.getNew();
        newVoice.setId(newId);

        VOICE_MATCHER.assertMatch(created, newVoice);

        RESTAURANT_VOTED_BY_USER_DTO_MATCHER.assertMatch(
                service.getVotedByUser(ADMIN_ID), RestaurantUtil.convertToVotedByUserDto(french));

        restaurantsWithNumberVoices.get(2).setVoices(1);
        RESTAURANT_WITH_NUMBER_VOICES_DTO_MATCHER.assertMatch(
                service.getAllWithNumberVoicesForToday(), restaurantsWithNumberVoices);
        restaurantsWithNumberVoices.get(2).setVoices(0);
    }

    @Test
    void voteChange() {
        service.vote(VoiceTestData.getNew(), USER_1_ID, FRENCH_ID);

        RESTAURANT_VOTED_BY_USER_DTO_MATCHER.assertMatch(
                service.getVotedByUser(USER_1_ID), RestaurantUtil.convertToVotedByUserDto(french));

        restaurantsWithNumberVoices.get(0).setVoices(1);
        restaurantsWithNumberVoices.get(2).setVoices(1);
        RESTAURANT_WITH_NUMBER_VOICES_DTO_MATCHER.assertMatch(
                service.getAllWithNumberVoicesForToday(), restaurantsWithNumberVoices);
        restaurantsWithNumberVoices.get(0).setVoices(2);
        restaurantsWithNumberVoices.get(2).setVoices(0);
    }

    @Test
    void voteRestrictions() {
        assertThrows(VotingRestrictionsException.class, () ->
                service.vote(VoiceTestData.getNew(), USER_3_ID, FRENCH_ID));
    }

    @Test
    void getVotedByUser() {
        RESTAURANT_VOTED_BY_USER_DTO_MATCHER.assertMatch(
                service.getVotedByUser(USER_1_ID), RestaurantUtil.convertToVotedByUserDto(italian));
    }

    @Test
    void getVotedByUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.getVotedByUser(GUEST_ID));
    }
}
