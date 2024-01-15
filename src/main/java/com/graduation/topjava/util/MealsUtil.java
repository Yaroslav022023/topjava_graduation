package com.graduation.topjava.util;

import com.graduation.topjava.model.Meal;
import com.graduation.topjava.dto.MealDto;

import java.util.Set;
import java.util.stream.Collectors;

public class MealsUtil {
    private MealsUtil() {
    }

    public static Set<MealDto> getTos(Set<Meal> meals) {
        return meals.stream()
                .map(m -> new MealDto(m.getId(), m.getName(), m.getPrice()))
                .collect(Collectors.toSet());
    }
}
