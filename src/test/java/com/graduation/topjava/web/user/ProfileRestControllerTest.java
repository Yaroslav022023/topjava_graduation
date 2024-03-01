package com.graduation.topjava.web.user;

import com.graduation.topjava.model.Role;
import com.graduation.topjava.model.User;
import com.graduation.topjava.service.UserService;
import com.graduation.topjava.util.UsersUtil;
import com.graduation.topjava.web.AbstractControllerTest;
import com.graduation.topjava.web.json.JsonUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.graduation.topjava.UserTestData.*;
import static com.graduation.topjava.web.user.ProfileRestController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileRestControllerTest extends AbstractControllerTest {
    @Autowired
    private UserService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
//                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user_1));
    }

    @Disabled
    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
//                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(getUpdatedDto())))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(service.get(USER_1_ID), UsersUtil.updateFromDto(
                new User(USER_1_ID, "User_1", "user_1@yandex.ru", "password_1", Role.USER),
                getUpdatedDto()));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
//                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(service.getAll(), admin, guest, user_2, user_3);
    }
}
