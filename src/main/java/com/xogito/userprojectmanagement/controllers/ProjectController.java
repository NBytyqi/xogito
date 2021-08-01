package com.xogito.userprojectmanagement.controllers;

import com.xogito.userprojectmanagement.entities.Project;
import com.xogito.userprojectmanagement.entities.dtos.AssignProjectDto;
import com.xogito.userprojectmanagement.entities.dtos.ProjectDto;
import com.xogito.userprojectmanagement.entities.dtos.ProjectInfoDto;
import com.xogito.userprojectmanagement.exceptions.ProjectNotFoundException;
import com.xogito.userprojectmanagement.exceptions.UserNotFoundException;
import com.xogito.userprojectmanagement.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectInfoDto>> getAllProjects(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return new ResponseEntity(projectService.getAllProjects(pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectDto projectDto) {
        return new ResponseEntity(projectService.saveProject(projectDto), HttpStatus.OK);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long projectId, @Valid @RequestBody ProjectDto projectDto) throws ProjectNotFoundException {
        return new ResponseEntity(projectService.updateProject(projectDto, projectId), HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable Long projectId) throws ProjectNotFoundException {
        projectService.deleteProject(projectId);
    }

    @PostMapping("/assign-user")
    public void assignUserToProject(@Valid @RequestBody AssignProjectDto assignProjectDto) throws UserNotFoundException, ProjectNotFoundException {
        projectService.assignUserToProject(assignProjectDto);
    }

    @PostMapping("/unassign-user")
    public void unAssignUserToProject(@Valid @RequestBody AssignProjectDto assignProjectDto) throws UserNotFoundException, ProjectNotFoundException {
        projectService.unAassignUserToProject(assignProjectDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectInfoDto>> searchProjectsByName(@RequestParam String query, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return new ResponseEntity(projectService.searchProjectsByName(query, pageable), HttpStatus.OK);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long projectId) throws ProjectNotFoundException {
        return new ResponseEntity<>(projectService.getProjectById(projectId), HttpStatus.OK);
    }

}