package com.topjava.graduation;

import com.topjava.graduation.dto.UserDto;
import com.topjava.graduation.model.Role;
import com.topjava.graduation.model.User;

import java.util.Collections;
import java.util.List;

import static com.topjava.graduation.web.json.TestJsonUtil.writeAdditionProps;
import static com.topjava.graduation.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int ADMIN_ID = START_SEQ;
    public static final int USER_1_ID = START_SEQ + 1;
    public static final int USER_2_ID = START_SEQ + 2;
    public static final int USER_3_ID = START_SEQ + 3;
    public static final int GUEST_ID = START_SEQ + 4;

    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.USER);
    public static final User user_1 = new User(USER_1_ID, "User_1", "user_1@gmail.com", "password_1", Role.USER);
    public static final User user_2 = new User(USER_2_ID, "User_2", "user_2@gmail.com", "password_2", Role.USER);
    public static final User user_3 = new User(USER_3_ID, "User_3", "user_3@gmail.com", "password_3", Role.USER);
    public static final User guest = new User(GUEST_ID, "Guest", "guest@gmail.com", "guest");

    public static final List<User> users = List.of(admin, guest, user_1, user_2, user_3);

    public static User getNew() {
        return new User(null, "nameNew", "email_new@gmail.com", "passNew", Role.USER);
    }

    public static User getUpdated() {
        User updated = new User(USER_2_ID, "User_2_updated", "email_updated@yandex.ru", "passUpdated", Role.USER);
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static UserDto getNewDto() {
        return new UserDto(null, "nameNew", "email_new@gmail.com", "passNew");
    }

    public static UserDto getUpdatedDto() {
        return new UserDto(null, "nameUpdated", "email_updated@gmail.com", "passUpdated");
    }

    public static String jsonWithPassword(User user, String passw) {
        return writeAdditionProps(user, "password", passw);
    }

    public static String jsonWithPassword(UserDto userDto, String passw) {
        return writeAdditionProps(userDto, "password", passw);
    }
}