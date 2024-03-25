package com.topjava.graduation.service;

import com.topjava.graduation.dto.RestaurantViewDto;
import com.topjava.graduation.dto.RestaurantVotedByUserDto;
import com.topjava.graduation.dto.RestaurantWithNumberVoicesDto;
import com.topjava.graduation.model.Restaurant;
import com.topjava.graduation.model.Voice;
import com.topjava.graduation.repository.CrudRestaurantRepository;
import com.topjava.graduation.repository.CrudUserRepository;
import com.topjava.graduation.repository.CrudVoiceRepository;
import com.topjava.graduation.util.RestaurantUtil;
import com.topjava.graduation.util.VoiceUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.topjava.graduation.util.validation.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {
    private final CrudRestaurantRepository crudRestaurantRepository;
    private final CrudVoiceRepository crudVoiceRepository;
    private final CrudUserRepository crudUserRepository;

    public RestaurantService(CrudRestaurantRepository crudRestaurantRepository,
                             CrudVoiceRepository crudVoiceRepository, CrudUserRepository crudUserRepository) {
        this.crudRestaurantRepository = crudRestaurantRepository;
        this.crudVoiceRepository = crudVoiceRepository;
        this.crudUserRepository = crudUserRepository;
    }

    public List<Restaurant> getAll() {
        return crudRestaurantRepository.findAll();
    }

    @Cacheable("restaurants")
    public List<RestaurantViewDto> getAllWithMealsForToday() {
        return RestaurantUtil.convertToViewDtos(crudRestaurantRepository.findAllWithMealsForToday(LocalDate.now()));
    }

    public List<RestaurantWithNumberVoicesDto> getAllWithNumberVoicesForToday() {
        return crudRestaurantRepository.findAllWithNumberVoicesForToday(LocalDate.now());
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(crudRestaurantRepository.findById(id).orElse(null), id);
    }

    public RestaurantVotedByUserDto getVotedByUser(int userId) {
        Restaurant restaurant =
                checkNotFoundWithId(crudVoiceRepository.findByUserIdForToday(userId, LocalDate.now()), userId)
                        .getRestaurant();
        return restaurant != null ? RestaurantUtil.convertToVotedByUserDto(restaurant) : null;
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant save(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return restaurant.isNew() || get(restaurant.id()) != null ? crudRestaurantRepository.save(restaurant) : null;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(crudRestaurantRepository.delete(id) != 0, id);
    }

    @Transactional
    public void vote(int userId, int restaurantId) {
        Voice existing = crudVoiceRepository.findByUserIdForToday(userId, LocalDate.now());
        if (existing != null) {
            if (VoiceUtil.isAvailableUpdate(existing)) {
                existing.setRestaurant(crudRestaurantRepository.getReferenceById(restaurantId));
                existing.setTime(LocalTime.now());
            }
        } else {
            Voice voice = new Voice();
            voice.setUser(crudUserRepository.getReferenceById(userId));
            voice.setRestaurant(crudRestaurantRepository.getReferenceById(restaurantId));
            crudVoiceRepository.save(voice);
        }
    }
}
