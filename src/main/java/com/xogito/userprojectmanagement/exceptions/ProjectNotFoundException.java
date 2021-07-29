package com.xogito.userprojectmanagement.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProjectNotFoundException extends Exception {
    private Long projectId;
}
