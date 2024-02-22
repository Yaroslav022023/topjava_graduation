package com.graduation.topjava.service;

import com.graduation.topjava.model.Meal;
import com.graduation.topjava.repository.CrudMealRepository;
import com.graduation.topjava.repository.CrudRestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.graduation.topjava.util.ValidationUtil.checkDuplicate;
import static com.graduation.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private final CrudMealRepository crudMealRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public MealService(CrudMealRepository crudMealRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Meal save(Meal meal, int restaurantId) {
        Assert.notNull(meal, "meal must not be null");
        if (meal.isNew() || get(meal.id(), restaurantId) != null) {
            checkDuplicate(crudMealRepository.checkDuplicate(meal.getName(), meal.getDate(), restaurantId) != null,
                    meal.getName() + " " + meal.getDate().toString());

            meal.setRestaurant(crudRestaurantRepository.getReferenceById(restaurantId));
            return crudMealRepository.save(meal);
        }
        return null;
    }

    public Meal get(int id, int restaurantId) {
        return checkNotFoundWithId(crudMealRepository.findByIdAndRestaurantId(id, restaurantId), id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id, int restaurantId) {
        checkNotFoundWithId(crudMealRepository.delete(id, restaurantId) != 0, id);
    }

    public List<Meal> getAll(int restaurantId) {
        return crudMealRepository.findAllByRestaurantId(restaurantId);
    }
}
