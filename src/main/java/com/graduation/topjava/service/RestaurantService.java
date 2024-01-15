package com.graduation.topjava.service;

import com.graduation.topjava.model.Restaurant;
import com.graduation.topjava.repository.CrudRestaurantRepository;
import com.graduation.topjava.repository.CrudVoiceRepository;
import com.graduation.topjava.dto.RestaurantViewDto;
import com.graduation.topjava.dto.RestaurantVotedByUserDto;
import com.graduation.topjava.util.RestaurantUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.graduation.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {
    private final CrudRestaurantRepository crudRestaurantRepository;
    private final CrudVoiceRepository crudVoiceRepository;

    public RestaurantService(CrudRestaurantRepository crudRestaurantRepository, CrudVoiceRepository crudVoiceRepository) {
        this.crudRestaurantRepository = crudRestaurantRepository;
        this.crudVoiceRepository = crudVoiceRepository;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant save(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return crudRestaurantRepository.save(restaurant);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(crudRestaurantRepository.findById(id).orElse(null), id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(crudRestaurantRepository.delete(id), id);
    }

    public List<Restaurant> getAll() {
        return crudRestaurantRepository.findAll();
    }

    @Cacheable("restaurants")
    public List<RestaurantViewDto> getAllWithMealsForToday() {
        return RestaurantUtil.convertToViewDtos(crudRestaurantRepository.findAllWithMealsForToday(LocalDate.now()));
    }

    public RestaurantVotedByUserDto getVotedByUser(int userId) {
        Restaurant restaurant = crudVoiceRepository.findByUserIdForToday(userId, LocalDate.now()).getRestaurant();
        if (restaurant != null) {
            return RestaurantUtil.convertToVotedByUserDto(restaurant);
        }
        return null;
    }
}
