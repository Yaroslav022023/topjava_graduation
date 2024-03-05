package com.topjava.graduation.web.user;

import com.topjava.graduation.dto.UserDto;
import com.topjava.graduation.model.Role;
import com.topjava.graduation.model.User;
import com.topjava.graduation.service.UserService;
import com.topjava.graduation.util.UsersUtil;
import com.topjava.graduation.web.AbstractControllerTest;
import com.topjava.graduation.UserTestData;
import com.topjava.graduation.web.json.TestJsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.topjava.graduation.TestUtil.userHttpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileRestControllerTest extends AbstractControllerTest {
    @Autowired
    private UserService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL)
                .with(userHttpBasic(UserTestData.user_1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTestData.USER_MATCHER.contentJson(UserTestData.user_1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register() throws Exception {
        UserDto newDto = new UserDto(null, "newName", "newemail@ya.ru", "newPassword");
        User newUser = UsersUtil.createNewFromDto(newDto);
        ResultActions action = perform(MockMvcRequestBuilders.post(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestJsonUtil.writeValue(newDto)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = UserTestData.USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        UserTestData.USER_MATCHER.assertMatch(created, newUser);
        UserTestData.USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(UserTestData.user_1))
                .content(TestJsonUtil.writeValue(UserTestData.getUpdatedDto())))
                .andDo(print())
                .andExpect(status().isNoContent());
        UserTestData.USER_MATCHER.assertMatch(service.get(UserTestData.USER_1_ID), UsersUtil.updateFromDto(
                new User(UserTestData.USER_1_ID, "User_1", "user_1@yandex.ru", "password_1", Role.USER),
                UserTestData.getUpdatedDto()));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ProfileRestController.REST_URL)
                .with(userHttpBasic(UserTestData.user_1)))
                .andExpect(status().isNoContent());
        UserTestData.USER_MATCHER.assertMatch(service.getAll(), UserTestData.admin, UserTestData.guest, UserTestData.user_2, UserTestData.user_3);
    }
}
