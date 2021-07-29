package com.xogito.userprojectmanagement.services;

import com.xogito.userprojectmanagement.entities.dtos.UserDto;
import com.xogito.userprojectmanagement.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserDto saveUser(UserDto userDto);
    void deleteUser(Long userId) throws UserNotFoundException;
    UserDto updateUser(UserDto userDto, Long userId) throws UserNotFoundException;
    List<UserDto> getAllUsers(int pageNumber, int pageSize);
    List<UserDto> getUsersByNameOrEmail(String query, int pageNumber, int pageSize);
}