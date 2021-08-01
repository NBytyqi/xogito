package com.xogito.userprojectmanagement.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignProjectDto {

    @NotNull(message = "User ID is required")
    private Long userId;
    @NotNull(message = "Project ID is required")
    private Long projectId;
}