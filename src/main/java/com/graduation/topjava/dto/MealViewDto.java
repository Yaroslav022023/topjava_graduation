package com.graduation.topjava.dto;

import java.util.Objects;

public class MealViewDto {
    private Integer id;
    private String name;
    private long price;

    public MealViewDto(Integer id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealViewDto mealViewDTO = (MealViewDto) o;
        return price == mealViewDTO.price && Objects.equals(id, mealViewDTO.id) && Objects.equals(name, mealViewDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
