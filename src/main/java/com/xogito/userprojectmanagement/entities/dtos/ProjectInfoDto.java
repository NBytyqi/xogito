package com.xogito.userprojectmanagement.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoDto {

    private Long projectId;
    private String name;
    private String description;
}
