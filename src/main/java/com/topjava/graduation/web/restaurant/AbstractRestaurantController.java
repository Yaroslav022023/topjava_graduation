package com.topjava.graduation.web.restaurant;

import com.topjava.graduation.dto.RestaurantViewDto;
import com.topjava.graduation.dto.RestaurantVotedByUserDto;
import com.topjava.graduation.dto.RestaurantWithNumberVoicesDto;
import com.topjava.graduation.model.Restaurant;
import com.topjava.graduation.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.topjava.graduation.util.validation.ValidationUtil.assureIdConsistent;
import static com.topjava.graduation.util.validation.ValidationUtil.checkNew;

public class AbstractRestaurantController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private RestaurantService service;

    public List<Restaurant> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public List<RestaurantViewDto> getAllWithMealsForToday() {
        log.info("getAllWithMealsForToday");
        return service.getAllWithMealsForToday();
    }

    public List<RestaurantWithNumberVoicesDto> getAllWithNumberVoicesForToday() {
        log.info("getAllWithNumberVoicesForToday");
        return service.getAllWithNumberVoicesForToday();
    }

    public Restaurant get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public RestaurantVotedByUserDto getVotedByUser(int id) {
        log.info("getVotedByUser {}", id);
        return service.getVotedByUser(id);
    }

    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        return service.save(restaurant);
    }

    public void update(Restaurant restaurant, int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        service.save(restaurant);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void vote(int userId, int restaurantId) {
        log.info("vote from user {} for restaurant {}", userId, restaurantId);
        service.vote(userId, restaurantId);
    }
}
