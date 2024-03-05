package com.topjava.graduation.dto;

public class RestaurantVotedByUserDto {
    private Integer id;
    private String name;

    public RestaurantVotedByUserDto() {
    }

    public RestaurantVotedByUserDto(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
