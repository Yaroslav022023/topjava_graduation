package com.graduation.topjava.util;

import com.graduation.topjava.model.Restaurant;
import com.graduation.topjava.dto.RestaurantViewDto;
import com.graduation.topjava.dto.RestaurantVotedByUserDto;

import java.util.List;

public class RestaurantUtil {
    private RestaurantUtil() {
    }

    public static List<RestaurantViewDto> convertToViewDtos(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(r -> new RestaurantViewDto(r.getId(), r.getName(), MealsUtil.getTos(r.getMeals())))
                .toList();
    }

    public static RestaurantVotedByUserDto convertToVotedByUserDto(Restaurant restaurant) {
        return new RestaurantVotedByUserDto(restaurant.getId(), restaurant.getName());
    }
}
