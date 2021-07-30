package com.xogito.userprojectmanagement.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProjectNotFoundException extends Exception {
    private Long projectId;
}
