package com.topjava.graduation.web.json;

import com.topjava.graduation.model.Meal;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.topjava.graduation.MealTestData.*;
import static com.topjava.graduation.web.json.JsonUtil.readValue;
import static com.topjava.graduation.web.json.JsonUtil.readValues;
import static com.topjava.graduation.web.json.TestJsonUtil.writeValue;


class JsonUtilTest {
    private static final Logger log = LoggerFactory.getLogger(JsonUtilTest.class);

    @Test
    void readWriteValue() {
        String json = TestJsonUtil.writeValue(italian_meal1);
        log.info(json);
        Meal meal = readValue(json, Meal.class);
        MEAL_MATCHER.assertMatch(meal, italian_meal1);
    }

    @Test
    void readWriteValues() {
        String json = writeValue(italian_meals);
        log.info(json);
        List<Meal> actual = readValues(json, Meal.class);
        MEAL_MATCHER.assertMatch(actual, italian_meals);
    }
}