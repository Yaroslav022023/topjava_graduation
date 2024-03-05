package com.graduation.topjava.util;

import com.graduation.topjava.dto.UserDto;
import com.graduation.topjava.model.Role;
import com.graduation.topjava.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsersUtil {

    public static User createNewFromDto(UserDto userDto) {
        return new User(null, userDto.getName(), userDto.getEmail().toLowerCase(), userDto.getPassword(), Role.USER);
    }

    public static UserDto asDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public static User updateFromDto(User user, UserDto userDto) {
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setPassword(userDto.getPassword());
        return user;
    }

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
