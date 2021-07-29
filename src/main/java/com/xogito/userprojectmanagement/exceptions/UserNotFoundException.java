package com.xogito.userprojectmanagement.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserNotFoundException extends Exception {
    private Long userId;
}
