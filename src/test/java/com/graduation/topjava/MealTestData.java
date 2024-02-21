package com.graduation.topjava;

import com.graduation.topjava.model.Meal;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static com.graduation.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Meal.class, "restaurant");
    public static final int NOT_FOUND = 111111;
    public static final int MEAL_ID = START_SEQ + 8;

    public static final Meal italian_meal1 = new Meal(MEAL_ID, LocalDate.of(2024, Month.JANUARY, 15), "Steak Philadelphia", 1600);
    public static final Meal italian_meal2 = new Meal(MEAL_ID + 1, LocalDate.of(2024, Month.JANUARY, 15), "Margherita Pizza", 300);
    public static final Meal italian_meal3 = new Meal(MEAL_ID + 2, LocalDate.of(2024, Month.JANUARY, 15), "Sushi", 700);

    public static final Meal asian_meal1 = new Meal(MEAL_ID + 3, LocalDate.of(2024, Month.JANUARY, 15), "Paella", 300);
    public static final Meal asian_meal2 = new Meal(MEAL_ID + 4, LocalDate.of(2024, Month.JANUARY, 15), "Shawarma", 200);
    public static final Meal asian_meal3 = new Meal(MEAL_ID + 5, LocalDate.of(2024, Month.JANUARY, 15), "Pad Thai", 250);
    public static final Meal asian_meal4 = new Meal(MEAL_ID + 6, LocalDate.of(2024, Month.JANUARY, 15), "Tandoori Chicken", 440);

    public static final Meal french_meal1 = new Meal(MEAL_ID + 7, LocalDate.of(2024, Month.JANUARY, 15), "Ratatouille", 680);
    public static final Meal french_meal2 = new Meal(MEAL_ID + 8, LocalDate.of(2024, Month.JANUARY, 15), "Beef Bourguignon", 530);

    public static final List<Meal> italian_meals = List.of(italian_meal1, italian_meal2, italian_meal3);
    public static final List<Meal> asian_meals = List.of(asian_meal1, asian_meal2, asian_meal3, asian_meal4);
    public static final List<Meal> french_meals = List.of(french_meal1, french_meal2);

    public static Meal getNew() {
        return new Meal(null, LocalDate.of(2024, Month.JANUARY, 15), "Created dish", 930);
    }

    public static Meal getUpdated() {
        return new Meal(italian_meal1.id(), italian_meal1.getDate().plusDays(1), "Updated dish", 1500);
    }
}
