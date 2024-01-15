package com.graduation.topjava.service;

import com.graduation.topjava.model.Voice;
import com.graduation.topjava.repository.CrudRestaurantRepository;
import com.graduation.topjava.repository.CrudUserRepository;
import com.graduation.topjava.repository.CrudVoiceRepository;
import com.graduation.topjava.util.VoiceUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;

import static com.graduation.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoiceService {
    private final CrudVoiceRepository crudVoiceRepository;
    private final CrudUserRepository crudUserRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public VoiceService(CrudVoiceRepository crudVoiceRepository, CrudUserRepository crudUserRepository,
                        CrudRestaurantRepository crudRestaurantRepository) {
        this.crudVoiceRepository = crudVoiceRepository;
        this.crudUserRepository = crudUserRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Transactional
    public Voice save(Voice voice, int userId, int restaurantId) {
        Assert.notNull(voice, "voice must not be null");
        if (voice.isNew() || VoiceUtil.isAvailableUpdate(get(voice.id(), userId))) {
            voice.setUser(crudUserRepository.getReferenceById(userId));
            voice.setRestaurant(crudRestaurantRepository.getReferenceById(restaurantId));
            return crudVoiceRepository.save(voice);
        }
        return null;
    }

    private Voice get(int id, int userId) {
        return checkNotFoundWithId(crudVoiceRepository.findByIdAndUserIdForToday(id, userId, LocalDate.now()), id);
    }
}
