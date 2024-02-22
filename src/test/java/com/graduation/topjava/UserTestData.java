package com.graduation.topjava;

import com.graduation.topjava.model.Role;
import com.graduation.topjava.model.User;

import static com.graduation.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered");

    public static final int ADMIN_ID = START_SEQ;
    public static final int USER_1_ID = START_SEQ + 1;
    public static final int USER_2_ID = START_SEQ + 2;
    public static final int USER_3_ID = START_SEQ + 3;
    public static final int GUEST_ID = START_SEQ + 4;

    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.USER);
    public static final User user_1 = new User(USER_1_ID, "User_1", "user_1@yandex.ru", "password_1", Role.USER);
    public static final User user_2 = new User(USER_2_ID, "User_2", "user_2@yandex.ru", "password_2", Role.USER);
    public static final User user_3 = new User(USER_3_ID, "User_3", "user_3@yandex.ru", "password_3", Role.USER);
    public static final User guest = new User(GUEST_ID, "Guest", "guest@gmail.com", "guest");
}
