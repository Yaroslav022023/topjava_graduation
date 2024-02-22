package com.graduation.topjava.service;

import com.graduation.topjava.model.User;
import com.graduation.topjava.repository.CrudUserRepository;
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

    public User save(User user) {
        Assert.notNull(user, "user must not be null");
        return user.isNew() || get(user.id()) != null ? crudUserRepository.save(user) : null;
    }

    public User get(int id) {
        return checkNotFoundWithId(crudUserRepository.findById(id).orElse(null), id);
    }

    public void delete(int id) {
        checkNotFoundWithId(crudUserRepository.delete(id) != 0, id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(crudUserRepository.findByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return crudUserRepository.findAll(SORT_NAME_EMAIL);
    }
}
