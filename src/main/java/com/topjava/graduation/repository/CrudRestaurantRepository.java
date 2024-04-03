package com.topjava.graduation.repository;

import com.topjava.graduation.dto.RestaurantWithNumberVoicesDto;
import com.topjava.graduation.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT DISTINCT r FROM Restaurant r JOIN FETCH r.meals m WHERE m.date = :date")
    List<Restaurant> findAllWithMealsForToday(@Param("date") LocalDate date);

    @Query("SELECT new com.topjava.graduation.dto.RestaurantWithNumberVoicesDto(r.id, r.name, COUNT(v.id)) " +
            "FROM Restaurant r " +
            "LEFT JOIN Voice v ON v.restaurant.id = r.id AND v.date = :date " +
            "GROUP BY r.id")
    List<RestaurantWithNumberVoicesDto> findAllWithNumberVoicesForToday(@Param("date") LocalDate date);

}