package com.topjava.graduation.dto;

import java.util.Objects;
import java.util.Set;

public class RestaurantViewDto {
    private Integer id;
    private String name;
    private Set<MealViewDto> meals;

    public RestaurantViewDto() {
    }

    public RestaurantViewDto(Integer id, String name, Set<MealViewDto> meals) {
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

    public Set<MealViewDto> getMeals() {
        return meals;
    }

    public void setMeals(Set<MealViewDto> meals) {
        this.meals = meals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantViewDto that = (RestaurantViewDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(meals, that.meals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, meals);
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