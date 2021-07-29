package com.xogito.userprojectmanagement.mappers;

import com.xogito.userprojectmanagement.entities.Project;
import com.xogito.userprojectmanagement.entities.dtos.ProjectInfoDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface ProjectToInfoDtoMapper {

    ProjectInfoDto mapProjectToInfoDto(Project project);
    List<ProjectInfoDto> mapProjectsToInfoDtos(List<Project> projects);
}