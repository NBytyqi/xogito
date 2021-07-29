package com.xogito.userprojectmanagement.mappers;

import com.xogito.userprojectmanagement.entities.dtos.ProjectDto;
import com.xogito.userprojectmanagement.entities.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface ProjectToDtoMapper {

    Project mapDtoToProject(ProjectDto projectDto);
    List<Project> mapDtosToProjects(List<ProjectDto> projectDtos);

    ProjectDto mapProjectToDto(Project project);
    List<ProjectDto> mapProjectsToDtos(List<Project> projects);
}