package com.graduation.topjava.web.security;

import com.graduation.topjava.dto.UserDto;
import com.graduation.topjava.model.User;
import com.graduation.topjava.util.UsersUtil;

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