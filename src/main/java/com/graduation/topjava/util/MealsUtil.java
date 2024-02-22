package com.graduation.topjava.util;

import com.graduation.topjava.model.Meal;
import com.graduation.topjava.dto.MealViewDto;

import java.util.Set;
import java.util.stream.Collectors;

public class MealsUtil {
    private MealsUtil() {
    }

    public static Set<MealViewDto> getTos(Set<Meal> meals) {
        return meals.stream()
                .map(m -> new MealViewDto(m.getId(), m.getName(), m.getPrice()))
                .collect(Collectors.toSet());
    }
}
