package com.graduation.topjava.repository;

import com.graduation.topjava.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    int delete(@Param("id") int id, @Param("restaurantId") int restaurantId);

    @Query("SELECT m FROM Meal m WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    Meal findByIdAndRestaurantId(@Param("id") int id, @Param("restaurantId") int restaurantId);

    @Query("SELECT m FROM Meal m WHERE m.restaurant.id=:restaurantId ORDER BY m.date DESC")
    List<Meal> findAllByRestaurantId(@Param("restaurantId") int restaurantId);

    @Query("SELECT m FROM Meal m WHERE m.name=:name AND m.date=:date AND m.restaurant.id=:restaurantId")
    Meal checkDuplicate(@Param("name") String name, @Param("date") LocalDate date,
                        @Param("restaurantId") int restaurantId);
}
