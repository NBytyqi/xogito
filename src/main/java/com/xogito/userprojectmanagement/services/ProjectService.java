package com.xogito.userprojectmanagement.services;

import com.xogito.userprojectmanagement.entities.Project;
import com.xogito.userprojectmanagement.entities.dtos.AssignProjectDto;
import com.xogito.userprojectmanagement.entities.dtos.ProjectDto;
import com.xogito.userprojectmanagement.entities.dtos.ProjectInfoDto;
import com.xogito.userprojectmanagement.exceptions.ProjectNotFoundException;
import com.xogito.userprojectmanagement.exceptions.UserNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {

    Project saveProject(ProjectDto projectDto);
    ProjectDto updateProject(ProjectDto projectDto, Long projectId) throws ProjectNotFoundException;
    void deleteProject(Long projectId) throws ProjectNotFoundException;
    void assignUserToProject(AssignProjectDto assignProjectDto) throws UserNotFoundException, ProjectNotFoundException;
    List<ProjectInfoDto> searchProjectsByName(String projectName, Pageable pageable);
    ProjectDto getProjectById(Long projectId) throws ProjectNotFoundException;
    List<ProjectInfoDto> getAllProjects(Pageable pageable);
}