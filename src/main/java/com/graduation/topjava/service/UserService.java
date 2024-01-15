package com.graduation.topjava.service;

import com.graduation.topjava.model.User;
import com.graduation.topjava.repository.CrudUserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static com.graduation.topjava.util.ValidationUtil.checkNotFound;
import static com.graduation.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class UserService {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");
    private final CrudUserRepository crudUserRepository;

    public UserService(CrudUserRepository crudUserRepository) {
        this.crudUserRepository = crudUserRepository;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User save(User user) {
        Assert.notNull(user, "user must not be null");
        return crudUserRepository.save(user);
    }

    public User get(int id) {
        return checkNotFoundWithId(crudUserRepository.findById(id).orElse(null), id);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(crudUserRepository.delete(id), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(crudUserRepository.findByEmail(email), "email=" + email);
    }

    @Cacheable("users")
    public List<User> getAll() {
        return crudUserRepository.findAll(SORT_NAME_EMAIL);
    }
}
