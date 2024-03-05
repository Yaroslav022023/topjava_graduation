package com.topjava.graduation.web.security;

import com.topjava.graduation.dto.UserDto;
import com.topjava.graduation.model.User;
import com.topjava.graduation.util.UsersUtil;

import java.io.Serial;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    @Serial
    private static final long serialVersionUID = 1L;

    private UserDto userDto;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.userDto = UsersUtil.asDto(user);
    }

    public int getId() {
        return userDto.id();
    }

    public void update(UserDto newDto) {
        userDto = newDto;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    @Override
    public String toString() {
        return userDto.toString();
    }
}