package com.graduation.topjava;

import com.graduation.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

import static com.graduation.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Meal.class, "restaurant");
    public static final int NOT_FOUND = 111111;
    public static final int MEAL_ID = START_SEQ + 8;

    public static final Meal italian_meal1 = new Meal(MEAL_ID, LocalDate.now(), "Steak Philadelphia", 1600);
    public static final Meal italian_meal2 = new Meal(MEAL_ID + 1, LocalDate.now(), "Margherita Pizza", 300);
    public static final Meal italian_meal3 = new Meal(MEAL_ID + 2, LocalDate.now(), "Pasta", 700);

    public static final Meal asian_meal1 = new Meal(MEAL_ID + 3, LocalDate.now(), "Paella", 300);
    public static final Meal asian_meal2 = new Meal(MEAL_ID + 4, LocalDate.now(), "Shawarma", 200);
    public static final Meal asian_meal3 = new Meal(MEAL_ID + 5, LocalDate.now(), "Pad Thai", 250);
    public static final Meal asian_meal4 = new Meal(MEAL_ID + 6, LocalDate.now(), "Tandoori Chicken", 440);

    public static final Meal french_meal1 = new Meal(MEAL_ID + 7, LocalDate.now(), "Ratatouille", 680);
    public static final Meal french_meal2 = new Meal(MEAL_ID + 8, LocalDate.now(), "Beef Bourguignon", 530);

    public static final List<Meal> italian_meals = List.of(italian_meal2, italian_meal3, italian_meal1);
    public static final List<Meal> asian_meals = List.of(asian_meal1, asian_meal2, asian_meal3, asian_meal4);
    public static final List<Meal> french_meals = List.of(french_meal1, french_meal2);

    public static Meal getNew() {
        return new Meal(null, LocalDate.now(), "Created dish", 930);
    }

    public static Meal getUpdated() {
        return new Meal(italian_meal1.id(), LocalDate.now(), "Updated dish", 1500);
    }
}
