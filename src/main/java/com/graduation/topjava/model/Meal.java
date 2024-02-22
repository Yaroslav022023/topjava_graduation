package com.graduation.topjava.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "meal",
        indexes = {@Index(columnList = "restaurant_id, date", name = "meal_restaurant_datetime_idx")})
public class Meal extends AbstractNamedEntity {
    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name = "price", nullable = false)
    @Range(min = 5, max = 2000)
    private long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    public Meal() {
    }

    public Meal(Integer id, LocalDate date, String name, long price) {
        super(id, name);
        this.date = date;
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name=" + name +
                ", date=" + date +
                ", price=" + price +
                '}';
    }
}
