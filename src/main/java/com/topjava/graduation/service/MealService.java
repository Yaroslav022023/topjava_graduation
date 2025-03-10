package com.topjava.graduation.service;

import com.topjava.graduation.model.Meal;
import com.topjava.graduation.repository.CrudMealRepository;
import com.topjava.graduation.repository.CrudRestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.topjava.graduation.util.validation.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private final CrudMealRepository crudMealRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public MealService(CrudMealRepository crudMealRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    public List<Meal> getAll(int restaurantId) {
        return crudMealRepository.findAllByRestaurantId(restaurantId);
    }

    public Meal get(int id, int restaurantId) {
        return checkNotFoundWithId(crudMealRepository.findByIdAndRestaurantId(id, restaurantId), id);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Meal save(Meal meal, int restaurantId) {
        Assert.notNull(meal, "meal must not be null");
        if (!meal.isNew() && get(meal.id(), restaurantId) == null) {
            return null;
        }
        meal.setRestaurant(crudRestaurantRepository.getReferenceById(restaurantId));
        return crudMealRepository.save(meal);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id, int restaurantId) {
        checkNotFoundWithId(crudMealRepository.delete(id, restaurantId) != 0, id);
    }
}