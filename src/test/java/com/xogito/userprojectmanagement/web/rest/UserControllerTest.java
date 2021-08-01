package com.xogito.userprojectmanagement.web.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xogito.userprojectmanagement.UserProjectManagementApplication;
import com.xogito.userprojectmanagement.controllers.UserController;
import com.xogito.userprojectmanagement.entities.User;
import com.xogito.userprojectmanagement.entities.dtos.UserDto;
import com.xogito.userprojectmanagement.mappers.UserToDtoMapper;
import com.xogito.userprojectmanagement.repositories.UserRepository;
import com.xogito.userprojectmanagement.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserProjectManagementApplication.class)
@ActiveProfiles("test")
public class UserControllerTest {

    private static final String NEW_USER_NAME = "NewUserName";
    private static final String NEW_USER_EMAIL = "test@gmail.com";

    private static final String EDITED_USER_NAME = "NewUserName";
    private static final String EDITED_USER_EMAIL = "edited@gmail.com";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private UserToDtoMapper userMapper;
    @Autowired
    ObjectMapper objectMapper;

    MockMvc usersMockMvc;

    public UserControllerTest() { }

    @BeforeEach
    void setup() {
        UserController userController = new UserController(userService);
        usersMockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userRepository.deleteAll();
    }

    @Test
    public void createUser() throws Exception {
        int databaseSizeBeforeCreating = userRepository.findAll().size();

        createUser(NEW_USER_NAME, NEW_USER_EMAIL);

        int databaseSizeAfterCreating = userRepository.findAll().size();
        assertNotEquals(databaseSizeBeforeCreating, databaseSizeAfterCreating);

        User createdUser = userRepository.findAll().get(databaseSizeAfterCreating - 1);
        assertThat(createdUser.getName()).isEqualTo(NEW_USER_NAME);
        assertThat(createdUser.getEmail()).isEqualTo(NEW_USER_EMAIL);
    }

    @Test
    public void editProjectTest() throws Exception {
        createUser(NEW_USER_NAME, NEW_USER_EMAIL);

        List<User> allUsers = userRepository.findAll();
        int databaseSizeAfterCreating = allUsers.size();
        assertThat(databaseSizeAfterCreating).isEqualTo(1);

        UserDto createdUser = userMapper.mapUserToDto(allUsers.get(0));
        createdUser.setName(EDITED_USER_NAME);
        createdUser.setEmail(EDITED_USER_EMAIL);

        usersMockMvc.perform(put("/users/" + createdUser.getUserId())
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(userMapper.mapDtoToUser(createdUser)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        UserDto editedUser = userMapper.mapUserToDto(userRepository.findById(createdUser.getUserId()).get());
        assertEquals(editedUser.getName(), EDITED_USER_NAME);
        assertEquals(editedUser.getEmail(), EDITED_USER_EMAIL);
    }

    @Test
    public void deleteProjectTest() throws Exception {
        createUser(NEW_USER_NAME, NEW_USER_EMAIL);

        List<User> allUsers = userRepository.findAll();
        int databaseSizeAfterCreating = allUsers.size();
        assertThat(databaseSizeAfterCreating).isEqualTo(1);

        User createdUser = allUsers.get(0);
        Long userId = createdUser.getUserId();

        usersMockMvc.perform(delete("/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        allUsers = userRepository.findAll();
        int databaseSizeAfterDeleting = allUsers.size();

        assertEquals(databaseSizeAfterDeleting, 0);
    }

    @Test
    public void getAllProjectsTest() throws Exception {
        int databaseSizeBeforeCreating = userRepository.findAll().size();

        createUser(NEW_USER_NAME, NEW_USER_EMAIL);

        List<User> allUsers = userRepository.findAll();

        int databaseSizeAfterCreating = allUsers.size();

        assertThat(databaseSizeAfterCreating).isEqualTo(1);

        User firstUser = allUsers.get(0);

        assertNotEquals(databaseSizeBeforeCreating, databaseSizeAfterCreating);

        assertThat(firstUser.getName()).isEqualTo(NEW_USER_NAME);
        assertThat(firstUser.getEmail()).isEqualTo(NEW_USER_EMAIL);
    }

    private User createUser(String name, String email) throws Exception {
        User newUser = User.builder()
                .name(name)
                .email(email)
                .build();
        String result = usersMockMvc.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(userMapper.mapUserToDto(newUser)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(result, new TypeReference<>(){});
    }
}
