package com.xogito.userprojectmanagement.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignProjectDto {

    private Long userId;
    private Long projectId;

}
