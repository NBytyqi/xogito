package com.xogito.userprojectmanagement.services.impl;

import com.xogito.userprojectmanagement.entities.dtos.AssignProjectDto;
import com.xogito.userprojectmanagement.entities.dtos.ProjectDto;
import com.xogito.userprojectmanagement.entities.Project;
import com.xogito.userprojectmanagement.entities.User;
import com.xogito.userprojectmanagement.entities.dtos.ProjectInfoDto;
import com.xogito.userprojectmanagement.exceptions.ProjectNotFoundException;
import com.xogito.userprojectmanagement.exceptions.UserNotFoundException;
import com.xogito.userprojectmanagement.mappers.ProjectToDtoMapper;
import com.xogito.userprojectmanagement.mappers.ProjectToInfoDtoMapper;
import com.xogito.userprojectmanagement.repositories.ProjectRepository;
import com.xogito.userprojectmanagement.repositories.UserRepository;
import com.xogito.userprojectmanagement.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private ProjectToDtoMapper projectMapper = Mappers.getMapper(ProjectToDtoMapper.class);
    private ProjectToInfoDtoMapper projectInfoMapper = Mappers.getMapper(ProjectToInfoDtoMapper.class);

    @Override
    public Project saveProject(ProjectDto projectDto) {
        return projectRepository.save(projectMapper.mapDtoToProject(projectDto));
    }

    @Override
    public ProjectDto updateProject(ProjectDto projectDto, Long projectId) throws ProjectNotFoundException {
        Project existingProject = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        if(Optional.ofNullable(projectDto).isPresent()) {
            if(Optional.ofNullable(projectDto.getName()).isPresent()) {
                existingProject.setName(projectDto.getName());
            }
            if(Optional.ofNullable(projectDto.getDescription()).isPresent()) {
                existingProject.setDescription(projectDto.getDescription());
            }
        }

        return projectMapper.mapProjectToDto(projectRepository.save(existingProject));
    }

    @Override
    public void deleteProject(Long projectId) throws ProjectNotFoundException {
        Project existingProject = projectRepository.findById(projectId).orElseThrow(()-> new ProjectNotFoundException(projectId));
        projectRepository.delete(existingProject);
    }

    @Override
    public void assignUserToProject(AssignProjectDto assignProjectDto) throws UserNotFoundException, ProjectNotFoundException {
        Long userId = assignProjectDto.getUserId();
        Long projectId = assignProjectDto.getProjectId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        project.getAssignedUsers().add(user);
        projectRepository.save(project);
    }

    @Override
    public List<ProjectInfoDto> searchProjectsByName(String projectName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return projectInfoMapper.mapProjectsToInfoDtos(projectRepository.findByNameContaining(projectName, pageable));
    }

    @Override
    public ProjectDto getProjectById(Long projectId) throws ProjectNotFoundException {
        return projectMapper.mapProjectToDto(projectRepository.findById(projectId).orElseThrow(() ->new ProjectNotFoundException(projectId)));
    }

    @Override
    public List<ProjectDto> getAllProjects(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return projectMapper.mapProjectsToDtos(projectRepository.findAll(pageable).getContent());
    }
}
