package com.graduation.topjava.web.user;

import com.graduation.topjava.dto.UserDto;
import com.graduation.topjava.model.User;
import com.graduation.topjava.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.graduation.topjava.util.ValidationUtil.assureIdConsistent;
import static com.graduation.topjava.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserService service;

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return service.save(user);
    }

    public User create(UserDto userDto) {
        log.info("create {}", userDto);
        checkNew(userDto);
        return service.save(userDto);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.save(user);
    }

    public void update(UserDto userDto, int id) {
        log.info("update {} with id={}", userDto, id);
        assureIdConsistent(userDto, id);
        service.save(userDto);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        service.enable(id, enabled);
    }
}
