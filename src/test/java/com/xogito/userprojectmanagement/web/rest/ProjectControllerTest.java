package com.xogito.userprojectmanagement.web.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xogito.userprojectmanagement.UserProjectManagementApplication;
import com.xogito.userprojectmanagement.controllers.ProjectController;
import com.xogito.userprojectmanagement.controllers.UserController;
import com.xogito.userprojectmanagement.entities.Project;
import com.xogito.userprojectmanagement.entities.User;
import com.xogito.userprojectmanagement.entities.dtos.AssignProjectDto;
import com.xogito.userprojectmanagement.entities.dtos.ProjectDto;
import com.xogito.userprojectmanagement.entities.dtos.ProjectInfoDto;
import com.xogito.userprojectmanagement.mappers.ProjectToDtoMapper;
import com.xogito.userprojectmanagement.mappers.UserToDtoMapper;
import com.xogito.userprojectmanagement.repositories.ProjectRepository;
import com.xogito.userprojectmanagement.repositories.UserRepository;
import com.xogito.userprojectmanagement.services.ProjectService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserProjectManagementApplication.class)
@ActiveProfiles("test")
public class ProjectControllerTest {

    private static final String NEW_PROJECT_NAME = "NewProjectName";
    private static final String NEW_PROJECT_DESCRIPTION = "NewProjectDescription";

    private static final String EDITED_PROJECT_NAME = "EditedProjectName";
    private static final String EDITED_PROJECT_DESCRIPTION = "EditedProjectDescription";

    private static final String NEW_USER_NAME = "NewUserName";
    private static final String NEW_USER_EMAIL = "test@gmail.com";


    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private ProjectToDtoMapper projectMapper;
    @Autowired
    private UserToDtoMapper userMapper;
    @Autowired
    ObjectMapper objectMapper;


    MockMvc projectsMockMvc;
    MockMvc usersMockMvc;

    public ProjectControllerTest() {}

    @BeforeEach
    void setup() {
        ProjectController projectController = new ProjectController(projectService);
        UserController userController = new UserController(userService);

        projectsMockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
        usersMockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void createProject() throws Exception {
        int databaseSizeBeforeCreating = projectRepository.findAll().size();

        createProject(NEW_PROJECT_NAME, NEW_PROJECT_DESCRIPTION);

        int databaseSizeAfterCreating = projectRepository.findAll().size();
        assertNotEquals(databaseSizeBeforeCreating, databaseSizeAfterCreating);

        Project createdProject = projectRepository.findAll().get(databaseSizeAfterCreating - 1);
        assertThat(createdProject.getName()).isEqualTo(NEW_PROJECT_NAME);
        assertThat(createdProject.getDescription()).isEqualTo(NEW_PROJECT_DESCRIPTION);
        assertThat(createdProject.getAssignedUsers()).isEmpty();
    }

    @Test
    public void getAllProjectsTest() throws Exception {
        int databaseSizeBeforeCreating = projectRepository.findAll().size();

        createProject(NEW_PROJECT_NAME, NEW_PROJECT_DESCRIPTION);

        List<Project> allProjects = projectRepository.findAll();

        int databaseSizeAfterCreating = allProjects.size();

        assertThat(databaseSizeAfterCreating).isEqualTo(1);

        Project firstProject = allProjects.get(0);

        assertNotEquals(databaseSizeBeforeCreating, databaseSizeAfterCreating);

        assertThat(firstProject.getName()).isEqualTo(NEW_PROJECT_NAME);
        assertThat(firstProject.getDescription()).isEqualTo(NEW_PROJECT_DESCRIPTION);
        assertThat(firstProject.getAssignedUsers()).isEmpty();
    }

    @Test
    public void editProjectTest() throws Exception {
        createProject(NEW_PROJECT_NAME, NEW_PROJECT_DESCRIPTION);

        List<Project> allProjects = projectRepository.findAll();
        int databaseSizeAfterCreating = allProjects.size();
        assertThat(databaseSizeAfterCreating).isEqualTo(1);

        ProjectDto createdProject = projectMapper.mapProjectToDto(allProjects.get(0));
        createdProject.setName(EDITED_PROJECT_NAME);
        createdProject.setDescription(EDITED_PROJECT_DESCRIPTION);

        projectsMockMvc.perform(put("/projects/" + createdProject.getProjectId())
               .accept(MediaType.APPLICATION_JSON)
               .content(TestUtils.convertObjectToJsonBytes(projectMapper.mapDtoToProject(createdProject)))
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());

        ProjectDto editedProject = projectMapper.mapProjectToDto(projectRepository.findById(createdProject.getProjectId()).get());
        assertEquals(editedProject.getName(), EDITED_PROJECT_NAME);
        assertEquals(editedProject.getDescription(), EDITED_PROJECT_DESCRIPTION);
        assertEquals(editedProject.getAssignedUsers(), new ArrayList<>());
    }

    @Test
    public void deleteProjectTest() throws Exception {
        createProject(NEW_PROJECT_NAME, NEW_PROJECT_DESCRIPTION);

        List<Project> allProjects = projectRepository.findAll();
        int databaseSizeAfterCreating = allProjects.size();
        assertThat(databaseSizeAfterCreating).isEqualTo(1);

        Project createdProject = allProjects.get(0);
        Long projectId = createdProject.getProjectId();

        projectsMockMvc.perform(delete("/projects/" + projectId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        allProjects = projectRepository.findAll();
        int databaseSizeAfterDeleting = allProjects.size();

        assertEquals(databaseSizeAfterDeleting, 0);
    }

    @Test
    public void getProjectById() throws Exception {

        Project newProject = Project.builder()
                                    .name(NEW_PROJECT_NAME)
                                    .description(NEW_PROJECT_DESCRIPTION)
                                    .build();

        String result = projectsMockMvc.perform(post("/projects")
                               .accept(MediaType.APPLICATION_JSON)
                               .content(TestUtils.convertObjectToJsonBytes(projectMapper.mapProjectToDto(newProject)))
                               .contentType(MediaType.APPLICATION_JSON))
                               .andExpect(status().isOk())
                               .andReturn()
                               .getResponse()
                               .getContentAsString();

        ProjectDto response =  objectMapper.readValue(result, new TypeReference<>(){});

        assertEquals(response.getName(), NEW_PROJECT_NAME);
        assertEquals(response.getDescription(), NEW_PROJECT_DESCRIPTION);
    }

    @Test
    public void assignUserToProjectTest() throws Exception {
        User user = createUser(NEW_USER_NAME, NEW_USER_EMAIL);
        Project project = createProject(NEW_PROJECT_NAME, NEW_PROJECT_DESCRIPTION);

        AssignProjectDto assignProjectDto  = AssignProjectDto.builder()
                                                            .userId(user.getUserId())
                                                            .projectId(project.getProjectId())
                                                            .build();



        projectsMockMvc.perform(post("/projects/assign-user")
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(assignProjectDto))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        Optional<Project> optionalProject = projectRepository.findById(project.getProjectId());
        if(optionalProject.isPresent()) {
            Project createdProject = optionalProject.get();
            assertEquals(user, createdProject.getAssignedUsers().stream().collect(Collectors.toList()).get(0));
            //REMOVE THE USER FROM ASSIGNMENT IN ORDER TO BE ABLE TO DELETE ALL IN USERCONTROLLERTEST SETUP METHOD
            createdProject.getAssignedUsers().clear();
            projectsMockMvc.perform(post("/projects/unassign-user")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectToJsonBytes(assignProjectDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } else {
            fail();
        }
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

    private Project createProject(String name, String description) throws Exception {
        Project newProject = Project.builder()
                                    .name(name)
                                    .description(description)
                                    .build();

        String result = projectsMockMvc.perform(post("/projects")
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(projectMapper.mapProjectToDto(newProject)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(result, new TypeReference<>(){});
    }
}