package com.topjava.graduation.dto;

import java.util.Objects;

public class RestaurantWithNumberVoicesDto {
    private Integer id;
    private String name;
    private int voices;

    public RestaurantWithNumberVoicesDto() {
    }

    public RestaurantWithNumberVoicesDto(Integer id, String name, long voices) {
        this.id = id;
        this.name = name;
        this.voices = (int) voices;
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

    public int getVoices() {
        return voices;
    }

    public void setVoices(int voices) {
        this.voices = voices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantWithNumberVoicesDto that = (RestaurantWithNumberVoicesDto) o;
        return voices == that.voices && Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, voices);
    }

    @Override
    public String toString() {
        return "RestaurantWithNumberVoicesDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", voices=" + voices +
                '}';
    }
}
