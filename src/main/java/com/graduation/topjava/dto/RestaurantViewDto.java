package com.graduation.topjava.dto;

import java.util.Set;

public class RestaurantViewDto {
    private Integer id;
    private String name;
    private Set<MealDto> meals;

    public RestaurantViewDto(Integer id, String name, Set<MealDto> meals) {
        this.id = id;
        this.name = name;
        this.meals = meals;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MealDto> getMeals() {
        return meals;
    }

    public void setMeals(Set<MealDto> meals) {
        this.meals = meals;
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", meals=" + meals +
                '}';
    }
}
