package com.xogito.userprojectmanagement.entities.dtos;

import com.xogito.userprojectmanagement.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private Long projectId;
    List<User> assignedUsers;
    private String name;
    private String description;
}
