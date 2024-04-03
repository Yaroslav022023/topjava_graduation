package com.topjava.graduation.web.user;

import com.topjava.graduation.dto.UserDto;
import com.topjava.graduation.model.User;
import com.topjava.graduation.service.UserService;
import com.topjava.graduation.util.UsersUtil;
import com.topjava.graduation.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.topjava.graduation.TestUtil.userHttpBasic;
import static com.topjava.graduation.UserTestData.*;
import static com.topjava.graduation.util.UsersUtil.createNewFromDto;
import static com.topjava.graduation.util.exception.ErrorType.VALIDATION_ERROR;
import static com.topjava.graduation.web.user.ProfileRestController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileRestControllerTest extends AbstractControllerTest {
    @Autowired
    private UserService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user_1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void register() throws Exception {
        UserDto newUserDto = getNewDto();
        User newUser = createNewFromDto(newUserDto);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUserDto, newUserDto.getPassword())))
                .andExpect(status().isCreated())
                .andDo(print());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    void registerInvalidName() throws Exception {
        UserDto newUser = getNewDto();
        newUser.setName("");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(2, "[name] must not be blank", "[name] size must be between 2 and 255"));
    }

    @Test
    void registerInvalidEmail() throws Exception {
        UserDto newUser = getNewDto();
        newUser.setEmail("invalid");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[email] must be a well-formed email address"));
    }

    @Test
    void registerInvalidPassword() throws Exception {
        UserDto newUser = getNewDto();
        newUser.setPassword("1234");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[password] size must be between 5 and 128"));
    }

    @Test
    void registerHtmlUnsafeName() throws Exception {
        UserDto newUser = getNewDto();
        newUser.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[name] Unsafe html content"));
    }

    @Test
    void registerHtmlUnsafeEmail() throws Exception {
        UserDto newUser = getNewDto();
        newUser.setEmail("<script>alert(123)</script>@gmail.com");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(2, "[email] Unsafe html content",
                        "[email] must be a well-formed email address"));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void registerDuplicateEmail() throws Exception {
        UserDto newUser = getNewDto();
        newUser.setName(user_1.getName());
        newUser.setEmail(user_1.getEmail());
        newUser.setPassword(user_1.getPassword());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "A user with this email already exists"));
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user_2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(getUpdatedDto(), getUpdatedDto().getPassword())))
                .andExpect(status().isNoContent())
                .andDo(print());
        User updated = UsersUtil.updateFromDto(getNew(), getUpdatedDto());
        updated.setId(USER_2_ID);
        USER_MATCHER.assertMatch(service.get(USER_2_ID), updated);
    }

    @Test
    void updateInvalidName() throws Exception {
        UserDto updated = getUpdatedDto();
        updated.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user_1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(2, "[name] must not be blank", "[name] size must be between 2 and 255"));
    }

    @Test
    void updateInvalidEmail() throws Exception {
        UserDto updated = getUpdatedDto();
        updated.setEmail("invalid");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user_1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[email] must be a well-formed email address"));
    }

    @Test
    void updateInvalidPassword() throws Exception {
        UserDto updated = getUpdatedDto();
        updated.setPassword("1234");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user_1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[password] size must be between 5 and 128"));
    }

    @Test
    void updateHtmlUnsafeName() throws Exception {
        UserDto updated = getUpdatedDto();
        updated.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user_1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[name] Unsafe html content"));
    }

    @Test
    void updateHtmlUnsafeEmail() throws Exception {
        UserDto updated = getUpdatedDto();
        updated.setEmail("<script>alert(123)</script>@gmail.com");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user_1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(2, "[email] Unsafe html content",
                        "[email] must be a well-formed email address"));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void updateDuplicateEmail() throws Exception {
        UserDto updated = getUpdatedDto();
        updated.setEmail(user_2.getEmail());
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user_1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "A user with this email already exists"));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user_1)))
                .andExpect(status().isNoContent())
                .andDo(print());
        USER_MATCHER.assertMatch(service.getAll(), admin, guest, user_2, user_3);
    }
}