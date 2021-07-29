package com.xogito.userprojectmanagement.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignProjectDto {

    private Long userId;
    private Long projectId;

}
