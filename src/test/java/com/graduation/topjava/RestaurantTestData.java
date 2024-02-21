package com.graduation.topjava;

import com.graduation.topjava.model.Restaurant;

import java.util.HashSet;

import static com.graduation.topjava.MealTestData.*;
import static com.graduation.topjava.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHERMATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "meals");

    public static final int ITALIAN_ID = START_SEQ + 5;
    public static final int ASIAN_ID = START_SEQ + 6;
    public static final int FRENCH_ID = START_SEQ + 7;

    public static final Restaurant italian = new Restaurant(ITALIAN_ID, "Italian");
    public static final Restaurant asian = new Restaurant(ASIAN_ID, "Asian");
    public static final Restaurant french = new Restaurant(FRENCH_ID, "French");

    static {
        italian.setMeals(new HashSet<>(italian_meals));
        asian.setMeals(new HashSet<>(asian_meals));
        french.setMeals(new HashSet<>(french_meals));
    }
}
