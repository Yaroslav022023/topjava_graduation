package com.graduation.topjava.util;

import com.graduation.topjava.dto.UserDto;
import com.graduation.topjava.model.Role;
import com.graduation.topjava.model.User;

public class UsersUtil {

    public static User createNewFromDto(UserDto userDto) {
        return new User(null, userDto.getName(), userDto.getEmail().toLowerCase(), userDto.getPassword(), Role.USER);
    }

    public static void updateFromDto(User user, UserDto userDto) {
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setPassword(userDto.getPassword());
    }
}
