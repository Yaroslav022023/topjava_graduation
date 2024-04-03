package com.topjava.graduation.service;

import com.topjava.graduation.dto.UserDto;
import com.topjava.graduation.model.User;
import com.topjava.graduation.repository.CrudUserRepository;
import com.topjava.graduation.util.UsersUtil;
import com.topjava.graduation.web.security.AuthorizedUser;
import com.topjava.graduation.util.validation.ValidationUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.topjava.graduation.util.validation.ValidationUtil.checkNotFound;
import static com.topjava.graduation.util.validation.ValidationUtil.checkNotFoundWithId;

@Service()
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");
    private final CrudUserRepository crudUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(CrudUserRepository crudUserRepository, PasswordEncoder passwordEncoder) {
        this.crudUserRepository = crudUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        return crudUserRepository.findAll(SORT_NAME_EMAIL);
    }

    public User get(int id) {
        return checkNotFoundWithId(crudUserRepository.findById(id).orElse(null), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(crudUserRepository.findByEmail(email), "email=" + email);
    }

    @Transactional
    public User save(User user) {
        Assert.notNull(user, "user must not be null");
        return user.isNew() || get(user.id()) != null ?
                crudUserRepository.save(UsersUtil.prepareToSave(user, passwordEncoder)) : null;
    }

    @Transactional
    public User save(UserDto userDto) {
        Assert.notNull(userDto, "user must not be null");
        if (userDto.isNew()) {
            return crudUserRepository.save(UsersUtil.prepareToSave(UsersUtil.createNewFromDto(userDto), passwordEncoder));
        }
        User user = get(userDto.id());
        if (user != null) {
            return UsersUtil.prepareToSave(UsersUtil.updateFromDto(user, userDto), passwordEncoder);
        }
        return null;
    }

    public void delete(int id) {
        ValidationUtil.checkNotFoundWithId(crudUserRepository.delete(id) != 0, id);
    }

    @Transactional
    public void enable(int id, boolean enabled) {
        User user = get(id);
        user.setEnabled(enabled);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}