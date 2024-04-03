package com.topjava.graduation.util;

import com.topjava.graduation.dto.MealViewDto;
import com.topjava.graduation.model.Meal;

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