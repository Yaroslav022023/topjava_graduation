package com.topjava.graduation.web.user;

import com.topjava.graduation.UserTestData;
import com.topjava.graduation.model.User;
import com.topjava.graduation.service.UserService;
import com.topjava.graduation.util.exception.NotFoundException;
import com.topjava.graduation.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.topjava.graduation.MealTestData.NOT_FOUND;
import static com.topjava.graduation.TestUtil.userHttpBasic;
import static com.topjava.graduation.UserTestData.*;
import static com.topjava.graduation.util.exception.ErrorType.DATA_NOT_FOUND;
import static com.topjava.graduation.util.exception.ErrorType.VALIDATION_ERROR;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestController.REST_URL + '/';
    @Autowired
    private UserService service;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(users));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(DATA_NOT_FOUND))
                .andExpect(detailMessages(0));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user_1)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-email?email=" + user_1.getEmail())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user_1));
    }

    @Test
    void getByEmailNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-email?email=" + "not_found@gmail.com")
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(DATA_NOT_FOUND))
                .andExpect(detailMessages(0));
    }

    @Test
    void createWithLocation() throws Exception {
        User newUser = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isCreated())
                .andDo(print());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    void createInvalidName() throws Exception {
        User newUser = getNew();
        newUser.setName("");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(2, "[name] must not be blank", "[name] size must be between 2 and 255"));
    }

    @Test
    void createInvalidEmail() throws Exception {
        User newUser = getNew();
        newUser.setEmail("invalid");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[email] must be a well-formed email address"));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void createDuplicateEmail() throws Exception {
        User newUser = getNew();
        newUser.setName(user_1.getName());
        newUser.setEmail(user_1.getEmail());
        newUser.setPassword(user_1.getPassword());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "A user with this email already exists"));
    }

    @Test
    void createInvalidPassword() throws Exception {
        User newUser = getNew();
        newUser.setPassword("1234");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[password] size must be between 5 and 128"));
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + USER_2_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(getUpdated(), getUpdated().getPassword())))
                .andExpect(status().isNoContent())
                .andDo(print());
        USER_MATCHER.assertMatch(service.get(USER_2_ID), getUpdated());
    }

    @Test
    void updateInvalidName() throws Exception {
        User updated = getUpdated();
        updated.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL + USER_2_ID)
                .with(userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(2, "[name] must not be blank", "[name] size must be between 2 and 255"));
    }

    @Test
    void updateInvalidEmail() throws Exception {
        User updated = getUpdated();
        updated.setEmail("invalid");
        perform(MockMvcRequestBuilders.put(REST_URL + USER_2_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[email] must be a well-formed email address"));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void updateDuplicateEmail() throws Exception {
        User updated = getUpdated();
        updated.setEmail(user_1.getEmail());
        perform(MockMvcRequestBuilders.put(REST_URL + USER_2_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "A user with this email already exists"));
    }

    @Test
    void updateInvalidPassword() throws Exception {
        User updated = getUpdated();
        updated.setPassword("1234");
        perform(MockMvcRequestBuilders.put(REST_URL + USER_2_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessages(1, "[password] size must be between 5 and 128"));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER_1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertThrows(NotFoundException.class, () -> service.get(USER_1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(DATA_NOT_FOUND))
                .andExpect(detailMessages(0));
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + USER_1_ID)
                .with(userHttpBasic(admin))
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertFalse(service.get(USER_1_ID).isEnabled());
    }
}
