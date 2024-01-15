package com.graduation.topjava.repository;

import com.graduation.topjava.model.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface CrudVoiceRepository extends JpaRepository<Voice, Integer> {
    @Query("SELECT v FROM Voice v WHERE v.id=:id AND v.user.id=:userId AND v.date = :date")
    Voice findByIdAndUserIdForToday(@Param("id") int id, @Param("userId") int userId, @Param("date") LocalDate date);

    @Query("SELECT v FROM Voice v WHERE v.user.id = :userId AND v.date = :date")
    Voice findByUserIdForToday(@Param("userId") int userId, @Param("date") LocalDate date);
}
